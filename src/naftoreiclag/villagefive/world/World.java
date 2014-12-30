/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.control.Control;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.plot.Plot;
import naftoreiclag.villagefive.PlotSerial;

public class World
{
    public Node trueRootNode;
    public Node rootNode;
    public AssetManager assetManager;
    
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Plot> plots = new ArrayList<Plot>();
    
    public World(Node rootNode, AssetManager assetManager)
    {
        this.rootNode = new Node();
        this.trueRootNode = rootNode;
        this.trueRootNode.attachChild(this.rootNode);
        this.assetManager = assetManager;
    }
    
    /*
     * Spawn methods accept a "static" set of data (class, serializables...) which is then translated to physical
     * world-elements.
     * 
     * 
     */
    
    public Plot spawnPlot(PlotSerial plotType)
    {
        // Create the thing
        if(plotType == null)
        {
            return null;
        }
        Plot plot = new Plot(plotType, this);
        
        // Load the node
        plot.loadNode();
        rootNode.attachChild(plot.node);
        
        // Keep track of it
        plots.add(plot);
        
        // Return it
        return plot;
    }
    
    public <SomeEntity extends Entity> SomeEntity spawnEntity(Class<SomeEntity> entityType, Vector2f vector2f)
    {
        // Create the thing
        SomeEntity entity;
        try
        {
            entity = entityType.getConstructor().newInstance();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            
            return null;
        }
        entity.assertWorld(this);
        
        // Load the node
        entity.loadNode();
        rootNode.attachChild(entity.node);
        
        // Move it into position
        entity.teleport(vector2f);
        
        // Keep track of it
        entities.add(entity);
        
        // Return it
        return entity;
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

    public void destroyEntity(Entity aThis)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void tick(float tpf)
    {
        for(Entity entity : entities)
        {
            entity.tick(tpf);
        }
    }


}
