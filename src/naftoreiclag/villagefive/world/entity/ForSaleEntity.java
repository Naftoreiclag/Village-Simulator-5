/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import naftoreiclag.villagefive.PlayerController;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.GrassMaker;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;
import naftoreiclag.villagefive.world.PhysWorld;
import naftoreiclag.villagefive.world.body.EntityBody;
import naftoreiclag.villagefive.world.plot.Plot;
import org.dyn4j.geometry.Circle;

public class ForSaleEntity extends Entity
{
    public Plot thingy;
    
    @Override
    public String getEntityId()
    {
        return "forsale";
    }

    @Override
    public void createNode()
    {
        node = ModelManipulator.loadNode("Models/Knight.mesh.j3o");
        Material mat = world.assetManager.loadMaterial("Materials/TestMaterial.j3m");
        node.setMaterial(mat);
        
        /*
        node = new Node();
        
        Mesh m = GrassMaker.makeGrass(20, 900, 2, 1, true);
        Geometry geo = new Geometry("", m);
        Material mat2 = world.assetManager.loadMaterial("Materials/tallGrass.j3m");
        //mat2.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geo.setMaterial(mat2);
        geo.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.setShadowMode(RenderQueue.ShadowMode.Receive);
        
        node.attachChild(geo);
        */
        
        
        
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
