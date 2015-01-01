/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.scenegraph;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder.Vertex;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;


// Counter-Clockwise

/*
 *  D<------C
 *  |       ^
 *  v inside|
 *  A------>B
 * 
 *  When traversing the perimeter of the polygon, the area on the left is the "inside."
 * 
 */
public class Polygon
{
    public List<Vec2> vecs = new ArrayList<Vec2>();
    
    public Map<Integer, ArrayList<Hole>> holesPerEdge = new HashMap<Integer, ArrayList<Hole>>();
    
    // Doors and windows in walls
    public static class Hole
    {
        // On what edge
        public int point;
        
        public float x;
        public float y;
        public float w;
        public float h;
    }
    
    public enum RoundingMethod
    {
        round,
        sharp,
        box
    }
    
    // "Inflate" a polygon
    public Polygon margin(float thickness)
    {
        Polygon ret = new Polygon();
        
        /*
         * Process:
         * 1. Duplicate all the edges and expand them by a given amount
         * 2. "Diminish" the resulting polygon by removing any unusual geometry
         * 3. Copy over the hole data
         * 
         */
        
        // Loop through my own verticies
        for(int i = 0; i < vecs.size(); ++ i)
        {
            /*
             *  Top-down view:
             *  
             *   [inside]
             * 
             *   D-------C
             *   ^       ^
             *   |       |
             *   A-------B
             * 
             * A: you are here
             * B: next vertex
             * 
             */
            
            Vec2 a = get(i);
            Vec2 b = get(i + 1);
            
            Vec2 ab = b.subtract(a).normalizeLocal();
            Vec2 ad = new Vec2(ab.getYf(), -ab.getXf());
            ad.multLocal(thickness);
            
            Vec2 d = a.add(ad);
            Vec2 c = b.add(ad);
            
            ret.vecs.add(d);
            ret.vecs.add(c);
        }
        
        ret = ret.diminish();
        
        // Hole check
        for(int i = 0; i < vecs.size(); ++ i)
        {
        	// Has holes
            if(holesPerEdge.containsKey(i))
            {
                
                
                /* map:
                 *  
                 *      C
                 *     /
                 *    /
                 *   /
                 *  A------------B
                 * 
                 *  A: you are here (original polygon)
                 *  B: next vertex on this polygon
                 *  C: the new position for A after margining/padding
                 * 
                 */
                
                
                // How far it moved
                Vec2 a = get(i);
                Vec2 c = ret.vecs.get(i);
                Vec2 b = get(i + 1);
                
                Vec2 ac = c.subtract(a);
                Vec2 ab = b.subtract(a);
                
                float offset = ac.dot(ab);
                offset /= ab.length();
                
                ArrayList<Hole> origHoles = holesPerEdge.get(i);
                ArrayList<Hole> offsetHoles = new ArrayList<Hole>();
                
                for(int j = 0; j < origHoles.size(); ++ j)
                {
                    Hole origHole = origHoles.get(j);
                    Hole offsetHole = new Hole();
                    
                    offsetHole.point = origHole.point;
                    offsetHole.h = origHole.h;
                    offsetHole.w = origHole.w;
                    offsetHole.x = origHole.x;
                    offsetHole.y = origHole.y;
                    
                    offsetHole.x -= offset;
                    
                    offsetHoles.add(offsetHole);
                }
                
                ret.holesPerEdge.put(i, offsetHoles);
            }
        }
        
        
        return ret;
    }
    
    // Replace each line segment with a single vertex that is the original line's intersection with its incident edge
    public Polygon diminish()
    {
        Polygon ret = new Polygon();
        
        // Loop through my own verticies
        for(int i = 0; i < vecs.size(); i += 2)
        {

            /*
             *           ^
             *           C
             *           |
             *  <-A------N------B->
             *           |
             *           D
             *           v
             * 
             * N: new vertex
             * 
             */
        	
            Vec2 a = get(i - 2);
            Vec2 b = get(i - 1);
            Vec2 c = get(i);
            Vec2 d = get(i + 1);
            
            ret.vecs.add(intersect(a, b, c, d));
        }
        
        return ret;
    }
    
    // Point where two lines meet
    public static Vec2 intersect(Vec2 a, Vec2 b, Vec2 c, Vec2 d)
    {
        /*
         *           ^
         *           C
         *           |
         *  <-A------R------B->
         *           |
         *           D
         *           v
         * 
         *  R: return value
         * 
         */
    	
    	float crossProduct = (a.getXf() - b.getXf()) * (c.getYf() - d.getYf()) - (a.getYf() - b.getYf()) * (c.getXf() - d.getXf());

		// Parallel or equal -> infinite or no solutions
		if (crossProduct == 0)
		{
			return null;
		}

		// Has an intersection
		else
		{
			float xi = ((c.getXf() - d.getXf()) * (a.getXf() * b.getYf() - a.getYf() * b.getXf()) - (a.getXf() - b.getXf()) * (c.getXf() * d.getYf() - c.getYf() * d.getXf())) / crossProduct;
			float yi = ((c.getYf() - d.getYf()) * (a.getXf() * b.getYf() - a.getYf() * b.getXf()) - (a.getYf() - b.getYf()) * (c.getXf() * d.getYf() - c.getYf() * d.getXf())) / crossProduct;

			return new Vec2(xi, yi);
		}
    }
    
    // Even-odd thing
    public boolean inside(Vec2 test)
	{
		Vec2 prevPoint = this.get(-1);
		boolean even = false;
		for(int i = 0; i < vecs.size(); ++ i)
		{
			Vec2 currPoint = this.get(i);
			
			if((currPoint.getYf() > test.getYf()) != (prevPoint.getYf() > test.getYf()) && (test.getXf() < (prevPoint.getXf() - currPoint.getXf()) * (test.getYf() - currPoint.getYf()) / (prevPoint.getYf() - currPoint.getYf()) + currPoint.getXf()))
			{
				even = !even;
			}
			
			prevPoint = currPoint;
		}
        return even;
    }
    
    // Make into a floor
    public void makeRoof(ModelBuilder mb, float thickness, float height, float textureWidth, float textureHeight)
    {
        Polygon out = this.margin(thickness);
        
        for(int i = 0; i < vecs.size(); ++ i)
        {
            Vec2 a = this.get(i);
            Vec2 b = this.get(i + 1);
            Vec2 c = out.get(i + 1);
            Vec2 d = out.get(i);
            
            
            Vertex A = new Vertex(a.getXf(), height, a.getYf(), Vector3f.UNIT_Y, a.getXf() / textureWidth, a.getYf() / textureHeight);
            Vertex B = new Vertex(b.getXf(), height, b.getYf(), Vector3f.UNIT_Y, b.getXf() / textureWidth, b.getYf() / textureHeight);
            Vertex C = new Vertex(c.getXf(), 0, c.getYf(), Vector3f.UNIT_Y, c.getXf() / textureWidth, c.getYf() / textureHeight);
            Vertex D = new Vertex(d.getXf(), 0, d.getYf(), Vector3f.UNIT_Y, d.getXf() / textureWidth, d.getYf() / textureHeight);
            
            mb.addQuad(D, C, B, A);
        }
        mb.addAppendOrigin(0, height, 0);
        this.makeFloor(mb, textureWidth, textureHeight);
        mb.addAppendOrigin(0, -height, 0);
    }
    
    // Make into a floor
    public void makeFloor(ModelBuilder mb, float textureWidth, float textureHeight)
    {
        List<PolygonPoint> points = new ArrayList<PolygonPoint>();
        
        for(Vec2 a : vecs)
        {
            points.add(new PolygonPoint(a.getXf(), a.getYf()));
        }
        
        org.poly2tri.geometry.polygon.Polygon polygon = new org.poly2tri.geometry.polygon.Polygon(points);
        
        Poly2Tri.triangulate(polygon);
        
        for(DelaunayTriangle tri : polygon.getTriangles())
        {
            TriangulationPoint a = tri.points[0];
            TriangulationPoint b = tri.points[1];
            TriangulationPoint c = tri.points[2];
            
            Vertex A = new Vertex(a.getXf(), 0f, a.getYf(), Vector3f.UNIT_Y, a.getXf() / textureWidth, a.getYf() / textureHeight);
            Vertex B = new Vertex(b.getXf(), 0f, b.getYf(), Vector3f.UNIT_Y, b.getXf() / textureWidth, b.getYf() / textureHeight);
            Vertex C = new Vertex(c.getXf(), 0f, c.getYf(), Vector3f.UNIT_Y, c.getXf() / textureWidth, c.getYf() / textureHeight);
            
            mb.addTriangle(A, B, C);
        }
    }
    
    // Make into a wall
    public void makeWall(ModelBuilder mb, float height, float textureWidth, float textureHeight, boolean reverseNormals)
    {
        float tH = height / textureHeight;
        
        for(int i = 0; i < vecs.size(); ++ i)
        {
            Vec2 a = get(i);
            Vec2 b = get(i + 1);
            
            /*
             *  [viewing front of wall]
             * 
             *  D-------C
             *  |       |
             *  |       |
             *  A-------B
             */
            
            Vec2 ab = b.subtract(a).normalizeLocal();
            
            float tW = a.distance(b) / textureWidth;
            tW /= 2f;
            
            Vector3f normalVec;
            
            if(reverseNormals)
            {
                normalVec = new Vector3f(-ab.getYf(), 0f, ab.getXf());
            }
            else
            {
                normalVec = new Vector3f(ab.getYf(), 0f, -ab.getXf());
            }
            
            Vertex C = new Vertex(b.getXf(), height, b.getYf(), normalVec, -tW, 0f);
            Vertex D = new Vertex(a.getXf(), height, a.getYf(), normalVec,  tW, 0f);
            Vertex A = new Vertex(a.getXf(),     0f, a.getYf(), normalVec,  tW, tH);
            Vertex B = new Vertex(b.getXf(),     0f, b.getYf(), normalVec, -tW, tH);
            
            if(holesPerEdge.containsKey(i))
            {
                List<Hole> holes = holesPerEdge.get(i);
                
                // Last thingy
                Vertex prevTOP = D;
                Vertex prevBOT = A;
                Vec2 prevZ = a;
                
                float tX = tW;
                
                for(int j = 0; j < holes.size(); j ++)
                {
                    /*
                     *    D   -T-----P-     -T-----P-    tall       C
                     *    |    |     |       |     |                |
                     *    |   -R-----Y-     -R-----Y-    r          |
                     *    |    |     |       |     |                |
                     *    |   -G-----J-     -G-----J-    g          |
                     *    |    |     |       |     |                |
                     *    A   -V-----N- ... -V-----N-    0f         B
                     *             
                     *                       q     z
                     */
                    
                    Hole hole = holes.get(j);
                    
                    Vec2 q = ab.mult(hole.x).addLocal(a);
                    Vec2 z = ab.mult(hole.w).addLocal(q);
                    
                    // Reusable y-values for the R,Y,G,J vertexes
                    float r = hole.y + hole.h;
                    float g = hole.y;
                    
                    tX -= prevZ.distance(q) / textureWidth;
                    Vertex T = new Vertex(q.getXf(), height, q.getYf(), normalVec, tX, 0f);
                    Vertex R = new Vertex(q.getXf(),    r, q.getYf(), normalVec, tX, (height - r) / textureHeight);
                    Vertex G = new Vertex(q.getXf(),    g, q.getYf(), normalVec, tX, (height - g) / textureHeight);
                    Vertex V = new Vertex(q.getXf(),   0f, q.getYf(), normalVec, tX, tH);
                    
                    tX -= q.distance(z) / textureWidth;
                    Vertex P = new Vertex(z.getXf(), height, z.getYf(), normalVec, tX, 0f);
                    Vertex Y = new Vertex(z.getXf(),    r, z.getYf(), normalVec, tX, (height - r) / textureHeight);
                    Vertex J = new Vertex(z.getXf(),    g, z.getYf(), normalVec, tX, (height - g) / textureHeight);
                    Vertex N = new Vertex(z.getXf(),   0f, z.getYf(), normalVec, tX, tH);
                    
                    prevZ = z;
                    
                    if(reverseNormals)
                    {
                        mb.addQuad(T, P, Y, R);
                        mb.addQuad(G, J, N, V);
                    }
                    else
                    {
                        mb.addQuad(P, T, R, Y);
                        mb.addQuad(J, G, V, N);
                    }
                    
                    if(reverseNormals)
                    {
                        mb.addQuad(prevTOP, T, V, prevBOT);
                    }
                    else
                    {
                        mb.addQuad(T, prevTOP, prevBOT, V);
                    }
                    prevTOP = P;
                    prevBOT = N;
                }
                
                if(reverseNormals)
                {
                    mb.addQuad(prevTOP, C, B, prevBOT);
                }
                else
                {
                    mb.addQuad(C, prevTOP, prevBOT, B);
                }
            }
            else
            {
                if(reverseNormals)
                {
                    mb.addQuad(D, C, B, A);
                }
                else
                {
                    mb.addQuad(C, D, A, B);
                }
            }
        }
    }
    
    public Mesh genOutsideWall(float thickness, float height, float texWidth, float texHeight)
    {
        ModelBuilder mb = new ModelBuilder();
        Polygon grow = this.margin(thickness);
        grow.makeWall(mb, height, texWidth, texHeight, false);
        return mb.bake();
    }
    
    public Mesh genInsideWall(float thickness, float height, float texWidth, float texHeight)
    {
        ModelBuilder mb = new ModelBuilder();
        Polygon shrunk = this.margin(-thickness);
        shrunk.makeWall(mb, height, texWidth, texHeight, true);
        return mb.bake();
    }
    
    public Mesh genFloor(float thickness, float height, float texWidth, float texHeight)
    {
        ModelBuilder mb = new ModelBuilder();
        mb.setAppendOrigin(0, 0.02f, 0f);
        this.makeFloor(mb, texWidth, texHeight);
        return mb.bake();
    }
    
    
    public Mesh genRoof(float texWidth, float texHeight)
    {
        ModelBuilder mb = new ModelBuilder();
        mb.setAppendOrigin(0, 7f, 0f);
        this.makeRoof(mb, 1f, 1f, texWidth, texHeight);
        return mb.bake();
    }
    
    public Vec2 get(int i)
    {
        return vecs.get(((i % vecs.size()) + vecs.size()) % vecs.size());
    }
}
