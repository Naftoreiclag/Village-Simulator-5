/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import naftoreiclag.villagefive.util.math.OreDict;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.plot.Plot;
import naftoreiclag.villagefive.world.chunk.Chunk;
import naftoreiclag.villagefive.world.entity.EntityRegistry;
import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.collision.Bounds;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

// Thingy that handles pretty much anything that can be considered a part of the world
public class World implements JSONAware
{
    // Size in chunks
    public int width = 5;
    public int height = 5;
    
    public Chunk[][] chunks = new Chunk[width][height];
    
    public Node trueRootNode;
    public Node rootNode;
    public AssetManager assetManager;
    
    public PhysWorld physWorld = new PhysWorld();
    public Bounds physBounds = new AxisAlignedBounds(width * Chunk.width * 2, height * Chunk.height * 2);
    
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Plot> plots = new ArrayList<Plot>();
    public List<Resident> residents = new ArrayList<Resident>();
    
    public World(Node rootNode, AssetManager assetManager)
    {
        this.rootNode = new Node();
        this.trueRootNode = rootNode;
        this.trueRootNode.attachChild(this.rootNode);
        this.assetManager = assetManager;
        
        physWorld.setBounds(physBounds);
        physWorld.setGravity(Vec2.ZERO_DYN4J);
        
        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                Chunk chunk  = new Chunk();
                
                chunk.x = x;
                chunk.z = z;
                
                chunk.createNode();
                this.rootNode.attachChild(chunk.getNode());
                
                chunks[x][z] = chunk;
            }
        }
    }
    
    public void materializeEnvironment()
    {
        
    }

    public boolean showPhysDebug = false;
    public Node last = null;

    public void tick(float tpf)
    {
        physWorld.update(tpf);
        
        if(showPhysDebug)
        {
            if(last != null)
            {
                last.removeFromParent();
            }
            
            last = physWorld.debugShow();
            last.setLocalTranslation(0, 0.1f, 0);
            rootNode.attachChild(last);
        }
        
        
        for(Entity entity : entities)
        {
            entity.tick(tpf);
        }
        
    }
    
    public Plot materializePlot(Plot plot)
    {
        plot.assertWorld(this);
        
        // Load the node
        plot.createNode();
        rootNode.attachChild(plot.getNode());
        
        // 
        plot.spawnAttachedEntities(this);
        
        
        // Body
        plot.createBody();
        if(plot.getBody() != null) { physWorld.addBody(plot.getBody()); }
        
        // Keep track of it
        plots.add(plot);
        
        // Return it
        return plot;
    }
    
    public void materializeEntity(Entity entity)
    {
        entity.assertWorld(this);
        
        // Load the node
        entity.createNode();
        rootNode.attachChild(entity.getNode());
        
        // Body
        entity.createBody();
        if(entity.getBody() != null) { physWorld.addBody(entity.getBody()); }
        
        // Keep track of it
        entities.add(entity);
    }

    
    
    // Are you in room
    public boolean insideRoom(Vec2 loc2)
    {
        /*
        Vector3f loc3 = OreDict.Vec2ToVec3(loc2);
        
        for(Plot plot : plots)
        {
            Vector3f relPos3 = plot.getNode().worldToLocal(loc3, null);
            Vec2 relPos2 = OreDict.vec3ToVec2(relPos3);
            
            for(Face f : plot.blueprint.getFaces())
            {
                Polygon p = OreDict.roomToPoly(plot.blueprint, f);
                
                if(p.inside(relPos2))
                {
                    return true;
                }
            }
        }
        */
        
        return false;
    }
    
    
    // Something
    public Spatial hiddenRoom;
    public Node hiddenRoomParen;
    public boolean something(Vec2 loc2)
    {
        
        if(hiddenRoom != null)
        {
            hiddenRoomParen.attachChild(hiddenRoom);
        }
        
        Vector3f loc3 = OreDict.Vec2ToVec3(loc2);
        
        /*
        for(Plot plot : plots)
        {
            Vector3f relPos3 = plot.getNode().worldToLocal(loc3, null);
            Vec2 relPos2 = OreDict.vec3ToVec2(relPos3);
            
            for(Face f : plot.blueprint.getFaces())
            {
                Polygon p = OreDict.roomToPoly(plot.blueprint, f);
                
                if(p.inside(relPos2))
                {
                    Node n = plot.roomNodes.get(f.getId());
                    
                    Spatial o = n.getChild("Outside");
                    o.removeFromParent();
                    
                        
                    hiddenRoom = o;
                    hiddenRoomParen = n;
                    
                }
            }
        }
        */


        
        return false;
    }
    
    public void destroyEntity(Entity aThis)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toJSONString()
    {
        JSONObject json = new JSONObject();
        
        json.put("name", "swagland");
        json.put("entities", entities);
        json.put("plots", plots);
        
        return json.toJSONString();
    }

    public void spawnFromJson(JSONObject worldj)
    {
        JSONArray plotList = (JSONArray) worldj.get("plots");
        JSONArray entityList = (JSONArray) worldj.get("entities");
        
        // For the plots
        for(Object obj : plotList)
        {
            JSONObject plotData = (JSONObject) obj;
            Plot plot = new Plot(plotData);
            
            this.materializePlot(plot);
        }
        
        for(Object obj : entityList)
        {
            JSONObject entityData = (JSONObject) obj;
            
            Entity ddd = null;
            try
            {
                ddd = EntityRegistry.entities.get((String) entityData.get("instanceof")).newInstance();
            }
            catch(InstantiationException ex)
            {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IllegalAccessException ex)
            {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ddd.setLocation(new Vec2((JSONObject) entityData.get("location")));
            
            this.materializeEntity(ddd);
            
        }
    }

}
