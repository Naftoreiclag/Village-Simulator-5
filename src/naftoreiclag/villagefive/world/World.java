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
        Plot plot = new Plot(plotType, this);
        plot.loadNode();
        
        rootNode.attachChild(plot.node);
        
        plots.add(plot);
        
        return plot;
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
        e.teleport(vector2f);
        
        entities.add(e);
        
        return e;
    }
    
    public Node loadNode(String modelName)
    {
        if(modelName == null)
        {
            Node body = new Node();
            rootNode.attachChild(body);

            return body;
        }
        
        Node body = (Node) assetManager.loadModel(modelName);
        
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
