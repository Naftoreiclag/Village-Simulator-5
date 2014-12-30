/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.plot;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import naftoreiclag.villagefive.world.World;
import com.jme3.scene.Node;
import java.util.ArrayList;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.PlotSerial;
import naftoreiclag.villagefive.Polygon;
import naftoreiclag.villagefive.util.ModelBuilder;

public class Plot
{
    public PlotSerial data;
    public World world;
    public Node node;
    
    public Plot(PlotSerial data, World world)
    {
        this.data = data;
        this.world = world;
    }
    
    public void loadNode()
    {
        this.node = new Node();
        
        ModelBuilder mb = new ModelBuilder();
        
        // For each room
        for(PlotSerial.Face room : data.getFaces())
        {
            // Create a new polygon to represent it
            Polygon p = new Polygon();
            
            // Copy over the vertex data
            for(int i = 0; i < room.getVertexes().length; ++ i)
            {
                // Get the vertex by its id
                PlotSerial.Vertex vert = data.getVerticies()[room.getVertexes()[i]];
                
                // Copy it over
                p.vecs.add(new Vector2f((float) vert.getX(), (float) vert.getZ()));
                
                // Copy over decal (hole) data
                for(PlotSerial.Decal decal : data.getEdges())
                {
                    // If this decal does not apply
                    if(decal.getVertA() != vert.getId())
                    {
                        // Skip it
                        continue;
                    }
                    
                    Polygon.Hole jam = new Polygon.Hole();
                    jam.point = decal.getVertA();
                    jam.x = (float) decal.getDistance();
                    jam.y = 0f;
                    jam.h = 5f;
                    jam.w = (float) decal.width;

                    System.out.println(jam.point);

                    ArrayList<Polygon.Hole> h = new ArrayList<Polygon.Hole>();
                    h.add(jam);

                    // fix dis
                    p.holesPerEdge.put(i, h);
                }
            }
            
            //    public Mesh doit(float thickness, float height, float texWidth, float texHeight)
            Mesh m = p.doit(0.4f, 7f, 3f, 3f);
            Geometry geo = new Geometry("", m);
            geo.setMaterial(Main.mat_debug_bricks);

            node.attachChild(geo);
        }
    }
}
