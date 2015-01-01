/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.scenegraph;

import naftoreiclag.villagefive.util.math.Vec2;
import com.jme3.math.Vector2f;
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
    
    public void addLine(Vector2f a, Vector2f b)
    {
        lines.add(new Line(a, b));
    }
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
    
    public void addRect(Vector2f a, Vector2f b, Vector2f c, Vector2f d)
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
            
            Vector2f p = new Vector2f(l.a.y - l.b.y, l.b.x - l.a.x).normalize().multLocal(thickness); // vector that is 90 deg away (counter-clockwise)
            Vector2f w = new Vector2f(l.a.x - l.b.x, l.a.y - l.b.y).normalize().multLocal(thickness); // vector that is 180 deg away
            
            Vector2f A = l.a.add(p).addLocal(w);
            Vector2f B = l.a.subtract(p).addLocal(w);
            Vector2f C = l.b.subtract(p).subtractLocal(w);
            Vector2f D = l.b.add(p).subtractLocal(w);
            
            float leng = A.distance(D) / thickness;
            leng /= texStretch;
            leng /= 2f;
            
            v.put(A.x * xscale).put(0f).put(A.y * yscale);
            t.put(-leng).put(0.0f);
            v.put(B.x * xscale).put(0f).put(B.y * yscale);
            t.put(-leng).put(1.0f);
            v.put(C.x * xscale).put(0f).put(C.y * yscale);
            t.put(leng).put(1.0f);
            v.put(D.x * xscale).put(0f).put(D.y * yscale);
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
        final Vector2f a;
        final Vector2f b;
        
        public Line(Vector2f a, Vector2f b)
        {
            this.a = a.clone();
            this.b = b.clone();
        }
        public Line(float ax, float ay, float bx, float by)
        {
            this.a = new Vector2f(ax, ay);
            this.b = new Vector2f(bx, by);
        }
    }
}
