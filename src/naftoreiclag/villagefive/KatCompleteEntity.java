/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.scene.Node;

public class KatCompleteEntity extends Entity
{
    @Override
    public void meow()
    {
        System.out.println("MEOW");
    }
    
    Node other;
    
    @Override
    public void assertNode(Node node)
    {
        super.assertNode(node);
        
        other = world.loadNode("Models/katty/Katty.mesh.j3o");
        other.setLocalTranslation(0, 2.44571f, 0);
        this.node.attachChild(other);
    }

    @Override
    public String getModelName()
    {
        return "Models/katty/KattyBody.mesh.j3o";
    }
}
