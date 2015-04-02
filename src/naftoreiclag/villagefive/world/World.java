/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import naftoreiclag.villagefive.PlayerOfflineData;
import naftoreiclag.villagefive.util.math.OreDict;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.plot.Plot;
import naftoreiclag.villagefive.world.chunk.Chunk;
import naftoreiclag.villagefive.world.entity.EntityRegistry;
import naftoreiclag.villagefive.world.rays.ControllerFilter;
import naftoreiclag.villagefive.world.rays.DebugRayRenderer;
import naftoreiclag.villagefive.world.rays.InteractRay;
import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.collision.Bounds;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

// Thingy that handles pretty much anything that can be considered a part of the world
public class World implements JSONAware
{
    // Player-given name for the world. For use-friendliness only.
    String name = "swagland";
    
    // Size in chunks
    public int width = 20;
    public int height = 20;
    
    // Chunk data
    public Chunk[][] chunks = new Chunk[width][height];
    
    // Rendering nonscense
    public Node rootNode;
    private Node entityRoot;
    private Node chunkRoot;
    
    // Physics calculations
    public PhysWorld physics = new PhysWorld();
    public Bounds physBounds = new AxisAlignedBounds(width * Chunk.width * 2, height * Chunk.height * 2);
    
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Plot> plots = new ArrayList<Plot>();
    public List<PlayerOfflineData> players = new ArrayList<PlayerOfflineData>();
    
    public World(Node theRootNode, AssetManager assetManager)
    {
        // Establish some holding nodes.
        this.rootNode = new Node();
        theRootNode.attachChild(this.rootNode);
        this.entityRoot = new Node();
        this.rootNode.attachChild(this.entityRoot);
        this.chunkRoot = new Node();
        this.rootNode.attachChild(this.chunkRoot);
        
        // Entities should cast but not receive shadows.
        this.entityRoot.setShadowMode(RenderQueue.ShadowMode.Cast);
        
        // Chunks should receive but not cast shadows.
        this.chunkRoot.setShadowMode(RenderQueue.ShadowMode.Receive);
        
        physics.setBounds(physBounds);
        physics.setGravity(Vec2.ZERO_DYN4J);
        
        physics.addListener(new InteractRay.RaycastInteractFilter());
        physics.addListener(new ControllerFilter());
        
        if(showPhysDebug) {
            physics.addListener(new DebugRayRenderer());
            DebugRayRenderer.rootNode = this.rootNode;
        }
        
        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                Chunk chunk  = new Chunk();
                
                chunk.x = x;
                chunk.z = z;
                
                chunk.createNode();
                chunkRoot.attachChild(chunk.getNode());
                
                chunks[x][z] = chunk;
            }
        }
    }

    @Override
    public String toJSONString()
    {
        JSONObject json = new JSONObject();
        
        json.put("name", name);
        json.put("entities", entities);
        json.put("plots", plots);
        
        return json.toJSONString();
    }

    public boolean showPhysDebug = false;
    public Node last = null;

    public void tick(float tpf)
    {
        physics.update(tpf);
        
        if(showPhysDebug)
        {
            if(last != null)
            {
                last.removeFromParent();
            }
            
            last = physics.debugShow();
            last.setLocalTranslation(0, 0.1f, 0);
            rootNode.attachChild(last);
        }
        
        
        for(Entity entity : entities)
        {
            entity.tick(tpf);
        }
        
    }
    
    private Plot addPlot(Plot plot)
    {
        plot.assertWorld(this);
        
        // Load the node
        plot.createNode();
        rootNode.attachChild(plot.getNode());
        
        // 
        plot.spawnAttachedEntities(this);
        
        // Body
        plot.createBody(this.physics);
        
        // Keep track of it
        plots.add(plot);
        
        //
        plot.updateLoc();
        
        // Return it
        return plot;
    }
    
    private void addEntity(Entity entity)
    {
        entity.assertWorld(this);
        
        // Load the node
        entity.createNode();
        entityRoot.attachChild(entity.getNode());
        
        // Body
        entity.createBody(this.physics);
        
        // Keep track of it
        entities.add(entity);
    }
    
    public Entity spawnEntity(String entityId)
    {
        Entity entity = EntityRegistry.newInstance(entityId);
        
        entity.assertWorld(this);
        
        // Load the node
        entity.createNode();
        entityRoot.attachChild(entity.getNode());
        
        // Body
        entity.createBody(this.physics);
        
        // Keep track of it
        entities.add(entity);
        
        return entity;
    }
    public Entity getEntity(String entityId) {
        for(Entity entity : entities) {
            if(entity.getEntityId().equals(entityId)) {
                return entity;
            }
        }
        return null;
    }

    
    public void updateChunkLODs(Vec2 loc2)
    {
        Vec2 chunkLoc = loc2.grid(Chunk.width, Chunk.height);
        
        int cx = chunkLoc.getXI();
        int cy = chunkLoc.getYI();
        
        int lodHighRad = 2;
        int lodMidRad = 4;
        
        for(int x = 0; x < width; ++ x)
        {
            for(int y = 0; y < height; ++ y)
            {
                if(Math.abs(x - cx) < lodHighRad && Math.abs(y - cy) < lodHighRad)
                {
                    this.chunks[x][y].setLOD(Chunk.HIGH_LOD);
                    continue;
                }
                if(Math.abs(x - cx) < lodMidRad && Math.abs(y - cy) < lodMidRad)
                {
                    this.chunks[x][y].setLOD(Chunk.MID_LOD);
                    continue;
                }
                this.chunks[x][y].setLOD(Chunk.LOW_LOD);
            }
        }
        
        
    }
    
    public Chunk getChunk(int x, int y)
    {
        if(x < 0 || x >= width || y < 0 || y >= height)
        {
            return null;
        }
        else
        {
            return this.chunks[x][y];
        }
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
    
    public void removeEntity(Entity ent)
    {
        this.entities.remove(ent);
        
        if(ent.getNode() != null)
        {
            ent.getNode().removeFromParent();
        }
        if(ent.getBody() != null)
        {
            this.physics.removeBody(ent.getBody());
        }
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
            this.addPlot(plot);
        }
        
        for(Object obj : entityList)
        {
            JSONObject entityData = (JSONObject) obj;
            
            String entityId = (String) entityData.get("instanceof");
            Vec2 loc = new Vec2((JSONObject) entityData.get("location"));
            
            Entity entity = EntityRegistry.newInstance(entityId);
            this.addEntity(entity);
            entity.setLocation(loc);
            
        }
    }

    public void materializeEntity(Entity entity) {
        entity.assertWorld(this);
        
        // Load the node
        entity.createNode();
        entityRoot.attachChild(entity.getNode());
        
        // Body
        entity.createBody(this.physics);
        
        // Keep track of it
        entities.add(entity);
    }

}
