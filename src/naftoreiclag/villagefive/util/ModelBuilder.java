/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

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
	// Where the data is stored to be baked
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private List<Triangle> triangles = new ArrayList<Triangle>();
	
	// Add a triangle
	public void addTriangle(Vertex a, Vertex b, Vertex c)
	{
		// Find appropriate indices for these, re-using old ones if possible
		int ai = -1;
		int bi = -1;
		int ci = -1;
		for(int index = 0; index < vertices.size(); ++ index)
		{
			if(vertices.get(index).same(a))
			{
				ai = index;
			}
			if(vertices.get(index).same(b))
			{
				bi = index;
			}
			if(vertices.get(index).same(c))
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
	
	// Add a triangle
	public void addTriangle(
			float x1, float y1, float z1, Vector3f normal1, float texX1, float texY1,
			float x2, float y2, float z2, Vector3f normal2, float texX2, float texY2,
			float x3, float y3, float z3, Vector3f normal3, float texX3, float texY3)
	{
		// Make vertexes from this
		Vertex a = new Vertex(x1, y1, z1, normal1, texX1, texY1);
		Vertex b = new Vertex(x2, y2, z2, normal2, texX2, texY2);
		Vertex c = new Vertex(x3, y3, z3, normal3, texX3, texY3);
		
		this.addTriangle(a, b, c);
	}
	
	// Add a quad. Note: this just adds two triangles at once
	public void addQuad(Vertex a, Vertex b, Vertex c, Vertex d)
	{
		// 1  2
		//    
		// 4  3
		
		this.addTriangle(a, b, c);
		this.addTriangle(a, c, d);
	}
	
	// Add a quad. Note: this just adds two triangles at once
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
	
	// Bakes the data into a usable model. Note: You can bake this more than once if you really want to.
	public Mesh bake()
	{
		FloatBuffer v = BufferUtils.createFloatBuffer(vertices.size() * 3);
		FloatBuffer n = BufferUtils.createFloatBuffer(vertices.size() * 3);
		FloatBuffer t = BufferUtils.createFloatBuffer(vertices.size() * 2);
		for(Vertex vert : vertices)
		{
                    v.put(vert.x).put(vert.y).put(vert.z);
                    n.put(vert.normal.x).put(vert.normal.y).put(vert.normal.z);
                    t.put(vert.texX).put(vert.texY);
		}
		IntBuffer i = BufferUtils.createIntBuffer(triangles.size() * 3);
		for(Triangle tri : triangles)
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
		public final float x;
		public final float y;
		public final float z;
		public final Vector3f normal;
		public final float texX;
		public final float texY;
		
		public Vertex(float x, float y, float z, Vector3f normal, float texX, float texY)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.normal = normal;
			this.texX = texX;
			this.texY = texY;
		}
		
		private boolean vComp(Vector3f a, Vector3f b)
		{
			//return true;
			return a.x == b.x && a.y == b.y && a.z == b.z;
		}
		
		public boolean same(Vertex other)
		{
			// In case this is literally the same object
			if(this == other) { return true; }
			// Functionally the same
			return other.x == this.x && other.y == this.y && other.z == this.z && vComp(other.normal, this.normal) && other.texX == this.texX && other.texY == this.texY;
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

