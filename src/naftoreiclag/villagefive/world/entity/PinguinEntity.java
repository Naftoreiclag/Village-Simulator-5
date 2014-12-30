/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import com.jme3.material.Material;

public class PinguinEntity extends Entity
{
    @Override
    public void tick(float tpf)
    {
    }
    
    @Override
    public void loadNode()
    {
        node = loadNode("Models/Knight.mesh.j3o");
        
        Material mat = world.assetManager.loadMaterial("Materials/TestMaterial.j3m");
        
        node.setMaterial(mat);
        
          
    }

}
