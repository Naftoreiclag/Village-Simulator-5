/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.plot;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import naftoreiclag.villagefive.world.World;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.util.math.OreDict;
import naftoreiclag.villagefive.util.serializable.PlotSerial;
import naftoreiclag.villagefive.util.math.Polygon;
import naftoreiclag.villagefive.util.scenegraph.ModelBuilder;
import naftoreiclag.villagefive.world.Mundane;
import org.dyn4j.dynamics.Body;

public class Plot extends Mundane
{
    public PlotSerial data;
    protected Node node;
    protected Body body;
    
    public Map<Integer, Node> wholeRooms = new HashMap<Integer, Node>();
    
    public Plot(PlotSerial data)
    {
        this.data = data;
    }
    
    @Override
    public void createNode()
    {
        this.node = new Node();
        
        // For each room
        for(PlotSerial.Face room : data.getFaces())
        {
            Polygon polygon = OreDict.roomToPoly(data, room);
            
            Node roomNode = new Node();
            
            Mesh floorM = polygon.genFloor(0.2f, 7f, 3f, 3f);
            Geometry floorG = new Geometry("Floor", floorM);
            floorG.setMaterial(Main.mat_debug_bricks);
            roomNode.attachChild(floorG);
            
            Mesh outM = polygon.genOutsideWall(0.2f, 7f, 3f, 3f);
            Geometry outG = new Geometry("Outside", outM);
            outG.setMaterial(Main.mat_debug_bricks);
            roomNode.attachChild(outG);
            
            Mesh inM = polygon.genInsideWall(0.2f, 7f, 3f, 3f);
            Geometry inG = new Geometry("Inside", inM);
            inG.setMaterial(Main.mat_debug_bricks);
            roomNode.attachChild(inG);
            
            Mesh rM = polygon.genRoof(3f, 3f);
            Geometry rG = new Geometry("Roof", rM);
            rG.setMaterial(Main.mat_debug_bricks);
            roomNode.attachChild(rG);
            
            wholeRooms.put(room.getId(), roomNode);
            System.out.println("loaded: " + room.getId());
            
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
        if(data.getFaces().length == 0)
        {
            this.body = null;
            return;
        }
        
        Body newBod = new Body();
        
        // For each room
        for(PlotSerial.Face room : data.getFaces())
        {
            Polygon polygon = OreDict.roomToPoly(data, room);
            
            polygon.makeBody(newBod, 0.4);
        }
        
        
        
        this.body = newBod;
    }

    @Override
    public Body getBody()
    {
        return this.body;
    }

}
