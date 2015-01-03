/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.Vec2;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Shape;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Ellipse;
import org.dyn4j.geometry.HalfEllipse;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Slice;
import org.dyn4j.geometry.Vector2;
import org.lwjgl.BufferUtils;

public class PhysWorld extends org.dyn4j.dynamics.World
{
    public Node debugShow()
    {
        Node node = new Node();
        
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
                Angle rot = new Angle(body.getTransform().getRotation());
                
                geo.setLocalTranslation(loc.getXF(), 0f, loc.getYF());
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
            return this.makeGeoCircle((Circle) shape);
        }
        else if(shape instanceof Polygon)
        {
            return this.makeGeoPoly((Polygon) shape);
        }
        
        return null;
    }

    private Mesh makeGeoCircle(Circle circle)
    {
        int num = 20;
        
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.LineLoop);
        
        FloatBuffer v = BufferUtils.createFloatBuffer(num * 3);
        
        Vec2 center = new Vec2(circle.getCenter());
        double radius = circle.getRadius();
        
        for(int i = 0; i < num; ++ i)
        {
            Vec2 bbb = Vec2.fromAngle((Math.PI * 2 * i) / num).multLocal(radius);
            bbb.addLocal(center);
            
            v.put(bbb.getXF()).put(0f).put(bbb.getYF());
        }

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);

        mesh.updateBound();
        
        return mesh;
    }

    private Mesh makeGeoPoly(Polygon polygon)
    {
        return null;
    }
    
    
}
