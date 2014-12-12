/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.scene.Node;

public class BlendBoxEntity extends Entity
{
    @Override
    public void meow()
    {
        System.out.println("box");
    }
    
    @Override
    public void assertNode(Node newNode)
    {
        super.assertNode(newNode);
    }
    
    

    @Override
    public String getModelName()
    {
        return "Models/Box.mesh.j3o";
    }

    @Override
    public void tick(float tpf)
    {
    }

}
