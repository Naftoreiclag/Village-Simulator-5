/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;


// Serializable format
public class PlotSerial implements JSONAware
{
    private long id = 1337L;
    private String name = "Bakery";
    
    private double x, z;
    private double angle;
    
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

    @Override
    public String toJSONString()
    {
        JSONObject obj = new JSONObject();
        
        obj.put("id", id);
        obj.put("name", name);
        obj.put("vertexes", Arrays.asList(verts));
        obj.put("decals", Arrays.asList(decals));
        obj.put("faces", Arrays.asList(faces));
        
        return obj.toJSONString();
    }
    
    
    // "Flags"
    public static class Vert implements JSONAware
    {
        private double x;
        private double z;
        private int id;

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

            obj.put("id", id);
            obj.put("x", x);
            obj.put("z", z);

            return obj.toJSONString();
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

            obj.put("id", id);
            obj.put("vertA", vertA);
            obj.put("vertB", vertB);
            obj.put("x", distance);
            obj.put("width", width);

            return obj.toJSONString();
        }
    }
    
    // "Rooms"
    // Store the edges in counter-clockwise order
    public static class Face implements JSONAware
    {
        private int[] verts;
        private int id;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int[] getVerts() { return verts; }
        public void setVerts(int[] edges) { this.verts = edges; }

        @Override
        public String toJSONString()
        {
            JSONObject obj = new JSONObject();

            obj.put("id", id);
            
            JSONArray jsonVerts = new JSONArray();
            for(int i = 0; i < verts.length; ++ i)
            {
                jsonVerts.add(verts[i]);
            }
            obj.put("loop", jsonVerts);

            return obj.toJSONString();
        }
    }

    public double getX() { return x; }
    public double getZ() { return z; }
    public double getAngle() { return angle; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public void setX(double x) { this.x = x; }
    public void setZ(double z) { this.z = z; }
    public void setAngle(double angle) { this.angle = angle; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
}
