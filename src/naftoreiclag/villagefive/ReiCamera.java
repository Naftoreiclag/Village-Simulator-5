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
    public final Camera c;
    
    public final Vector3f targetLoc;
    public final Vector3f dummyLoc;
    public final Vector3f cubicLoc;
    
    public final Vector3f targetView;
    public final Vector3f dummyView;
    public final Vector3f cubicView;
    
    public float maxSpd = 5.0f;
    public float exp = 20f;
    
    public ReiCamera(Camera cam)
    {
        this.c = cam;
        
        this.targetLoc = c.getLocation().clone();
        this.dummyLoc = this.targetLoc.clone();
        this.cubicLoc = this.targetLoc.clone();
        this.targetView = targetLoc.add(c.getDirection());
        this.dummyView = this.targetView.clone();
        this.cubicView = this.targetView.clone();
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
    void lookAt(float x, float y, float z)
    {
        targetView.set(x, y, z);
    }
    
    public void tick(float tpf)
    {
        if(mode == SmoothMode.none)
        {
            return;
        }
        
        // interpolate dummies linearly
        float dummyLocDist = dummyLoc.distance(targetLoc);
        float matchedSpd = dummyLocDist / (maxSpd * tpf);
        if(dummyLocDist > maxSpd * tpf)
        {
            dummyLoc.addLocal(targetLoc.subtract(dummyLoc).normalizeLocal().multLocal(maxSpd * tpf));
            dummyView.addLocal(targetView.subtract(dummyView).divideLocal(matchedSpd));
        }
        else
        {
            dummyLoc.set(targetLoc);
            dummyView.set(targetView);
            
        }
        
        // aaaa
        if(mode == SmoothMode.linear)
        {
            c.setLocation(dummyLoc);
            c.lookAt(dummyView, Vector3f.UNIT_Y);
        }
        else if(mode == SmoothMode.cubic)
        {
            cubicLoc.addLocal(targetLoc.subtract(cubicLoc).divideLocal(exp));
            cubicView.addLocal(targetView.subtract(cubicView).divideLocal(exp));
            
            c.setLocation(cubicLoc);
            c.lookAt(cubicView, Vector3f.UNIT_Y);
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
