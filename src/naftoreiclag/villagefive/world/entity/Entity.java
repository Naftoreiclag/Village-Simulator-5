/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import naftoreiclag.villagefive.world.World;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.world.Mundane;

/*
 * An easy-to-use interface for synchronizing all the different bodies involved.
 * 
 * Important: All sub-classes must have one 0-argument constructor. (or have no written constructors at all)
 */

// Position and rotation are stored in the node
public abstract class Entity extends Mundane
{
    public World world;
    protected Node node;
    
    public void destroy()
    {
        this.world.destroyEntity(this);
    }
    
    public void assertWorld(World world)
    {
        this.world = world;
    }
    
    @Override
    public abstract void loadNode();
    
    @Override
    public Node getNode()
    {
        return node;
    }
    
    public abstract void tick(float tpf);
    
    public void attachSpatial(Spatial spatial)
    {
        this.node.attachChild(spatial);
    }
    
}
