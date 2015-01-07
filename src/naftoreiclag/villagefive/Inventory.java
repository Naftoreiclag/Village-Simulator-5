/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.math.SmoothBoolean;

public class Inventory
{
    SpritePlane plane;
    
    Sprite box;
    
    boolean enabled;
    SmoothBoolean onoff = new SmoothBoolean();

    Inventory(SpritePlane plane)
    {
        this.plane = plane;
        onoff.setTime(0.5d);
        
        box = new Sprite("Interface/inv.png");
        plane.attach(box);
        
        Sprite box2 = new Sprite("Interface/inv.png");
        plane.attach(box2);
        box.addFollower(box2);
        box2.setOrigin(50, 50);
        
    }
    
    public void enable()
    {
        this.enabled = true;
        onoff.gotoOne = true;
    }
    
    public void disable()
    {
        this.enabled = false;
        onoff.gotoOne = false;
    }


    void tick(float tpf)
    {
        onoff.tick(tpf);
        
        box.setLoc(plane.width / 2, plane.height + 200 - (onoff.x * 400));
        
        //System.out.println(onoff.x);
        //System.out.println("aaaa");
        
    }

}
