/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.util.json.AbstractJSONThingy;
import naftoreiclag.villagefive.util.json.JSONThingy;
import naftoreiclag.villagefive.util.math.Polygon;
import naftoreiclag.villagefive.util.math.Vec2;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class BlueprintBuilder extends AbstractJSONThingy
{
    // Used only for preserving object references when saving/loading with JSON.
    // Specifically, it refers to the index of this object in a JSONArray and therefore does not require saving.
    public long jsonIndex;
    
    // Name, duh
    public String name;
    
    public List<Room> rooms = new ArrayList<Room>();
    public List<Vert> verts = new ArrayList<Vert>();
    public List<Door> doors = new ArrayList<Door>();

    public String toJSONString()
    {
        JSONObject obj = new JSONObject();
        
        obj.put("name", name);
        
        /*
        JSONArray jsonVerts = new JSONArray();
        for(int i = 0; i < verts.size(); ++ i)
        {
            Vert vert = verts.get(i);
            vert.jsonIndex = indexKeeper;
            jsonVerts.add(vert);
        }
        obj.put("vertexes", JSONThingy.something(verts));
        */
        obj.put("vertexes", JSONThingy.convertToArray(verts));
        
        JSONArray jsonDoors = new JSONArray();
        for(int i = 0; i < verts.size(); ++ i)
        {
            Vert vert = verts.get(i);
            vert.jsonIndex = indexKeeper;
            jsonVerts.add(vert);
        }
        obj.put("vertexes", jsonVerts);
        
        JSONArray jsonRooms = new JSONArray();
        for(int i = 0; i < rooms.size(); ++ i)
        {
            Room room = rooms.get(i);
            room.jsonIndex = indexKeeper;
            jsonRooms.add(jsonRooms);
        }
        obj.put("rooms", jsonRooms);
        
        
        return obj.toJSONString();
    }
    
    public BlueprintBuilder(JSONObject obj)
    {
        name = (String) obj.get("name");
        
        JSONArray jsonVerts = (JSONArray) obj.get("vertexes");
        for(int i = 0; i < jsonVerts.size(); ++ i)
        {
            Vert vert = new Vert((JSONObject) jsonVerts.get(i));
            verts.add(vert);
        }
        
        JSONArray jsonDoors = (JSONArray) obj.get("doors");
        for(int i = 0; i < jsonDoors.size(); ++ i)
        {
            Door door = new Door((JSONObject) jsonDoors.get(i));
            doors.add(door);
        }
        
        JSONArray jsonRooms = (JSONArray) obj.get("rooms");
        for(int i = 0; i < jsonRooms.size(); ++ i)
        {
            Room room = new Room((JSONObject) jsonRooms.get(i));
            rooms.add(room);
        }
    }

    public void dopeJsonObject(JSONObject json)
    {
    }
    
    public class Vert extends AbstractJSONThingy
    {
        public long jsonIndex;
    
        public Vec2 loc;

        public Vert(JSONObject data)
        {
            this.loc = new Vec2((JSONObject) data.get("location"));
        }

        public String toJSONString()
        {
            JSONObject obj = new JSONObject();
            
            obj.put("location", loc);
            
            return obj.toJSONString();
        }
    }
    public class Door extends AbstractJSONThingy
    {
        public long jsonIndex;
        public WallLoc loc;
        
        public Door(JSONObject data)
        {
            loc = new WallLoc((JSONObject) data.get("location"));
        }

        public String toJSONString()
        {
            JSONObject obj = new JSONObject();
            
            obj.put("location", loc);
            
            return obj.toJSONString();
        }
    }
    public class WallLoc extends AbstractJSONThingy
    {
        public Vert a;
        public Vert b;
        public double x;
        
        public WallLoc(JSONObject data)
        {
            a = verts.get(((Long) data.get("a")).intValue());
            b = verts.get(((Long) data.get("b")).intValue());
            x = (Double) data.get("x");
        }

        public String toJSONString()
        {
            JSONObject obj = new JSONObject();
            
            obj.put("a", a.jsonIndex);
            obj.put("b", b.jsonIndex);
            obj.put("x", x);
            
            return obj.toJSONString();
        }
    }
    public class Room extends AbstractJSONThingy
    {
        public long jsonIndex;
        
        public WallType wallType;
        public List<Vert> vertPntrs;
        
        public String name;
        
        public Polygon toPolygon()
        {
            // Create a new polygon to represent it
            Polygon polygon = new Polygon();
            
            // Copy over the vertex data
            for(int i = 0; i < vertPntrs.size(); ++ i)
            {
                Vert vert = vertPntrs.get(i);
                polygon.vecs.add(vert.loc);
            }
            
            return polygon;
        }

        private Room(JSONObject data)
        {
            name = data.get("name").toString();
            
            JSONArray jsonVertPntrs = (JSONArray) data.get("vertexPointers");
            for(int i = 0; i < jsonVertPntrs.size(); ++ i)
            {
                vertPntrs.add(verts.get(((Long) jsonVertPntrs.get(i)).intValue()));
            }
        }

        public String toJSONString()
        {
            JSONObject obj = new JSONObject();
            
            obj.put("name", name);
            JSONArray jsonVertPntrs = new JSONArray();
            for(int i = 0; i < vertPntrs.size(); ++ i)
            {
                jsonVertPntrs.add(vertPntrs.get(i).jsonIndex);
            }
            obj.put("vertexPointers", jsonVertPntrs);
            
            return obj.toJSONString();
        }
    }
}
