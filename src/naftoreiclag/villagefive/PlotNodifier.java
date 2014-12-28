/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.Plot.EdgeFeature;
import naftoreiclag.villagefive.Plot.Face;
import naftoreiclag.villagefive.Plot.Vertex;
import naftoreiclag.villagefive.util.ModelBuilder;

public class PlotNodifier
{
    public static Node nodify(Plot plot)
    {
        
        Node ret = new Node();
        ModelBuilder mb = new ModelBuilder();
        
        /*
        // make walls
        for(Face f : plot.getFaces())
        {
            for(int i = 0; i < f.getVertexes().length; ++ i)
            {
                int ai = f.getVertexes()[i];
                Vertex a = plot.getVerticies()[ai];
                
                int bi;
                if(i < f.getVertexes().length - 1)
                {
                    bi = f.getVertexes()[i + 1];
                }
                else
                {
                     bi = f.getVertexes()[0];
                }
                Vertex b = plot.getVerticies()[bi];
                
                ModelBuilder.Vertex A = new ModelBuilder.Vertex((float) a.getX(), 0f, (float) a.getZ(), Vector3f.UNIT_Y, 0, 1);
                ModelBuilder.Vertex B = new ModelBuilder.Vertex((float) b.getX(), 0f, (float) b.getZ(), Vector3f.UNIT_Y, 1, 1);
                ModelBuilder.Vertex D = new ModelBuilder.Vertex((float) a.getX(), 3f, (float) a.getZ(), Vector3f.UNIT_Y, 0, 0);
                ModelBuilder.Vertex C = new ModelBuilder.Vertex((float) b.getX(), 3f, (float) b.getZ(), Vector3f.UNIT_Y, 1, 0);
                
                mb.addQuad(D, C, B, A);
            }
        }
        */
        
        for(Face f : plot.getFaces())
        {
            Polygon p = new Polygon();
            
            for(int i = 0; i < f.getVertexes().length; ++ i)
            {
                Vertex v = plot.getVerticies()[f.getVertexes()[i]];
                
                p.vecs.add(new Vector2f((float) v.getX(), (float) v.getZ()));
            }
            
            Mesh m = p.doit(0.4f, 4f, 4f);
            Geometry geo = new Geometry("", m);
            geo.setMaterial(Main.mat_debug_bricks);

            ret.attachChild(geo);
        }
        
        
        /*
        Mesh m = mb.bake();
        Geometry geo = new Geometry("", m);
        geo.setMaterial(Main.mat_debug);
        
        ret.attachChild(geo);
        */
        
        return ret;
    }
}
