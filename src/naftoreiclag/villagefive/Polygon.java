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
    
    public Map<Integer, ArrayList<Hole>> binmods = new HashMap<Integer, ArrayList<Hole>>();
    
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
            Vector2f q = get(i);
            Vector2f a = get(i + 1);
            Vector2f b = get(i + 2);
            
            Vector2f aq = q.subtract(a).normalizeLocal();
            Vector2f ab = b.subtract(a).normalizeLocal();
            
            // if this is positive, then angle baq is non-reflexive
            float baqC = -aq.determinant(ab);
            
            Vector2f ad = aq.add(ab).multLocal(FastMath.sign(baqC)).multLocal(thickness);
            
            /*
             *                 D
             * 
             * 
             *         B       A        Q
             */
            
            ret.vecs.add(ad.addLocal(a));
        }
        
        return ret;
    }
    
    public void makeWall(float tall, float textureWidth, ModelBuilder mb, boolean reverseNormals)
    {
        float textureHeight = textureWidth;
        float tH = tall / textureHeight;
        
        for(int i = 0; i < vecs.size(); ++ i)
        {
            Vector2f a = get(i);
            Vector2f b = get(i + 1);
            
            /*
             *     [viewing front of wall]
             * 
             *  D       C
             * 
             * 
             *  A       B
             */
            
            Vector2f ab = b.subtract(a).normalizeLocal();
            
            float tW = a.distance(b) / textureWidth;
            tW /= 2f;
            
            Vector3f joe;
            
            if(reverseNormals)
            {
                joe = new Vector3f(-ab.y, 0f, ab.x);
            }
            else
            {
                joe = new Vector3f(ab.y, 0f, -ab.x);
            }
            
            
            Vertex C = new Vertex(b.x, tall, b.y, joe, -tW, 0f);
            Vertex D = new Vertex(a.x, tall, a.y, joe, tW, 0f);
            Vertex A = new Vertex(a.x,   0f, a.y, joe, tW, tH);
            Vertex B = new Vertex(b.x,   0f, b.y, joe, -tW, tH);
            
            if(binmods.containsKey(i))
            {
                List<Hole> holes = binmods.get(i);
                
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
                    
                    float r = hole.y + hole.h;
                    float g = hole.y;
                    
                    tX -= prevZ.distance(q) / textureWidth;
                    Vertex T = new Vertex(q.x, tall, q.y, joe, tX, 0f);
                    Vertex R = new Vertex(q.x,    r, q.y, joe, tX, (tall - r) / textureHeight);
                    Vertex G = new Vertex(q.x,    g, q.y, joe, tX, (tall - g) / textureHeight);
                    Vertex V = new Vertex(q.x,   0f, q.y, joe, tX, tH);
                    
                    tX -= q.distance(z) / textureWidth;
                    Vertex P = new Vertex(z.x, tall, z.y, joe, tX, 0f);
                    Vertex Y = new Vertex(z.x,    r, z.y, joe, tX, (tall - r) / textureHeight);
                    Vertex J = new Vertex(z.x,    g, z.y, joe, tX, (tall - g) / textureHeight);
                    Vertex N = new Vertex(z.x,   0f, z.y, joe, tX, tH);
                    
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
    
    
    public Mesh doit(float thickness, float height, float texWidth)
    {
        ModelBuilder mb = new ModelBuilder();
        
        Polygon shrunk = this.shrink(thickness);
        
        this.makeWall(height, texWidth, mb, false);
        shrunk.makeWall(height, texWidth, mb, true);
        
        return mb.bake();
    }
    /*
    public void tall(float height, float texWidth, ModelBuilder mb)
    {
        this.tall(height, texWidth, mb, false);
    }
    public Mesh tall(float height, float texWidth)
    {
        ModelBuilder mb = new ModelBuilder();
        tall(height, texWidth, mb);
                
        return mb.bake();
    }
    */
    public Vector2f get(int i)
    {
        return vecs.get(i % vecs.size());
    }
}
