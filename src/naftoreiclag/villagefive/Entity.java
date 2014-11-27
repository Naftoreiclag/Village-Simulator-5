/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public abstract class Entity
{
    public Spatial aeBody;
    public CircleBody fisBody = new CircleBody();

    protected Entity(Spatial bod)
    {
        this.aeBody = bod;
        this.fisBody.linkToSpatial(bod);
    }

    public void bindSceneGraph(Node rootNode)
    {
        rootNode.attachChild(aeBody);
    }
    
    public void bindPhysics(Space space)
    {
        space.attachBody(fisBody);
    }

    public abstract void update(float tpf);
}
