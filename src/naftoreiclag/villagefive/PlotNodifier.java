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
import naftoreiclag.villagefive.Plot.Decal;
import naftoreiclag.villagefive.Plot.Face;
import naftoreiclag.villagefive.Plot.Vertex;
import naftoreiclag.villagefive.Polygon.Hole;
import naftoreiclag.villagefive.util.ModelBuilder;

public class PlotNodifier
{
    public static Node nodify(Plot plot)
    {
        Node ret = new Node();
        ModelBuilder mb = new ModelBuilder();
        
        for(Face f : plot.getFaces())
        {
            Polygon p = new Polygon();
            
            for(int i = 0; i < f.getVertexes().length; ++ i)
            {
                Vertex v = plot.getVerticies()[f.getVertexes()[i]];
                
                
                p.vecs.add(new Vector2f((float) v.getX(), (float) v.getZ()));
            }
            
            for(Decal decal : plot.getEdges())
            {
                Hole jam = new Hole();
                jam.point = decal.getVertA();
                jam.x = (float) decal.getDistance();
                jam.y = 0f;
                jam.h = 5f;
                jam.w = (float) decal.width;

                
                ArrayList<Hole> h = new ArrayList<Hole>();
                h.add(jam);
                
                // fix dis
                p.holesPerEdge.put(decal.getVertA(), h);
            }
            
            //    public Mesh doit(float thickness, float height, float texWidth, float texHeight)
            Mesh m = p.doit(0.4f, 7f, 3f, 3f);
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
