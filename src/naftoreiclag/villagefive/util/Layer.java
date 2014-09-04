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
    private List<Texvex> texvex = new ArrayList<Texvex>();
    private List<Line> lines = new ArrayList<Line>();
    
    public void addSeg(float x1, float z1, float x2, float z2, float tx1, float tx2)
    {
        /*     ^ normal
         *     |
         * 1 ----- 2
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
        
        for(int i = 0; i < texvex.size(); ++ i)
        {
            break;
        }
        
        float normX = z2 - z1;
        float normZ = x1 - x2;
        
        int indice1 = -1;
        int indice2 = -1;
        
        for(int i = 0; i < vertices.size(); ++ i)
        {
            Vertex comp = vertices.get(i);
            
            if(comp.x == x1 && comp.z == z1)
            {
                comp.normX += normX;
                comp.normZ += normZ;
                
                indice1 = i;
            }
            if(comp.x == x2 && comp.z == z2)
            {
                comp.normX += normX;
                comp.normZ += normZ;
                
                indice2 = i;
            }
            
            if(indice1 != -1 && indice2 != -1)
            {
                break;
            }
        }
        
        if(indice1 == -1)
        {
            indice1 = vertices.size();
            vertices.add(new Vertex(x1, z1, normX, normZ));
        }
        if(indice2 == -1)
        {
            indice2 = vertices.size();
            vertices.add(new Vertex(x2, z2, normX, normZ));
        }
        
        lines.add(new Line(indice1, indice2));
    }

    public Mesh bake()
    {
        FloatBuffer v = BufferUtils.createFloatBuffer(vertices.size() * 3);
        FloatBuffer n = BufferUtils.createFloatBuffer(vertices.size() * 3);
        FloatBuffer t = BufferUtils.createFloatBuffer(vertices.size() * 2);
        for(Vertex vert : vertices)
        {
            double magnitude = Math.sqrt((vert.normX * vert.normX) + (vert.normZ * vert.normZ));
            
            float normalX = (float) (vert.normX / magnitude);
            float normalZ = (float) (vert.normZ / magnitude);
            
            v.put(vert.x).put(0.0f).put(vert.z);
            n.put(normalX).put(0.0f).put(normalZ);
            t.put(vert.texX).put(vert.texY);
        }
        IntBuffer i = BufferUtils.createIntBuffer(triangles.size() * 3);
        for(Line tri : line)
        {
            // Note: I reversed the direction here to accommodate for JME.
            i.put(tri.a).put(tri.c).put(tri.b);
        }
        
        System.out.println("Model Built!");
        System.out.println("Polys: " + triangles.size());
        System.out.println("Vertices: " + (triangles.size() * 3));
        System.out.println("Output Verts: " + vertices.size());
        
                /*
        v.flip();
        n.flip();
        t.flip();
        i.flip();
                */
                
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
            this.indice1 = indice1;
            this.indice2 = indice2;
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
        
        public Vertex(float x, float z, float normX, float normZ)
        {
            this.x = x;
            this.z = z;
            this.normX = x;
            this.normZ = z;
        }
    }
}
