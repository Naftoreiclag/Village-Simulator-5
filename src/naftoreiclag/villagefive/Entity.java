/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/*
 * An easy-to-use interface for synchronizing all the different bodies involved.
 * 
 * Important: All sub-classes must have one 0-argument constructor. (or have no written constructors at all)
 */

// Position and rotation are stored in the node
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
    
    public abstract void tick(float tpf);
    
    public void attachSpatial(Spatial spatial)
    {
        this.node.attachChild(spatial);
    }
    
    public void teleport(Vector2f pos)
    {
        this.node.setLocalTranslation(pos.x, 0f, pos.y);
    }
    
    public abstract String getModelName();

    void move(Vector2f dir)
    {
        this.node.move(dir.x, 0f, dir.y);
    }
}
