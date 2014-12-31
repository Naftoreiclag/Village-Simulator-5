/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.chunk;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder;
import naftoreiclag.villagefive.world.Mundane;

public class Chunk extends Mundane
{
    // 
    public static final int chunkWidth = 16;
    public static final int chunkHeight = 16;
    public static final float tileSize = 4f;
    
    protected Node node;
    public int x;
    public int z;

    public void loadNode()
    {
        ModelBuilder mb = new ModelBuilder();
        
        mb.addQuad(0, 0, 0, Vector3f.UNIT_Y, 0, 0, 
                   chunkWidth, 0, 0, Vector3f.UNIT_Y, 1, 0, 
                   chunkWidth, 0, chunkHeight, Vector3f.UNIT_Y, 1, 1, 
                   0, 0, chunkHeight, Vector3f.UNIT_Y, 0, 1);

        Mesh evenCells = mb.bake(tileSize, 0, tileSize);
        Geometry geo = new Geometry("", evenCells);
        geo.setMaterial(Main.mat_grass);
        
        
        node = new Node();
        node.attachChild(geo);
        node.move(x * chunkWidth * tileSize, 0, z * chunkHeight * tileSize);
    }

    public Node getNode()
    {
        return node;
    }

}
