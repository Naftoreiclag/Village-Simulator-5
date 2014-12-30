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
import naftoreiclag.villagefive.PlotSerial.Decal;
import naftoreiclag.villagefive.PlotSerial.Face;
import naftoreiclag.villagefive.PlotSerial.Vertex;
import naftoreiclag.villagefive.Polygon.Hole;
import naftoreiclag.villagefive.util.ModelBuilder;

public class PlotNodifier
{
    public static Node nodify(PlotSerial plot)
    {
        Node ret = new Node();
        ModelBuilder mb = new ModelBuilder();
        
        // For each room
        for(Face room : plot.getFaces())
        {
            // Create a new polygon to represent it
            Polygon p = new Polygon();
            
            // Copy over the vertex data
            for(int i = 0; i < room.getVertexes().length; ++ i)
            {
                // Get the vertex by its id
                Vertex vert = plot.getVerticies()[room.getVertexes()[i]];
                
                // Copy it over
                p.vecs.add(new Vector2f((float) vert.getX(), (float) vert.getZ()));
                
                // Copy over decal (hole) data
                for(Decal decal : plot.getEdges())
                {
                    // If this decal does not apply
                    if(decal.getVertA() != vert.getId())
                    {
                        // Skip it
                        continue;
                    }
                    
                    Hole jam = new Hole();
                    jam.point = decal.getVertA();
                    jam.x = (float) decal.getDistance();
                    jam.y = 0f;
                    jam.h = 5f;
                    jam.w = (float) decal.width;

                    System.out.println(jam.point);

                    ArrayList<Hole> h = new ArrayList<Hole>();
                    h.add(jam);

                    // fix dis
                    p.holesPerEdge.put(i, h);
                }
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
