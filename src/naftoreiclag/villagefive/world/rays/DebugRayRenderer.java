/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.rays;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.PhysWorld;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.RaycastAdapter;
import org.dyn4j.geometry.Ray;

public class DebugRayRenderer extends RaycastAdapter
{
    public static Node rootNode;
    
    public Spatial last;
    
    @Override
    public boolean allow(Ray ray, Body body)
    {
        Vec2 s = new Vec2(ray.getStart());
        Vec2 d = new Vec2(ray.getDirectionVector());
        
        Vec2 b = s.add(d.mult(5));
        
        if(last != null)
        {
        last.removeFromParent();
        }
        
        last = new Geometry("nnn", PhysWorld.makeGeoLine(s, b));
        last.setMaterial(Main.mat_debug_wireframe);
        last.move(0, 0.1f, 0);
        
        rootNode.attachChild(last);
        
        return true;
        
    }
}
