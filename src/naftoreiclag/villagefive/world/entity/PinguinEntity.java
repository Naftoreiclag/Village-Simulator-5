/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import com.jme3.material.Material;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;

public class PinguinEntity extends Entity
{
    @Override
    public void tick(float tpf)
    {
    }
    
    @Override
    public void loadNode()
    {
        node = ModelManipulator.loadNode("Models/Knight.mesh.j3o");
        
        Material mat = world.assetManager.loadMaterial("Materials/TestMaterial.j3m");
        
        node.setMaterial(mat);
        
          
    }

}
