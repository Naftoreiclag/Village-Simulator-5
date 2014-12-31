/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.scenegraph;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<Vector2f> vecs = new ArrayList<Vector2f>();
    
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
            
            Vector2f a = get(i);
            Vector2f b = get(i + 1);
            
            Vector2f ab = b.subtract(a).normalizeLocal();
            Vector2f ad = new Vector2f(ab.y, -ab.x);
            ad.multLocal(thickness);
            
            Vector2f d = a.add(ad);
            Vector2f c = b.add(ad);
            
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
                Vector2f a = get(i);
                Vector2f c = ret.vecs.get(i);
                Vector2f b = get(i + 1);
                
                Vector2f ac = c.subtract(a);
                Vector2f ab = b.subtract(a);
                
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
        	
            Vector2f a = get(i - 2);
            Vector2f b = get(i - 1);
            Vector2f c = get(i);
            Vector2f d = get(i + 1);
            
            ret.vecs.add(intersect(a, b, c, d));
        }
        
        return ret;
    }
    
    // Point where two lines meet
    public static Vector2f intersect(Vector2f a, Vector2f b, Vector2f c, Vector2f d)
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
    	
    	float crossProduct = (a.x - b.x) * (c.y - d.y) - (a.y - b.y) * (c.x - d.x);

		// Parallel or equal -> infinite or no solutions
		if (crossProduct == 0)
		{
			return null;
		}

		// Has an intersection
		else
		{
			float xi = ((c.x - d.x) * (a.x * b.y - a.y * b.x) - (a.x - b.x) * (c.x * d.y - c.y * d.x)) / crossProduct;
			float yi = ((c.y - d.y) * (a.x * b.y - a.y * b.x) - (a.y - b.y) * (c.x * d.y - c.y * d.x)) / crossProduct;

			return new Vector2f(xi, yi);
		}
    }
    
    // Even-odd thing
    public boolean inside(Vector2f test)
	{
		Vector2f prevPoint = this.get(-1);
		boolean even = false;
		for(int i = 0; i < vecs.size(); ++ i)
		{
			Vector2f currPoint = this.get(i);
			
			if((currPoint.y > test.y) != (prevPoint.y > test.y) && (test.x < (prevPoint.x - currPoint.x) * (test.y - currPoint.y) / (prevPoint.y - currPoint.y) + currPoint.x))
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
            Vector2f a = this.get(i);
            Vector2f b = this.get(i + 1);
            Vector2f c = out.get(i + 1);
            Vector2f d = out.get(i);
            
            
            Vertex A = new Vertex(a.x, height, a.y, Vector3f.UNIT_Y, a.x / textureWidth, a.y / textureHeight);
            Vertex B = new Vertex(b.x, height, b.y, Vector3f.UNIT_Y, b.x / textureWidth, b.y / textureHeight);
            Vertex C = new Vertex(c.x, 0, c.y, Vector3f.UNIT_Y, c.x / textureWidth, c.y / textureHeight);
            Vertex D = new Vertex(d.x, 0, d.y, Vector3f.UNIT_Y, d.x / textureWidth, d.y / textureHeight);
            
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
        
        for(Vector2f a : vecs)
        {
            points.add(new PolygonPoint(a.x, a.y));
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
            Vector2f a = get(i);
            Vector2f b = get(i + 1);
            
            /*
             *  [viewing front of wall]
             * 
             *  D-------C
             *  |       |
             *  |       |
             *  A-------B
             */
            
            Vector2f ab = b.subtract(a).normalizeLocal();
            
            float tW = a.distance(b) / textureWidth;
            tW /= 2f;
            
            Vector3f normalVec;
            
            if(reverseNormals)
            {
                normalVec = new Vector3f(-ab.y, 0f, ab.x);
            }
            else
            {
                normalVec = new Vector3f(ab.y, 0f, -ab.x);
            }
            
            Vertex C = new Vertex(b.x, height, b.y, normalVec, -tW, 0f);
            Vertex D = new Vertex(a.x, height, a.y, normalVec,  tW, 0f);
            Vertex A = new Vertex(a.x,     0f, a.y, normalVec,  tW, tH);
            Vertex B = new Vertex(b.x,     0f, b.y, normalVec, -tW, tH);
            
            if(holesPerEdge.containsKey(i))
            {
                List<Hole> holes = holesPerEdge.get(i);
                
                // Last thingy
                Vertex prevTOP = D;
                Vertex prevBOT = A;
                Vector2f prevZ = a;
                
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
                    
                    Vector2f q = ab.mult(hole.x).addLocal(a);
                    Vector2f z = ab.mult(hole.w).addLocal(q);
                    
                    // Reusable y-values for the R,Y,G,J vertexes
                    float r = hole.y + hole.h;
                    float g = hole.y;
                    
                    tX -= prevZ.distance(q) / textureWidth;
                    Vertex T = new Vertex(q.x, height, q.y, normalVec, tX, 0f);
                    Vertex R = new Vertex(q.x,    r, q.y, normalVec, tX, (height - r) / textureHeight);
                    Vertex G = new Vertex(q.x,    g, q.y, normalVec, tX, (height - g) / textureHeight);
                    Vertex V = new Vertex(q.x,   0f, q.y, normalVec, tX, tH);
                    
                    tX -= q.distance(z) / textureWidth;
                    Vertex P = new Vertex(z.x, height, z.y, normalVec, tX, 0f);
                    Vertex Y = new Vertex(z.x,    r, z.y, normalVec, tX, (height - r) / textureHeight);
                    Vertex J = new Vertex(z.x,    g, z.y, normalVec, tX, (height - g) / textureHeight);
                    Vertex N = new Vertex(z.x,   0f, z.y, normalVec, tX, tH);
                    
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
    
    public Vector2f get(int i)
    {
        return vecs.get(((i % vecs.size()) + vecs.size()) % vecs.size());
    }
}
