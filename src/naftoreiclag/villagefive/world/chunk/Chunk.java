/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.chunk;

import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder;
import naftoreiclag.villagefive.world.Mundane;
import naftoreiclag.villagefive.world.PhysWorld;
import naftoreiclag.villagefive.world.World;
import org.dyn4j.dynamics.Body;

public class Chunk extends Mundane
{
    // 
    public static final int width = 100;
    public static final int height = 100;
    
    protected Node node;
    public int x;
    public int z;

    public void createNode()
    {
        ModelBuilder mb = new ModelBuilder();
        
        mb.addQuad(0, 0, 0, Vector3f.UNIT_Y, 0, 0, 
                   width, 0, 0, Vector3f.UNIT_Y, 4, 0, 
                   width, 0, height, Vector3f.UNIT_Y, 4, 4, 
                   0, 0, height, Vector3f.UNIT_Y, 0, 4);

        Mesh evenCells = mb.bake();
        Geometry geo = new Geometry("", evenCells);
        geo.setMaterial(Main.mat_grass);
        
        node = new Node();
        
        float floaty = 0.02f;
        for(int i = 1; i <= 3; ++ i)
        {
            Geometry geo2 = new Geometry("", evenCells);
            geo2.setMaterial(Main.mat_grass_tufts);
            geo2.setShadowMode(RenderQueue.ShadowMode.Receive);
            geo2.move(0, floaty * ((float) i), 0);
            node.attachChild(geo2);
        }
        
        
        
        node.attachChild(geo);
        node.move(x * width, 0, z * height);
    }

    public Node getNode()
    {
        return node;
    }

    @Override
    public void createBody(PhysWorld world) {}

    @Override
    protected Body getBody()
    {
        return null;
    }

}
