/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.util.math.OreDict;
import naftoreiclag.villagefive.util.scenegraph.Polygon;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.plot.Plot;
import naftoreiclag.villagefive.util.serializable.PlotSerial;
import naftoreiclag.villagefive.util.serializable.PlotSerial.Decal;
import naftoreiclag.villagefive.util.serializable.PlotSerial.Face;
import naftoreiclag.villagefive.util.serializable.PlotSerial.Vert;
import naftoreiclag.villagefive.world.chunk.Chunk;
import naftoreiclag.villagefive.world.entity.DoorEntity;
import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.collision.Bounds;

// Thingy that handles pretty much anything that can be considered a part of the world
public class World
{
    // Size in chunks
    public int width = 5;
    public int height = 5;
    
    public Chunk[][] chunks = new Chunk[width][height];
    
    public Node trueRootNode;
    public Node rootNode;
    public AssetManager assetManager;
    
    public PhysWorld physics = new PhysWorld();
    
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Plot> plots = new ArrayList<Plot>();
    
    public World(Node rootNode, AssetManager assetManager)
    {
        this.rootNode = new Node();
        this.trueRootNode = rootNode;
        this.trueRootNode.attachChild(this.rootNode);
        this.assetManager = assetManager;
        
        Bounds bounds = new AxisAlignedBounds(width * Chunk.width, height * Chunk.height);
        physics.setBounds(bounds);
        
        for(int x = 0; x < width; ++ x)
        {
            for(int z = 0; z < height; ++ z)
            {
                Chunk chunk  = new Chunk();
                
                chunk.x = x;
                chunk.z = z;
                
                System.out.println(x + ", " + z);
                chunk.loadNode();
                this.rootNode.attachChild(chunk.getNode());
                
                chunks[x][z] = chunk;
            }
        }
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
        plot.setLocation(new Vector2f((float) plotType.getX(), (float) plotType.getZ()));
        rootNode.attachChild(plot.getNode());
        
        for(Decal d : plotType.getDecals())
        {
            DoorEntity doorEnt = this.spawnEntity(DoorEntity.class);
            
            Vert a = plotType.getVerts()[d.getVertA()];
            Vert b = plotType.getVerts()[d.getVertB()];
            
            Vector2f A = new Vector2f((float) a.getX(), (float) a.getZ());
            Vector2f B = new Vector2f((float) b.getX(), (float) b.getZ());
            
            Vector2f AB = B.subtract(A).normalizeLocal().multLocal((float) d.getDistance());
            
            float angle = AB.getAngle();
            
            System.out.println("angle = " + angle);
            
            
            doorEnt.setLocation(A.add(AB));
            doorEnt.setRotation(-angle); // what
            
            plot.getNode().attachChild(doorEnt.getNode());
        }
        
        // Keep track of it
        plots.add(plot);
        
        // Return it
        return plot;
    }
    
    // Are you in room
    public boolean insideRoom(Vector2f loc2)
    {
        Vector3f loc3 = OreDict.Vec2ToVec3(loc2);
        
        for(Plot plot : plots)
        {
            Vector3f relPos3 = plot.getNode().worldToLocal(loc3, null);
            Vector2f relPos2 = OreDict.vec3ToVec2(relPos3);
            
            for(Face f : plot.data.getFaces())
            {
                Polygon p = OreDict.roomToPoly(plot.data, f);
                
                if(p.inside(relPos2))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    
    // Something
    public Spatial hiddenRoom;
    public Node hiddenRoomParen;
    public boolean something(Vector2f loc2)
    {
        
        if(hiddenRoom != null)
        {
            hiddenRoomParen.attachChild(hiddenRoom);
        }
        
        Vector3f loc3 = OreDict.Vec2ToVec3(loc2);
        
        for(Plot plot : plots)
        {
            Vector3f relPos3 = plot.getNode().worldToLocal(loc3, null);
            Vector2f relPos2 = OreDict.vec3ToVec2(relPos3);
            
            for(Face f : plot.data.getFaces())
            {
                Polygon p = OreDict.roomToPoly(plot.data, f);
                
                if(p.inside(relPos2))
                {
                    Node n = plot.wholeRooms.get(f.getId());
                    
                    Spatial o = n.getChild("Outside");
                    o.removeFromParent();
                    
                        
                    hiddenRoom = o;
                    hiddenRoomParen = n;
                    
                }
            }
        }


        
        return false;
    }
    
    public <SomeEntity extends Entity> SomeEntity spawnEntity(Class<SomeEntity> entityType)
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
        rootNode.attachChild(entity.getNode());
        
        // Keep track of it
        entities.add(entity);
        
        // Return it
        return entity;
    }
    
    public <SomeEntity extends Entity> SomeEntity spawnEntity(Class<SomeEntity> entityType, Vector2f vector2f)
    {
        // Create the thing
        SomeEntity entity = spawnEntity(entityType);
        
        if(entity == null)
        {
            return null;
        }
        
        // Move it into position
        entity.setLocation(vector2f);
        
        // Return it
        return entity;
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
        
        physics.update(tpf);
    }


}
