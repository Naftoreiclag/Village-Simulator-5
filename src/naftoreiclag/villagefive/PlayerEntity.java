/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.scene.Node;

class PlayerEntity extends Entity
{
    public Node bod;
    public CircleBody body = new CircleBody();

    public PlayerEntity(Node bod)
    {
        this.bod = bod;
        
        this.body.setLink(bod);
    }

    @Override
    public void bindSceneGraph(Node rootNode)
    {
        rootNode.attachChild(bod);
    }

    @Override
    public void bindPhysics(Space space)
    {
        space.attachBody(body);
    }

    @Override
    public void update(float tpf)
    {
        body.mot.set(1, 1);
    }

}
