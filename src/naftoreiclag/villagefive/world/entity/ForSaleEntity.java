/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import com.jme3.material.Material;
import naftoreiclag.villagefive.PlayerController;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;
import naftoreiclag.villagefive.world.PhysWorld;
import naftoreiclag.villagefive.world.body.EntityBody;
import naftoreiclag.villagefive.world.plot.Plot;
import org.dyn4j.geometry.Circle;

public class ForSaleEntity extends Entity
{
    public Plot thingy;
    
    @Override
    public String getTypeName()
    {
        return "forsale";
    }

    @Override
    public void createNode()
    {
        node = ModelManipulator.loadNode("Models/Knight.mesh.j3o");
        Material mat = world.assetManager.loadMaterial("Materials/TestMaterial.j3m");
        node.setMaterial(mat);
    }

    @Override
    public void createBody(PhysWorld world)
    {
        Vec2 location = this.getLocation();
        body = new EntityBody(this);
        body.addFixture(new Circle(1), 5);
        body.setMass();
       
        this.setLocation(location);
        
        world.addBody(body);
    }
    
    @Override
    public void onInteract(PlayerController other)
    {
        
        System.out.println("house purchased");
        
        other.property.add(thingy);
        
        this.destroySelf();
        
    }

}
