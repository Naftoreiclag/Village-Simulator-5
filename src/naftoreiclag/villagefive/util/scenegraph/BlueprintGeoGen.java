/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.scenegraph;

import naftoreiclag.villagefive.util.math.Vec2;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;

public class BlueprintGeoGen
{
    List<Line> lines = new ArrayList<Line>();
    
    public void addLine(Vec2 a, Vec2 b)
    {
        lines.add(new Line((float) a.getX(), (float) a.getY(), (float) b.getX(), (float) b.getY()));
    }
    public void addLine(float ax, float ay, float bx, float by)
    {
        lines.add(new Line(ax, ay, bx, by));
    }

    // Axis-aligned rectangles
    public void addRect(float x1, float y1, float x2, float y2)
    {
        this.addLine(x1, y1, x1, y2);
        this.addLine(x1, y2, x2, y2);
        this.addLine(x2, y2, x2, y1);
        this.addLine(x2, y1, x1, y1);
    }
    
    public void addRect(Vec2 a, Vec2 b, Vec2 c, Vec2 d)
    {
        this.addLine(a, b);
        this.addLine(b, c);
        this.addLine(c, d);
        this.addLine(d, a);
    }
    
    public Mesh bake(float thickness, float texStretch, float xscale, float yscale)
    {
        FloatBuffer v = BufferUtils.createFloatBuffer(lines.size() * 12);
        FloatBuffer t = BufferUtils.createFloatBuffer(lines.size() * 8);
        for(Line l : lines)
        {
            /*       p
             * A     |      D
             * w-a--------b
             * B            C
             */
            
            Vec2 p = new Vec2(l.a.getYf() - l.b.getYf(), l.b.getXf() - l.a.getXf()).normalize().multLocal(thickness); // vector that is 90 deg away (counter-clockwise)
            Vec2 w = new Vec2(l.a.getXf() - l.b.getXf(), l.a.getYf() - l.b.getYf()).normalize().multLocal(thickness); // vector that is 180 deg away
            
            Vec2 A = l.a.add(p).addLocal(w);
            Vec2 B = l.a.subtract(p).addLocal(w);
            Vec2 C = l.b.subtract(p).subtractLocal(w);
            Vec2 D = l.b.add(p).subtractLocal(w);
            
            float leng = A.distF(D) / thickness;
            leng /= texStretch;
            leng /= 2f;
            
            v.put(A.getXf() * xscale).put(0f).put(A.getYf() * yscale);
            t.put(-leng).put(0.0f);
            v.put(B.getXf() * xscale).put(0f).put(B.getYf() * yscale);
            t.put(-leng).put(1.0f);
            v.put(C.getXf() * xscale).put(0f).put(C.getYf() * yscale);
            t.put(leng).put(1.0f);
            v.put(D.getXf() * xscale).put(0f).put(D.getYf() * yscale);
            t.put(leng).put(0.0f);
        }
        IntBuffer i = BufferUtils.createIntBuffer(lines.size() * 6);
        int ioff = 0;
        for(Line l : lines)
        {
            // Clockwise???
            i.put(ioff).put(ioff + 2).put(ioff + 1);
            i.put(ioff).put(ioff + 3).put(ioff + 2);
            
            ioff += 4;
        }
        
                
        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, t);
        mesh.setBuffer(VertexBuffer.Type.Index,    3, i);

        mesh.updateBound();
                
        return mesh;
    }
    
    public static class Line
    {
        final Vec2 a;
        final Vec2 b;
        
        public Line(Vec2 a, Vec2 b)
        {
            this.a = a.clone();
            this.b = b.clone();
        }
        public Line(float ax, float ay, float bx, float by)
        {
            this.a = new Vec2(ax, ay);
            this.b = new Vec2(bx, by);
        }
    }
}
