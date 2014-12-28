/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import naftoreiclag.villagefive.util.ModelBuilder;
import naftoreiclag.villagefive.util.ModelBuilder.Vertex;


// Clockwise
public class Polygon
{
    public List<Vector2f> vecs = new ArrayList<Vector2f>();
    
    public Map<Integer, ArrayList<Hole>> holesPerEdge = new HashMap<Integer, ArrayList<Hole>>();
    
    public static class Hole
    {
        // On what edge
        public int point;
        
        public float x;
        public float y;
        public float w;
        public float h;
    }
    
    public Polygon shrink(float thickness)
    {
        // "inside" polygon
        Polygon ret = new Polygon();
        
        for(int i = 0; i < vecs.size(); ++ i)
        {
            Vector2f q = get(i - 1);
            Vector2f a = get(i);
            Vector2f b = get(i + 1);
            
            Vector2f aq = q.subtract(a).normalizeLocal();
            Vector2f ab = b.subtract(a).normalizeLocal();
            
            // if this is positive, then angle baq is non-reflexive
            float baqC = -aq.determinant(ab);
            
            Vector2f ad = aq.add(ab).multLocal(FastMath.sign(baqC)).multLocal(thickness);
            
            /*
             *           D
             *           |
             *           |
             *   Q-------A-------B
             */
            
            /*
             * 
             * Q     D
             * |    /
             * |   /
             * |  /
             * | /
             * A-----------B
             * 
             * 
             * 
             */
            
            ret.vecs.add(ad.add(a));
            
            // Has holes
            if(holesPerEdge.containsKey(i))
            {
                float offset = FastMath.sqrt(ad.lengthSquared() - FastMath.sqr(thickness));
                
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
    
    public void makeWall(float height, float textureWidth, float textureHeight, ModelBuilder mb, boolean reverseNormals)
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
    
    public Mesh doit(float thickness, float height, float texWidth, float texHeight)
    {
        ModelBuilder mb = new ModelBuilder();
        
        Polygon shrunk = this.shrink(thickness);
        
        this.makeWall(height, texWidth, texHeight, mb, false);
        shrunk.makeWall(height, texWidth, texHeight, mb, true);
        
        return mb.bake();
    }
    
    public Vector2f get(int i)
    {
        return vecs.get(((i % vecs.size()) + vecs.size()) % vecs.size());
    }
}
