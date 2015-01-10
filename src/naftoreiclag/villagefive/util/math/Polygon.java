/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder.Vertex;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Rectangle;
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
    private List<Vec2> vecs = new ArrayList<Vec2>();
    
    public void addVec(Vec2 nu)
    {
        vecs.add(nu);
    }
    public int size()
    {
        return vecs.size();
    }
    
    public Map<Integer, ArrayList<Hole>> holesPerEdge = new HashMap<Integer, ArrayList<Hole>>();
    
    // Doors and windows in walls
    public static class Hole
    {
        // On what edge
        public int point;
        
        public double x;
        public double y;
        public double w;
        public double h;
    }
    
    public enum RoundingMethod
    {
        round,
        sharp,
        box
    }
    
    // "Inflate" a polygon
    public Polygon margin(double thickness)
    {
        Polygon ret = new Polygon();
        
        /*
         * Process:
         * 1. Duplicate all the edges individually (x2 vertexes afterward) and expand outward them by a given amount
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
             *   A-------B
             *   |       |
             *   v       v
             *   D-------C
             * 
             * A: you are here
             * B: next vertex
             * 
             */
            Vec2 a = get(i);
            Vec2 b = get(i + 1);
            
            Vec2 ab = b.subtract(a).normalizeLocal();
            Vec2 ad = new Vec2(ab.getYF(), -ab.getXF());
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
                 *    /      [inside]
                 *   /
                 *  A------------B
                 * 
                 *  A: you are here (original polygon)
                 *  B: next vertex on this polygon
                 *  C: the new position for A after padding
                 * 
                 */
                
                
                // How far it moved
                Vec2 a = get(i);
                Vec2 c = ret.vecs.get(i);
                Vec2 b = get(i + 1);
                
                Vec2 ac = c.subtract(a);
                Vec2 ab = b.subtract(a);
                
                double offset = ac.dot(ab);
                offset /= ab.lenF();
                
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
    	
    	double crossProduct = (a.getXF() - b.getXF()) * (c.getYF() - d.getYF()) - (a.getYF() - b.getYF()) * (c.getXF() - d.getXF());

		// Parallel or equal -> infinite or no solutions
		if (crossProduct == 0)
		{
			return null;
		}

		// Has an intersection
		else
		{
			double xi = ((c.getXF() - d.getXF()) * (a.getXF() * b.getYF() - a.getYF() * b.getXF()) - (a.getXF() - b.getXF()) * (c.getXF() * d.getYF() - c.getYF() * d.getXF())) / crossProduct;
			double yi = ((c.getYF() - d.getYF()) * (a.getXF() * b.getYF() - a.getYF() * b.getXF()) - (a.getYF() - b.getYF()) * (c.getXF() * d.getYF() - c.getYF() * d.getXF())) / crossProduct;

			return new Vec2(xi, yi);
		}
    }
    
    
    // Would these two intersect if they were line segments? (General case aka no coinciding lines)
    public static boolean segsIntersect(Vec2 a, Vec2 b, Vec2 c, Vec2 d)
    {
    	return (
                    ((a.getX() - c.getX()) * (d.getY() - c.getY())) - ((a.getY() - c.getY()) * (d.getX() - c.getX())) > 0 != 
                    ((b.getX() - c.getX()) * (d.getY() - c.getY())) - ((b.getY() - c.getY()) * (d.getX() - c.getX())) > 0
               ) && (
                    ((d.getX() - a.getX()) * (b.getY() - a.getY())) - ((d.getY() - a.getY()) * (b.getX() - a.getX())) > 0 != 
                    ((c.getX() - a.getX()) * (b.getY() - a.getY())) - ((c.getY() - a.getY()) * (b.getX() - a.getX())) > 0
               );
    }
    
    // Check if a point is inside or outside the polygon (works in all cases)
    public boolean inside(Vec2 test)
	{
		Vec2 prevPoint = this.get(-1);
		boolean even = false;
		for(int i = 0; i < vecs.size(); ++ i)
		{
			Vec2 currPoint = this.get(i);
			
			if((currPoint.getYF() > test.getYF()) != (prevPoint.getYF() > test.getYF()) && (test.getXF() < (prevPoint.getXF() - currPoint.getXF()) * (test.getYF() - currPoint.getYF()) / (prevPoint.getYF() - currPoint.getYF()) + currPoint.getXF()))
			{
				even = !even;
			}
			
			prevPoint = currPoint;
		}
        return even;
    }
    
    
    // 
    public void makeBody(Body body, double thickness)
    {
        for(int i = 0; i < vecs.size(); ++ i)
        {
            Vec2 a = this.get(i);
            Vec2 b = this.get(i + 1);
            
            if(holesPerEdge.containsKey(i))
            {
                Vec2 ab = b.subtract(a).normalizeLocal();
                
                Vec2 prev = a;
            
                List<Hole> holes = holesPerEdge.get(i);
                
                for(int j = 0; j < holes.size(); j ++)
                {
                    /*
                     *          Holes
                     *            v
                     *    A----V     N--...--V     N-------B
                     *             
                     *            1    ->    q  2  z
                     */
                    
                    Hole hole = holes.get(j);
                    
                    // If it is a window then skip
                    if(hole.y > 0)
                    {
                        continue;
                    }
                    
                    Vec2 q = ab.mult(hole.x).addLocal(a);
                    Vec2 z = ab.mult(hole.w).addLocal(q);
                    
                    double length = prev.dist(q);
                    Rectangle wall = new Rectangle(length, thickness);
                    
                    wall.rotate(prev.angleTo(q));
                    wall.translate((prev.getX() + q.getX()) / 2, (prev.getY() + q.getY()) / 2);
                    
                    body.addFixture(wall);
                    
                    prev = z;
                    
                }

                double length = prev.dist(b);
                Rectangle wall = new Rectangle(length, thickness);

                wall.rotate(prev.angleTo(b));
                wall.translate((prev.getX() + b.getX()) / 2, (prev.getY() + b.getY()) / 2);

                body.addFixture(wall);
            }
            else
            {
                double length = a.dist(b);
            
                Rectangle wall = new Rectangle(length, thickness);

                // rotate first, translate second

                wall.rotate(a.angleTo(b));
                wall.translate((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);

                body.addFixture(wall);
            }
        }
        
        
    }

            
    // Make into a roolf
    public void makeRoof(ModelBuilder mb, double thickness, double height, double textureWidth, double textureHeight)
    {
        Polygon out = this.margin(thickness);
        
        for(int i = 0; i < vecs.size(); ++ i)
        {
            Vec2 a = this.get(i);
            Vec2 b = this.get(i + 1);
            Vec2 c = out.get(i + 1);
            Vec2 d = out.get(i);
            
            
            Vertex A = new Vertex(a.getXF(), height, a.getYF(), Vector3f.UNIT_Y, a.getXF() / textureWidth, a.getYF() / textureHeight);
            Vertex B = new Vertex(b.getXF(), height, b.getYF(), Vector3f.UNIT_Y, b.getXF() / textureWidth, b.getYF() / textureHeight);
            Vertex C = new Vertex(c.getXF(), 0, c.getYF(), Vector3f.UNIT_Y, c.getXF() / textureWidth, c.getYF() / textureHeight);
            Vertex D = new Vertex(d.getXF(), 0, d.getYF(), Vector3f.UNIT_Y, d.getXF() / textureWidth, d.getYF() / textureHeight);
            
            mb.addQuad(D, C, B, A);
        }
        mb.addAppendOrigin(0, height, 0);
        this.makeFloor(mb, textureWidth, textureHeight);
        mb.addAppendOrigin(0, -height, 0);
    }
    
    // Make into a floor
    public void makeFloor(ModelBuilder mb, double textureWidth, double textureHeight)
    {
        List<PolygonPoint> points = new ArrayList<PolygonPoint>();
        
        for(Vec2 a : vecs)
        {
            points.add(new PolygonPoint(a.getXF(), a.getYF()));
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
    public void makeWall(ModelBuilder mb, double height, double textureWidth, double textureHeight, boolean reverseNormals)
    {
        double tH = height / textureHeight;
        
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
            
            double tW = a.distF(b) / textureWidth;
            tW /= 2f;
            
            Vector3f normalVec;
            
            if(reverseNormals)
            {
                normalVec = new Vector3f(-ab.getYF(), 0f, ab.getXF());
            }
            else
            {
                normalVec = new Vector3f(ab.getYF(), 0f, -ab.getXF());
            }
            
            Vertex C = new Vertex(b.getXF(), height, b.getYF(), normalVec, -tW, 0f);
            Vertex D = new Vertex(a.getXF(), height, a.getYF(), normalVec,  tW, 0f);
            Vertex A = new Vertex(a.getXF(),     0f, a.getYF(), normalVec,  tW, tH);
            Vertex B = new Vertex(b.getXF(),     0f, b.getYF(), normalVec, -tW, tH);
            
            if(holesPerEdge.containsKey(i))
            {
                List<Hole> holes = holesPerEdge.get(i);
                
                // Last thingy
                Vertex prevTOP = D;
                Vertex prevBOT = A;
                Vec2 prevZ = a;
                
                double tX = tW;
                
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
                     *            1    ->    q  2  z
                     */
                    
                    Hole hole = holes.get(j);
                    
                    Vec2 q = ab.mult(hole.x).addLocal(a);
                    Vec2 z = ab.mult(hole.w).addLocal(q);
                    
                    // Reusable y-values for the R,Y,G,J vertexes
                    double r = hole.y + hole.h;
                    double g = hole.y;
                    
                    tX -= prevZ.distF(q) / textureWidth;
                    Vertex T = new Vertex(q.getXF(), height, q.getYF(), normalVec, tX, 0f);
                    Vertex R = new Vertex(q.getXF(),    r, q.getYF(), normalVec, tX, (height - r) / textureHeight);
                    Vertex G = new Vertex(q.getXF(),    g, q.getYF(), normalVec, tX, (height - g) / textureHeight);
                    Vertex V = new Vertex(q.getXF(),   0f, q.getYF(), normalVec, tX, tH);
                    
                    tX -= q.distF(z) / textureWidth;
                    Vertex P = new Vertex(z.getXF(), height, z.getYF(), normalVec, tX, 0f);
                    Vertex Y = new Vertex(z.getXF(),    r, z.getYF(), normalVec, tX, (height - r) / textureHeight);
                    Vertex J = new Vertex(z.getXF(),    g, z.getYF(), normalVec, tX, (height - g) / textureHeight);
                    Vertex N = new Vertex(z.getXF(),   0f, z.getYF(), normalVec, tX, tH);
                    
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
    
    public Mesh genOutsideWall(double thickness, double height, double texWidth, double texHeight)
    {
        ModelBuilder mb = new ModelBuilder();
        Polygon grow = this.margin(thickness);
        grow.makeWall(mb, height, texWidth, texHeight, false);
        return mb.bake();
    }
    
    public Mesh genInsideWall(double thickness, double height, double texWidth, double texHeight)
    {
        ModelBuilder mb = new ModelBuilder();
        Polygon shrunk = this.margin(-thickness);
        shrunk.makeWall(mb, height, texWidth, texHeight, true);
        return mb.bake();
    }
    
    public Mesh genFloor(double thickness, double height, double texWidth, double texHeight)
    {
        ModelBuilder mb = new ModelBuilder();
        mb.setAppendOrigin(0, 0.05f, 0f);
        this.makeFloor(mb, texWidth, texHeight);
        return mb.bake();
    }
    
    
    public Mesh genRoof(double texWidth, double texHeight)
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
