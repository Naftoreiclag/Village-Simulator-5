/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

// Build a better mousetrap
public class ReiCamera
{
    public final Camera cam;
    
    public final Vector3f targetLoc = new Vector3f();
    public final Vector3f dummyLoc = new Vector3f();
    public final Vector3f cubicLoc = new Vector3f();
    
    public final Vector3f targetView = new Vector3f();
    public final Vector3f dummyView = new Vector3f();
    public final Vector3f cubicView = new Vector3f();
    
    public float maxSpd = 5.0f;
    public float exp = 20f;
    
    public ReiCamera(Camera cam)
    {
        this.cam = cam;
    }
    
    public SmoothMode mode = SmoothMode.cubic;
    
    public void setLocation(Vector3f where)
    {
        targetLoc.set(where);
    }
    public void setLocation(float x, float y, float z)
    {
        targetLoc.set(x, y, z);
    }
    public void lookAt(Vector3f where)
    {
        targetView.set(where);
    }
    
    public void tick(float tpf)
    {
        if(mode == SmoothMode.none)
        {
            return;
        }
        
        // interpolate dummies linearly
        if(dummyLoc.distanceSquared(targetLoc) > maxSpd * maxSpd)
        {
            dummyLoc.addLocal(targetLoc.subtract(dummyLoc).normalizeLocal().multLocal(maxSpd));
        }
        else
        {
            dummyLoc.set(targetLoc);
        }
        if(dummyView.distanceSquared(targetView) > maxSpd * maxSpd)
        {
            dummyView.addLocal(targetView.subtract(dummyView).normalizeLocal().multLocal(maxSpd));
        }
        else
        {
            dummyView.set(targetView);
        }
        
        // aaaa
        if(mode == SmoothMode.linear)
        {
            cam.setLocation(dummyLoc);
            cam.lookAt(dummyView, Vector3f.UNIT_Y);
        }
        else if(mode == SmoothMode.cubic)
        {
            cubicLoc.addLocal(cubicLoc.subtract(targetLoc).divideLocal(exp));
            cubicView.addLocal(cubicView.subtract(targetLoc).divideLocal(exp));
            
            cam.setLocation(cubicLoc);
            cam.lookAt(cubicView, Vector3f.UNIT_Y);
        }
    }
    
    
    public static enum SmoothMode
    {
        none,
        linear,
        cubic, // is it really called this?
        inverse
    }
}
