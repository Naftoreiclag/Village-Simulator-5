/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.util.json.AbstractJSONThingy;
import naftoreiclag.villagefive.util.json.JSONUtil;
import naftoreiclag.villagefive.util.math.Polygon;
import naftoreiclag.villagefive.util.math.Vec2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Blueprint extends AbstractJSONThingy
{
    // Name, duh
    public String name;
    
    public int width;
    public int height;
    
    public List<Room> rooms = new ArrayList<Room>();
    public List<Vert> verts = new ArrayList<Vert>();
    public List<Door> doors = new ArrayList<Door>();
    
    public Blueprint()
    {
        
    }
    
    public Blueprint(JSONObject obj)
    {
        name = (String) obj.get("name");
        
        width = ((Long) obj.get("width")).intValue();
        height = ((Long) obj.get("height")).intValue();
        
        verts = JSONUtil.readList(obj, "vertexes", Vert.class);
        doors = JSONUtil.readList(obj, "doors", Door.class);
        rooms = JSONUtil.readList(obj, "rooms", Room.class);
    }

    @Override
    public void populateJson(JSONObject obj)
    {
        obj.put("name", name);
        
        obj.put("width", width);
        obj.put("height", height);
        
        obj.put("vertexes", JSONUtil.encodeList(verts));
        obj.put("doors", JSONUtil.encodeList(doors));
        obj.put("rooms", JSONUtil.encodeList(rooms));
    }
    
    public class Vert extends AbstractJSONThingy
    {
        public Vec2 loc = new Vec2();

        public Vert()
        {
            
        }
        
        public Vert(JSONObject data)
        {
            this.loc = new Vec2((JSONObject) data.get("location"));
        }

        @Override
        public void populateJson(JSONObject obj)
        {
            obj.put("location", loc);
        }
    }
    public class Door extends AbstractJSONThingy
    {
        public WallLoc loc;
        
        public Door()
        {
            loc = new WallLoc();
        }
        
        public Door(JSONObject data)
        {
            loc = new WallLoc((JSONObject) data.get("location"));
        }

        @Override
        public void populateJson(JSONObject obj)
        {
            obj.put("location", loc);
        }
    }
    public class WallLoc extends AbstractJSONThingy
    {
        public Vert a;
        public Vert b;
        public double x;

        private WallLoc()
        {
            a = null;
            b = null;
            x = 0;
        }
        
        public WallLoc(JSONObject data)
        {
            int ai = ((Long) data.get("a")).intValue();
            int bi = ((Long) data.get("b")).intValue();
            
            if(ai < 0 || bi < 0)
            {
                a = verts.get(0);
                b = verts.get(0);
            }
            else
            {
                a = verts.get(ai);
                b = verts.get(bi);
            }
            x = (Double) data.get("x");
            
        }

        public void populateJson(JSONObject obj)
        {
            if(a == null || b == null)
            {
                obj.put("a", -1);
                obj.put("b", -1);
            }
            else
            {
                obj.put("a", a.getJsonIndex());
                obj.put("b", b.getJsonIndex());
            }
            
            obj.put("x", x);
        }
    }
    public class Room extends AbstractJSONThingy
    {
        public WallType wallType;
        public List<Vert> vertPntrs = new ArrayList<Vert>();
        
        public String name;
        
        public Room()
        {
            
        }
        
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

        public void populateJson(JSONObject obj)
        {
            obj.put("name", name);
            obj.put("vertexPointers", JSONUtil.encodePntrList(vertPntrs));
        }
    }
}
