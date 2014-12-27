/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

// TODO: add layers
// Serializable format
public class Plot
{
    private double x, z;
    private double angle;
    
    private int width, height;
    
    private Vertex[] verticies;
    private Edge[] edges;
    private Face[] faces;

    public Vertex[] getVerticies() { return verticies; }
    public Edge[] getEdges() { return edges; }
    public Face[] getFaces() { return faces; }

    public void setVerticies(Vertex[] verticies) { this.verticies = verticies; }
    public void setEdges(Edge[] edges) { this.edges = edges; }
    public void setFaces(Face[] faces) { this.faces = faces; }
    
    // "Flags"
    public static class Vertex
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
    }
    
    // "Walls"
    // Store decal data (doors, windows, graffiti)
    public static class Edge
    {
        private int vertA;
        private int vertB;
        private int id;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getVertA() { return vertA; }
        public int getVertB() { return vertB; }
        public void setVertA(int id) { this.vertA = id; }
        public void setVertB(int id) { this.vertB = id; }
    }
    
    // "Rooms"
    // Store the edges in counter-clockwise order
    public static class Face
    {
        private int[] vertexes;
        private int id;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int[] getVertexes() { return vertexes; }
        public void setVertexes(int[] edges) { this.vertexes = edges; }

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
