/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

class PlayerEntity extends Entity
{
    public Node bod;

    public PlayerEntity(Node bod)
    {
        this.bod = bod;
    }

    @Override
    public void bindSceneGraph(Node rootNode)
    {
        rootNode.attachChild(bod);
    }

}
