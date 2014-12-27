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
import java.util.List;
import naftoreiclag.villagefive.util.ModelBuilder;
import naftoreiclag.villagefive.util.ModelBuilder.Vertex;


// Clockwise
public class Polygon
{
    public List<Vector2f> vecs = new ArrayList<Vector2f>();
    
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
            float baqC = aq.determinant(ab);
            
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
    
    public void tall(float height, ModelBuilder mb, boolean reverseNormals)
    {
        for(int i = 0; i < vecs.size(); ++ i)
        {
            Vector2f a = get(i);
            Vector2f b = get(i + 1);
            
            /*
             *     [viewing front of wall]
             * 
             *  C       D
             * 
             * 
             *  B       A
             */
            
            Vector2f ab = b.subtract(b).normalizeLocal();
            Vertex C = new Vertex(b.x, 1f, b.y, new Vector3f(-ab.y, 0f, ab.x), 0f, 0f);
            Vertex D = new Vertex(a.x, 1f, a.y, new Vector3f(-ab.y, 0f, ab.x), 1f, 0f);
            Vertex A = new Vertex(a.x, 0f, a.y, new Vector3f(-ab.y, 0f, ab.x), 1f, 1f);
            Vertex B = new Vertex(b.x, 0f, b.y, new Vector3f(-ab.y, 0f, ab.x), 0f, 1f);
            
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
    
    public void tall(float height, ModelBuilder mb)
    {
        this.tall(height, mb, false);
    }
    public Mesh tall(float height)
    {
        ModelBuilder mb = new ModelBuilder();
        tall(height, mb);
                
        return mb.bake();
    }
    
    public Mesh doit(float thickness, float height)
    {
        ModelBuilder mb = new ModelBuilder();
        
        Polygon shrunk = this.shrink(thickness);
        
        this.tall(height, mb);
        shrunk.tall(height, mb, true);
        
        return mb.bake();
    }
    
    public Vector2f get(int i)
    {
        return vecs.get(i % vecs.size());
    }
}
