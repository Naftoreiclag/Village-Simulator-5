/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import naftoreiclag.villagefive.util.math.Vec2;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;


// Serializable format
public class Blueprint implements JSONAware
{
    // Subclass ids are not saved because their index in the containing array determines that.

    private long id = 1337L;
    private String name = "Bakery";
    
    private int width, height;
    
    private Vert[] verts;
    private Decal[] decals;
    private Face[] faces;

    public Vert[] getVerts() { return verts; }
    public Decal[] getDecals() { return decals; }
    public Face[] getFaces() { return faces; }

    public void setVerts(Vert[] verts) { this.verts = verts; }
    public void setDecals(Decal[] decals) { this.decals = decals; }
    public void setFaces(Face[] faces) { this.faces = faces; }

    public Blueprint()
    {
        verts = new Vert[0];
        decals = new Decal[0];
        faces = new Face[0];
    }
    
    @Override
    public String toJSONString()
    {
        JSONObject obj = new JSONObject();
        obj.put("id", this.getId());
        obj.put("name", this.getName());
        obj.put("vertexes", Arrays.asList(verts));
        obj.put("decals", Arrays.asList(decals));
        obj.put("faces", Arrays.asList(faces));
        return obj.toJSONString();
    }
    
    public Blueprint(JSONObject data)
    {
        id = (Long) data.get("id");
        name = (String) data.get("name");
        JSONArray rawVerts = (JSONArray) data.get("vertexes");
        JSONArray rawDecals = (JSONArray) data.get("decals");
        JSONArray rawFaces = (JSONArray) data.get("faces");
        
        verts = new Vert[rawVerts.size()];
        for(int i = 0; i < rawVerts.size(); ++ i)
        {
            JSONObject vert = (JSONObject) rawVerts.get(i);
            
            verts[i] = new Vert(vert);
            verts[i].setId(i);
        }
        
        decals = new Decal[rawDecals.size()];
        for(int i = 0; i < rawDecals.size(); ++ i)
        {
            JSONObject decal = (JSONObject) rawDecals.get(i);
            
            decals[i] = new Decal(decal);
            decals[i].setId(i);
        }
        
        faces = new Face[rawFaces.size()];
        for(int i = 0; i < rawFaces.size(); ++ i)
        {
            JSONObject face = (JSONObject) rawFaces.get(i);
            
            faces[i] = new Face(face);
            faces[i].setId(i);
        }
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    
    // "Flags"
    public static class Vert implements JSONAware
    {
        private double x;
        private double z;
        private int id;
        
        public Vert() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public double getX() { return x; }
        public double getZ() { return z; }
        public void setX(double x) { this.x = x; }
        public void setZ(double z) { this.z = z; }

        @Override
        public String toJSONString()
        {
            JSONObject obj = new JSONObject();

            obj.put("x", x);
            obj.put("z", z);

            return obj.toJSONString();
        }
        
        private Vert(JSONObject vert)
        {
            this.x = (Double) vert.get("x");
            this.z = (Double) vert.get("z");
        }
    }
    
    // "Doors"
    // Store decal data (doors, windows, graffiti)
    public static class Decal implements JSONAware
    {
        private int vertA;
        private int vertB;
        private double distance;
        private int id;
        
        // temp
        public double width;
        
        public Decal() {}
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getVertA() { return vertA; }
        public int getVertB() { return vertB; }
        public void setVertA(int id) { this.vertA = id; }
        public void setVertB(int id) { this.vertB = id; }

        public double getDistance() { return distance; }
        public void setDistance(double distance) { this.distance = distance; }
    
        @Override
        public String toJSONString()
        {
            JSONObject obj = new JSONObject();

            obj.put("vertA", vertA);
            obj.put("vertB", vertB);
            obj.put("x", distance);
            obj.put("width", width);

            return obj.toJSONString();
        }
        
        private Decal(JSONObject data)
        {
            vertA = ((Long) data.get("vertA")).intValue();
            vertB = ((Long) data.get("vertB")).intValue();
            distance = (Double) data.get("x");
            width = (Double) data.get("width");
        }
    }
    
    // "Rooms"
    // Store the edges in counter-clockwise order
    public static class Face implements JSONAware
    {
        private int[] verts;
        private int id;
        
        public Face() {}
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int[] getVerts() { return verts; }
        public void setVerts(int[] edges) { this.verts = edges; }

        @Override
        public String toJSONString()
        {
            JSONObject obj = new JSONObject();
            
            JSONArray jsonVerts = new JSONArray();
            for(int i = 0; i < verts.length; ++ i)
            {
                jsonVerts.add(verts[i]);
            }
            obj.put("loop", jsonVerts);

            return obj.toJSONString();
        }
        private Face(JSONObject data)
        {
            JSONArray loop = (JSONArray) data.get("loop");
            
            verts = new int[loop.size()];
            for(int i = 0; i < loop.size(); ++ i)
            {
                verts[i] = ((Long) loop.get(i)).intValue();
            }
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
}
