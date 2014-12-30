/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.world.entity.Entity;
import com.jme3.material.Material;
import com.jme3.scene.Node;

public class PinguinEntity extends Entity
{
    @Override
    public void meow()
    {
        System.out.println("pinguin");
    }
    @Override
    public String getModelName()
    {
        return "Models/Knight.mesh.j3o";
    }
    @Override
    public void tick(float tpf)
    {
    }
    
    @Override
    public void loadNode()
    {
        super.loadNode();
        
        Material mat = world.assetManager.loadMaterial("Materials/TestMaterial.j3m");
        
        node.setMaterial(mat);
        
          
    }

}
