/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

/*
 * An easy-to-use interface for synchronizing all the different bodies involved.
 * 
 * Important: All sub-classes must have one 0-argument constructor. (or have no written constructors at all)
 */

public abstract class Entity
{
    protected World world;
    protected Node node;
    
    public abstract void meow();
    
    public void destroy()
    {
        this.world.destroyEntity(this);
    }
    
    public void assertWorld(World world)
    {
        this.world = world;
    }
    public void assertNode(Node node)
    {
        this.node = node;
    }
    
    public void teleport(Vector2f pos)
    {
        this.node.setLocalTranslation(pos.x, 0f, pos.y);
    }
    
    public abstract String getModelName();
}
