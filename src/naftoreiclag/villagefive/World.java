/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class World
{
    Node rootNode;
    AssetManager assetManager;
    
    World(Node rootNode, AssetManager assetManager)
    {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }
    
    public <SomeEntity extends Entity> SomeEntity spawnEntity(Class<SomeEntity> entityType, Vector2f vector2f)
    {
        SomeEntity e;
        try
        {
            e = entityType.getConstructor().newInstance();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            
            return null;
        }
        
        e.assertWorld(this);
        e.assertNode(loadNode(e.getModelName()));
        
        return e;
    }
    public Node loadNode(String modelName)
    {
        System.out.println(modelName);
        System.out.println("Models/perry/Cube.mesh.xml");
        
        Object test = assetManager.loadModel(modelName);
        System.out.println(test.getClass().getName());
        
        Node body = (Node) test;
        body.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        body.getControl(AnimControl.class);
        
        body.getNumControls();
        
        rootNode.attachChild(body);
        
        return body;
        
    }
    
    public Control[] getControls(Node model)
    {
        int numControls = model.getNumControls();
        
        Control[] controls = new Control[numControls];
        for(int i = 0; i < numControls; ++ i)
        {
            controls[i] = model.getControl(i);
        }
        
        return controls;
    }

    void destroyEntity(Entity aThis)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
