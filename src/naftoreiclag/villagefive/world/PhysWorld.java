/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import java.nio.FloatBuffer;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.Vec2;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Shape;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Polygon;
import org.lwjgl.BufferUtils;

public class PhysWorld extends org.dyn4j.dynamics.World
{
    public Node debugShow()
    {
        Node node = new Node();
        
        for(Joint joint : this.joints)
        {
            Mesh mesh = this.makeGeo(joint);

            if(mesh == null)
            {
                continue;
            }

            Geometry geo = new Geometry("", mesh);
            geo.setMaterial(Main.mat_debug_wireframe);
                
            Vec2 loc = new Vec2(joint.getAnchor1());

            geo.setLocalTranslation(loc.getXF(), 0f, loc.getYF());

            node.attachChild(geo);
        }
        
        for(Body body : this.bodies)
        {
            for(BodyFixture bf : body.getFixtures())
            {
                Convex convex = bf.getShape();
                
                Mesh mesh = this.makeGeo(convex);
                
                if(mesh == null)
                {
                    continue;
                }
                
                Geometry geo = new Geometry("", mesh);
                geo.setMaterial(Main.mat_debug_wireframe);
                
                Vec2 loc = new Vec2(body.getTransform().getTranslation());
                Angle rot = new Angle(body.getTransform());
                
                geo.setLocalTranslation(loc.getXF(), 0f, loc.getYF());
                
                // See? this is what I was talking about.
                rot.setX(rot.getX() + FastMath.HALF_PI);
                geo.setLocalRotation(rot.toQuaternion());
                
                node.attachChild(geo);
            }
        }
        
        return node;
    }

    // Ugly
    private Mesh makeGeo(Shape shape)
    {
        if(shape instanceof Circle)
        {
            return makeGeoCircle((Circle) shape);
        }
        else if(shape instanceof Polygon)
        {
            return makeGeoPoly((Polygon) shape);
        }
        
        return null;
    }

    public static Mesh makeGeoCircle(Circle circle)
    {
        int num = 16;
        
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.LineLoop);
        
        FloatBuffer v = BufferUtils.createFloatBuffer(num * 3);
        
        Vec2 center = new Vec2(circle.getCenter());
        double radius = circle.getRadius();
        
        for(int i = 0; i < num; ++ i)
        {
            Vec2 loc = Vec2.fromAngle((Math.PI * 2 * i) / num).multLocal(radius);
            loc.addLocal(center);
            
            v.put(loc.getXF()).put(0f).put(loc.getYF());
        }

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);

        mesh.updateBound();
        
        return mesh;
    }

    public static Mesh makeGeoPoly(Polygon polygon)
    {
        
        
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.LineLoop);
        FloatBuffer v = BufferUtils.createFloatBuffer(polygon.getVertices().length * 3);
        
        
        for(int i = 0; i < polygon.getVertices().length; ++ i)
        {
            Vec2 loc = new Vec2(polygon.getVertices()[i]);
            
            v.put(loc.getXF()).put(0f).put(loc.getYF());
        }
        
        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);

        mesh.updateBound();
        return mesh;
    }
    public static Mesh makeGeoLine(Vec2 a, Vec2 b)
    {
        
        
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.LineLoop);
        FloatBuffer v = BufferUtils.createFloatBuffer(2 * 3);
        v.put(a.getXF()).put(0f).put(a.getYF());
        v.put(b.getXF()).put(0f).put(b.getYF());
        
        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);

        mesh.updateBound();
        return mesh;
    }

    private Mesh makeGeo(Joint joint)
    {
        return makeGeoCircle(new Circle(0.2));
    }
    
    
}
