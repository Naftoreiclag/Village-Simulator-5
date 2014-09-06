/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;

public class Layer
{
    public float offX = 0;
    public float offZ = 0;
    
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private List<Texvex> texvexes = new ArrayList<Texvex>();
    private List<Line> lines = new ArrayList<Line>();
    
    public void addSeg(float x1, float z1, float x2, float z2)
    {
        this.addSeg(x1, z1, x2, z2, 1.0f, 0.0f);
    }
    
    public void addSeg(float x1, float z1, float x2, float z2, float tx1, float tx2)
    {
        /*
         * 1 ----- 2
         *     |
         *     v normal
         * 
         */
        
        x1 += offX;
        z1 += offZ;
        
        x2 += offX;
        z2 += offZ;
        
        if(x1 == x2 && z1 == z2)
        {
            System.out.println("Huh?");
            return;
        }
        
        float normX = z2 - z1;
        float normZ = x1 - x2;
        
        Vertex vertex1 = null;
        Vertex vertex2 = null;
        
        for(int i = 0; i < vertices.size(); ++ i)
        {
            Vertex comp = vertices.get(i);
            
            if(comp.x == x1 && comp.z == z1)
            {
                comp.normX += normX;
                comp.normZ += normZ;
                
                vertex1 = comp;
            }
            if(comp.x == x2 && comp.z == z2)
            {
                comp.normX += normX;
                comp.normZ += normZ;
                
                vertex2 = comp;
            }
            
            if(vertex1 != null && vertex2 != null)
            {
                break;
            }
        }
        
        if(vertex1 == null)
        {
            vertex1 = new Vertex(x1, z1, normX, normZ);
            vertices.add(vertex1);
        }
        if(vertex2 == null)
        {
            vertex2 = new Vertex(x2, z2, normX, normZ);
            vertices.add(vertex2);
        }
        
        int indice1 = -1;
        int indice2 = -1;
        
        for(int i = 0; i < texvexes.size(); ++ i)
        {
            Texvex comp = texvexes.get(i);
            
            if(comp.linkedData == vertex1 && comp.tx == tx1)
            {
                indice1 = i;
            }
            if(comp.linkedData == vertex2 && comp.tx == tx2)
            {
                indice2 = i;
            }
            
            if(indice1 != -1 && indice2 != -1)
            {
                break;
            }
        }
        
        if(indice1 == -1)
        {
            indice1 = texvexes.size();
            texvexes.add(new Texvex(tx1, vertex1));
        }
        if(indice2 == -1)
        {
            indice2 = texvexes.size();
            texvexes.add(new Texvex(tx2, vertex2));
        }
        
        lines.add(new Line(indice1, indice2));
    }
    
    public Mesh bake()
    {
        return bake(0.0f, 0.0f, 0.0f);
    }

    public Mesh bake(float xscale, float thickness, float zscale)
    {
        for(Vertex vert : vertices)
        {
            vert.normalize();
        }
        
        FloatBuffer v = BufferUtils.createFloatBuffer(texvexes.size() * 6);
        FloatBuffer n = BufferUtils.createFloatBuffer(texvexes.size() * 6);
        FloatBuffer t = BufferUtils.createFloatBuffer(texvexes.size() * 4);
        for(Texvex texv : texvexes)
        {
            Vertex vert = texv.linkedData;
            
            v.put(vert.x * xscale).put(thickness).put(vert.z * zscale);
            n.put(vert.normalX).put(0.0f).put(vert.normalZ);
            t.put(texv.tx).put(0.0f);
            
            v.put(vert.x * xscale).put(0.0f).put(vert.z * zscale);
            n.put(vert.normalX).put(0.0f).put(vert.normalZ);
            t.put(texv.tx).put(thickness);
        }
        IntBuffer i = BufferUtils.createIntBuffer(lines.size() * 6);
        for(Line line : lines)
        {
            // Note: I reversed the direction here to accommodate for JME.
            i.put(line.indice1).put(line.indice2 + 1).put(line.indice2);
            i.put(line.indice1).put(line.indice1 + 1).put(line.indice2 + 1);
        }
        
        System.out.println("Model Built!");
        System.out.println("Polys: " + lines.size() * 2);
        System.out.println("Vertices: " + (lines.size() * 4));
        System.out.println("Output Verts: " + vertices.size());
                
        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);
        mesh.setBuffer(VertexBuffer.Type.Normal,   3, n);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, t);
        mesh.setBuffer(VertexBuffer.Type.Index,    3, i);

        mesh.updateBound();
                
        return mesh;
    }
    
    // struct
    public static class Line
    {
        public int indice1;
        public int indice2;
        
        public Line(int indice1, int indice2)
        {
            this.indice1 = indice1 * 2;
            this.indice2 = indice2 * 2;
        }
    }
    
    // struct
    public static class Texvex
    {
        public float tx;
        
        public Vertex linkedData;
        
        public Texvex(float tx, Vertex linkedData)
        {
            this.tx = tx;
            this.linkedData = linkedData;
        }
    }
    
    // struct
    public static class Vertex
    {
        public float x;
        public float z;
        
        // Unnormalized vector perpendicular to its direction
        public float normX;
        public float normZ;
        
        //
        public float normalX;
        public float normalZ;
        
        public void normalize()
        {
            double magnitude = Math.sqrt((normX * normX) + (normZ * normZ));
            
            normalX = (float) (normX / magnitude);
            normalZ = (float) (normZ / magnitude);
        }
        
        public Vertex(float x, float z, float normX, float normZ)
        {
            this.x = x;
            this.z = z;
            this.normX = x;
            this.normZ = z;
        }
    }
}
