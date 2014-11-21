/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import com.jme3.scene.Node;

public abstract class Entity
{
    public abstract void bindSceneGraph(Node rootNode);

    public abstract void bindPhysics(Space space);

    public abstract void update(float tpf);
}
