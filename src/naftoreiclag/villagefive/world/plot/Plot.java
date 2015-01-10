/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.plot;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.OreDict;
import naftoreiclag.villagefive.util.math.Polygon;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.serializable.Blueprint;
import naftoreiclag.villagefive.util.serializable.Blueprint.Room;
import naftoreiclag.villagefive.world.Mundane;
import naftoreiclag.villagefive.world.World;
import naftoreiclag.villagefive.world.entity.DoorEntity;
import org.dyn4j.dynamics.Body;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

// Unlike the entities (which stores location), this stores its location separately as a Vec2
public class Plot extends Mundane implements JSONAware
{
    public Blueprint blueprint = new Blueprint();
    private Map<Integer, Node> roomNodes = new HashMap<Integer, Node>();
    protected Node node;
    protected Body body;
    
    public Vec2 loc = new Vec2();
    public double angle;
    
    public long owner;
    
    public Plot() {}
    
    public void setBlueprint(Blueprint data)
    {
        this.blueprint = data;
    }
    
    @Override
    public Vec2 getLocation()
    {
        return loc.clone();
    }
    @Override
    public void setLocation(Vec2 loc)
    {
        super.setLocation(loc);
        
        this.loc.set(loc);
    }
    
    @Override
    public void createNode()
    {
        this.node = new Node();
        
        // For each room
        for(Blueprint.Room room : blueprint.rooms)
        {
            Polygon polygon = room.toPolygon();
            Node roomNode = new Node();
            
            Mesh floorM = polygon.genFloor(0.2f, 7f, 3f, 3f);
            Geometry floorG = new Geometry("Floor", floorM);
            floorG.setMaterial(Main.mat_debug_bricks);
            roomNode.attachChild(floorG);

            Spatial outG = room.wallType.makeOutside(polygon);
            if(outG != null)
            {
                outG.setName("Outside");
                outG.setMaterial(Main.mat_debug_bricks);
                roomNode.attachChild(outG);
            }

            Spatial inG = room.wallType.makeInside(polygon);
            if(inG != null)
            {
                inG.setName("Inside");
                inG.setMaterial(Main.mat_debug_bricks);
                roomNode.attachChild(inG);
            }

            /*
            Mesh rM = polygon.genRoof(3f, 3f);
            Geometry rG = new Geometry("Roof", rM);
            rG.setMaterial(Main.mat_debug_bricks);
            roomNode.attachChild(rG);
            */

            node.attachChild(roomNode);
        }
    }
    
    @Override
    public Node getNode()
    {
        return node;
    }

    @Override
    public void createBody()
    {
        if(blueprint.rooms.isEmpty())
        {
            this.body = null;
            return;
        }

        Body newBod = new Body();

        // For each room
        for(Room room : blueprint.rooms)
        {
            Polygon polygon = room.toPolygon();

            polygon.makeBody(newBod, 0.4);
        }

        this.body = newBod;
    }

    @Override
    public Body getBody()
    {
        return this.body;
    }
    
    public Plot(JSONObject data)
    {
        this.loc = new Vec2((JSONObject) data.get("location"));
        this.blueprint = new Blueprint((JSONObject) data.get("blueprint"));
    }

    public String toJSONString()
    {
        JSONObject obj = new JSONObject();
        
        obj.put("location", loc);
        obj.put("blueprint", blueprint);
        
        return obj.toJSONString();
    }

    public void spawnAttachedEntities(World aThis)
    {
        // Spawn attached entities
        /*
        for(Blueprint.Decal d : blueprint.getDecals())
        {
            DoorEntity doorEnt = new DoorEntity();
            aThis.materializeEntity(doorEnt);

            Blueprint.Vert a = blueprint.getVerts()[d.getVertA()];
            Blueprint.Vert b = blueprint.getVerts()[d.getVertB()];

            Vec2 A = new Vec2((float) a.getX(), (float) a.getZ());
            Vec2 B = new Vec2((float) b.getX(), (float) b.getZ());

            Vec2 AB = B.subtract(A).normalizeLocal().multLocal((float) d.getDistance());

            Angle angle = AB.getAngle();

            System.out.println("angle = " + angle);

            doorEnt.setLocation(A.add(AB));
            doorEnt.setRotation(angle.inverse()); // what

            this.getNode().attachChild(doorEnt.getNode());
        }
        */
    }

    // Updates the body/node
    public void updateLoc()
    {
        this.setLocation(loc);
    }

}
