/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

/*
 * An easy-to-use interface for synchronizing all the different bodies involved.
 * 
 * Important: All sub-classes must have one 0-argument constructor. (or have no written constructors at all)
 */

public abstract class Entity
{
    protected World world;
    protected Model model;
    
    public abstract void meow();
    
    public void assertWorld(World world)
    {
        this.world = world;
    }
    public void assertModel(Model model)
    {
        this.model = model;
    }
    
    public abstract String getModelName();
}
