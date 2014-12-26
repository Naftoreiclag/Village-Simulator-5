/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import naftoreiclag.villagefive.Plot.Edge;
import naftoreiclag.villagefive.Plot.Vertex;
import naftoreiclag.villagefive.util.ModelBuilder;

public class PlotNodifier
{
    public static Node nodify(Plot plot)
    {
        
        Node ret = new Node();
        ModelBuilder mb = new ModelBuilder();
        
        // make walls
        for(Edge e : plot.getEdges())
        {
            /* D    C
             * 
             * 
             * A     B
             */
            
            
            Vertex av = plot.getVerticies()[e.getVertA()];
            Vertex bv = plot.getVerticies()[e.getVertB()];
            Vector3f a = new Vector3f((float) av.getX(), 0f, (float) av.getZ());
            Vector3f b = new Vector3f((float) bv.getX(), 0f, (float) bv.getZ());
            Vector3f c = b.add(0f, 3f, 0f);
            Vector3f d = a.add(0f, 3f, 0f);
            
            mb.addQuad(a, b, c, d);
        }
        
        
        Mesh m = mb.bake();
        Geometry geo = new Geometry("", m);
        geo.setMaterial(Main.debugMat);
        
        ret.attachChild(geo);
        
        return ret;
    }
}
