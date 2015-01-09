/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.scenegraph;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;

public class ModelBuilder
{
    // Optionally offset where vertexes are added
    private float appendX = 0;
    private float appendY = 0;
    private float appendZ = 0;
    public void setAppendOrigin(float x, float y, float z)
    {
        this.appendX = x;
        this.appendY = y;
        this.appendZ = z;
    }
    public void setAppendOrigin(double x, double y, double z)
    {
        this.appendX = (float) x;
        this.appendY = (float) y;
        this.appendZ = (float) z;
    }
    public void addAppendOrigin(float x, float y, float z)
    {
        this.appendX += x;
        this.appendY += y;
        this.appendZ += z;
    }
    public void addAppendOrigin(double x, double y, double z)
    {
        this.appendX += (float) x;
        this.appendY += (float) y;
        this.appendZ += (float) z;
    }
    public void resetAppendOrigin()
    {
        this.appendX = 0;
        this.appendY = 0;
        this.appendZ = 0;
    }
    
    // Whether or not to "smooth" normals
    public boolean combineNormals = false;
    
    // Where the data is stored to be baked
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private List<Triangle> triangles = new ArrayList<Triangle>();
    
    // Add a triangle with respects to the user-set origin
    public void addTriangle(Vertex a, Vertex b, Vertex c)
    {
        if(appendX == 0 && appendY == 0 && appendZ == 0)
        {
            addTriangleNoAppend(a, b, c);
        }
        else
        {
            addTriangleNoAppend(a.clone(appendX, appendY, appendZ), b.clone(appendX, appendY, appendZ), c.clone(appendX, appendY, appendZ));
        }
    }
    
    // Add a triangle without appending anything
    private void addTriangleNoAppend(Vertex a, Vertex b, Vertex c)
    {
        // Find appropriate indices for these, re-using old ones if possible
        int ai = -1;
        int bi = -1;
        int ci = -1;
        for(int index = 0; index < vertices.size(); ++ index)
        {
            Vertex compare = vertices.get(index);
            
            if(combineNormals)
            {
                if(compare.samePos(a))
                {
                    Vector3f combined = compare.normal.add(a.normal).normalizeLocal();
                    compare.normal = combined;
                    a.normal = combined;
                }
                if(compare.samePos(b))
                {
                    Vector3f combined = compare.normal.add(b.normal).normalizeLocal();
                    compare.normal = combined;
                    b.normal = combined;
                }
                if(compare.samePos(c))
                {
                    Vector3f combined = compare.normal.add(c.normal).normalizeLocal();
                    compare.normal = combined;
                    c.normal = combined;
                }
            }
            if(compare.same(a))
            {
                ai = index;
            }
            if(compare.same(b))
            {
                bi = index;
            }
            if(compare.same(c))
            {
                ci = index;
            }
            
            if(ai != -1 && bi != -1 && ci != -1)
            {
                break;
            }
        }
        if(ai == -1)
        {
            ai = vertices.size();
            vertices.add(a);
        }
        if(bi == -1)
        {
            bi = vertices.size();
            vertices.add(b);
        }
        if(ci == -1)
        {
            ci = vertices.size();
            vertices.add(c);
        }
        
        // Add this to our triangles
        triangles.add(new Triangle(ai, bi, ci));
    }
    
    // Add a triangle with respects to the user-set origin
    public void addTriangle(
            float x1, float y1, float z1, Vector3f normal1, float texX1, float texY1,
            float x2, float y2, float z2, Vector3f normal2, float texX2, float texY2,
            float x3, float y3, float z3, Vector3f normal3, float texX3, float texY3)
    {
        // Make vertexes from this
        Vertex a = new Vertex(x1 + appendX, y1 + appendY, z1 + appendZ, normal1, texX1, texY1);
        Vertex b = new Vertex(x2 + appendX, y2 + appendY, z2 + appendZ, normal2, texX2, texY2);
        Vertex c = new Vertex(x3 + appendX, y3 + appendY, z3 + appendZ, normal3, texX3, texY3);
        
        this.addTriangleNoAppend(a, b, c);
    }
    
    // Add a quad with respects to the user-set origin. Note: this just adds two triangles at once
    public void addQuad(Vertex a, Vertex b, Vertex c, Vertex d)
    {
        // 1  2
        //    
        // 4  3
        
        this.addTriangle(a, b, c);
        this.addTriangle(a, c, d);
    }
    
    // Add a quad with Vertor3f. Note: this is mainly used for debugging
    public void addQuad(Vector3f a, Vector3f b, Vector3f c, Vector3f d)
    {
        // 1  2
        //    
        // 4  3
        
        this.addQuad(a.x, a.y, a.z, Vector3f.UNIT_Y, 0f, 0f, 
                     b.x, b.y, b.z, Vector3f.UNIT_Y, 1f, 0f, 
                     c.x, c.y, c.z, Vector3f.UNIT_Y, 1f, 1f, 
                     d.x, d.y, d.z, Vector3f.UNIT_Y, 0f, 1f);
    }
    
    
    // Add a quad with respects to the user-set origin. Note: this just adds two triangles at once
    public void addQuad(
            float x1, float y1, float z1, Vector3f normal1, float texX1, float texY1,
            float x2, float y2, float z2, Vector3f normal2, float texX2, float texY2,
            float x3, float y3, float z3, Vector3f normal3, float texX3, float texY3,
            float x4, float y4, float z4, Vector3f normal4, float texX4, float texY4)
    {
        // 1  2
        //    
        // 4  3
        
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x2, y2, z2, normal2, texX2, texY2, x3, y3, z3, normal3, texX3, texY3);
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x3, y3, z3, normal3, texX3, texY3, x4, y4, z4, normal4, texX4, texY4);
    }
    
    public void addPentagon(Vertex a, Vertex b, Vertex c, Vertex d, Vertex e)
    {
        this.addTriangle(a, b, c);
        this.addTriangle(a, c, d);
        this.addTriangle(a, d, e);
    }
    
    public void addPentagon(
            float x1, float y1, float z1, Vector3f normal1, float texX1, float texY1,
            float x2, float y2, float z2, Vector3f normal2, float texX2, float texY2,
            float x3, float y3, float z3, Vector3f normal3, float texX3, float texY3,
            float x4, float y4, float z4, Vector3f normal4, float texX4, float texY4,
            float x5, float y5, float z5, Vector3f normal5, float texX5, float texY5)
    {
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x2, y2, z2, normal2, texX2, texY2, x3, y3, z3, normal3, texX3, texY3);
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x3, y3, z3, normal3, texX3, texY3, x4, y4, z4, normal4, texX4, texY4);
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x4, y4, z4, normal4, texX4, texY4, x5, y5, z5, normal5, texX5, texY5);
    }
    
    public void addHexagon(Vertex a, Vertex b, Vertex c, Vertex d, Vertex e, Vertex f)
    {
        this.addTriangle(a, b, c);
        this.addTriangle(a, c, d);
        this.addTriangle(a, d, e);
        this.addTriangle(a, e, f);
    }
    
    public void addHexagon(
            float x1, float y1, float z1, Vector3f normal1, float texX1, float texY1,
            float x2, float y2, float z2, Vector3f normal2, float texX2, float texY2,
            float x3, float y3, float z3, Vector3f normal3, float texX3, float texY3,
            float x4, float y4, float z4, Vector3f normal4, float texX4, float texY4,
            float x5, float y5, float z5, Vector3f normal5, float texX5, float texY5,
            float x6, float y6, float z6, Vector3f normal6, float texX6, float texY6)
    {
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x2, y2, z2, normal2, texX2, texY2, x3, y3, z3, normal3, texX3, texY3);
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x3, y3, z3, normal3, texX3, texY3, x4, y4, z4, normal4, texX4, texY4);
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x4, y4, z4, normal4, texX4, texY4, x5, y5, z5, normal5, texX5, texY5);
        this.addTriangle(x1, y1, z1, normal1, texX1, texY1, x5, y5, z5, normal5, texX5, texY5, x6, y6, z6, normal6, texX6, texY6);
    }
    
    // Bakes the data into a usable model. Note: You can bake this more than once if you really want to.
    public Mesh bake()
    {
        return bake(1.0f, 1.0f, 1.0f);
    }
    
    public Mesh bake(float xscale, float yscale, float zscale)
    {
        FloatBuffer v = BufferUtils.createFloatBuffer(vertices.size() * 3);
        FloatBuffer n = BufferUtils.createFloatBuffer(vertices.size() * 3);
        FloatBuffer t = BufferUtils.createFloatBuffer(vertices.size() * 2);
        for(Vertex vert : vertices)
        {
                    v.put(vert.x * xscale).put(vert.y * yscale).put(vert.z * zscale);
                    n.put(vert.normal.x).put(vert.normal.y).put(vert.normal.z);
                    t.put(vert.texX).put(vert.texY);
        }
        IntBuffer i = BufferUtils.createIntBuffer(triangles.size() * 3);
        for(Triangle tri : triangles)
        {
                    // Note: I reversed the direction here to accommodate for JME.
                    i.put(tri.a).put(tri.c).put(tri.b);
        }
        
                
        Mesh mesh = new Mesh();

        mesh.setBuffer(Type.Position, 3, v);
        mesh.setBuffer(Type.Normal,   3, n);
        mesh.setBuffer(Type.TexCoord, 2, t);
        mesh.setBuffer(Type.Index,    3, i);

        mesh.updateBound();
                
        return mesh;
    }
    
    // Class for storing a single vertex's data
    public static class Vertex
    {
        public float x;
        public float y;
        public float z;
        public Vector3f normal;
        public float texX;
        public float texY;
        
        public Vertex(float x, float y, float z, Vector3f normal, float texX, float texY)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.normal = normal;
            this.texX = texX;
            this.texY = texY;
        }
        
        public Vertex(double x, double y, double z, Vector3f normal, double texX, double texY)
        {
            this.x = (float) x;
            this.y = (float) y;
            this.z = (float) z;
            this.normal = normal;
            this.texX = (float) texX;
            this.texY = (float) texY;
        }
        
        // Clone and add values to location
        public Vertex clone(float x, float y, float z)
        {
            return new Vertex(this.x + x, this.y + y, this.z + z, normal.clone(), texX, texY);
        }
        
        // If two vectors are the same
        private boolean compareVectors(Vector3f a, Vector3f b)
        {
            //return true;
            return a == b || (a.x == b.x && a.y == b.y && a.z == b.z);
        }
        
        // Returns if this has the same position, no regards to tex or normals
        public boolean samePos(Vertex other)
        {
            // In case this is literally the same object
            if(this == other) { return true; }
            // Functionally the same
            return other.x == this.x && other.y == this.y && other.z == this.z;
        }
        
        // Returns if this is exactly the same as another vertex
        public boolean same(Vertex other)
        {
            // In case this is literally the same object
            if(this == other) { return true; }
            // Functionally the same
            return other.x == this.x && other.y == this.y && other.z == this.z && compareVectors(other.normal, this.normal) && other.texX == this.texX && other.texY == this.texY;
        }
    }
    
    // Private class for storing a tuple of integers
    private class Triangle
    {
        private int a;
        private int b;
        private int c;
        
        public Triangle(int a, int b, int c)
        {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}

