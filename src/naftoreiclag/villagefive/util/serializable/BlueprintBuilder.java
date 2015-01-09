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
import naftoreiclag.villagefive.util.json.JSONUtil;
import naftoreiclag.villagefive.util.math.Polygon;
import naftoreiclag.villagefive.util.math.Vec2;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class BlueprintBuilder extends AbstractJSONThingy
{
    // Name, duh
    public String name;
    
    public List<Room> rooms = new ArrayList<Room>();
    public List<Vert> verts = new ArrayList<Vert>();
    public List<Door> doors = new ArrayList<Door>();
    
    public BlueprintBuilder(JSONObject obj)
    {
        name = (String) obj.get("name");
        
        verts = JSONUtil.readList(obj, "vertexes", Vert.class);
        doors = JSONUtil.readList(obj, "doors", Door.class);
        rooms = JSONUtil.readList(obj, "rooms", Room.class);
    }

    @Override
    public void dopeJsonObject(JSONObject obj)
    {
        obj.put("name", name);
        
        obj.put("vertexes", JSONUtil.encodeList(verts));
        obj.put("doors", JSONUtil.encodeList(doors));
        obj.put("rooms", JSONUtil.encodeList(rooms));
    }
    
    public class Vert extends AbstractJSONThingy
    {
        public Vec2 loc;

        public Vert(JSONObject data)
        {
            this.loc = new Vec2((JSONObject) data.get("location"));
        }


        public void dopeJsonObject(JSONObject obj)
        {
            obj.put("location", loc);
        }
    }
    public class Door extends AbstractJSONThingy
    {
        public WallLoc loc;
        
        public Door(JSONObject data)
        {
            loc = new WallLoc((JSONObject) data.get("location"));
        }

        public void dopeJsonObject(JSONObject obj)
        {
            obj.put("location", loc);
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

        public void dopeJsonObject(JSONObject obj)
        {
            obj.put("a", a.getJsonIndex());
            obj.put("b", b.getJsonIndex());
            obj.put("x", x);
        }
    }
    public class Room extends AbstractJSONThingy
    {
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

        public void dopeJsonObject(JSONObject obj)
        {
            obj.put("name", name);
            obj.put("vertexPointers", JSONUtil.encodePntrList(vertPntrs));
        }
    }
}
