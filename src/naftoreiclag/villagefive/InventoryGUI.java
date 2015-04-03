/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.gui.Element;
import naftoreiclag.villagefive.gui.Sprite;
import naftoreiclag.villagefive.gui.Collision;
import naftoreiclag.villagefive.gui.SpritePlane;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import java.util.Iterator;
import naftoreiclag.villagefive.util.KeyKeys;
import naftoreiclag.villagefive.util.math.SmoothBoolean;
import naftoreiclag.villagefive.util.math.Vec2;

public class InventoryGUI implements ActionListener, AnalogListener
{
    SpritePlane plane;
    
    Sprite mainInventory;
    
    boolean enabled;
    SmoothBoolean onoff = new SmoothBoolean();

    InventoryGUI(SpritePlane plane)
    {
        this.plane = plane;
        onoff.setTime(0.5d);
        
        mainInventory = new Sprite(SAM.ASSETS.loadTexture("Interface/inv.png"));
        plane.addElement(mainInventory);
        
        Collision box2 = new Collision(mainInventory.width, mainInventory.height);
        plane.addElement(box2);
        mainInventory.attachElement(box2);
        
        SAM.INPUT.addListener(this, KeyKeys.mouse_move, KeyKeys.mouse_left);
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
        
        mainInventory.setLoc(plane.width / 2, plane.height + 200 - (onoff.x * 400));
    }

    Vec2 mouseLoc = new Vec2();
    
    public void onAction(String name, boolean isPressed, float tpf)
    {
        if(name.equals(KeyKeys.mouse_left))
        {
            Element e = plane.rayCast(mouseLoc);
            
            System.out.println(e);
        }
    }

    public void onAnalog(String name, float value, float tpf)
    {
        if(name.equals(KeyKeys.mouse_move))
        {
            mouseLoc.set(SAM.INPUT.getCursorPosition());
            
            //mouseLoc.debug();
            
            Element e = plane.rayCast(mouseLoc);
            
            if(e != null)
            {
                //System.out.println(e);
            }
        }
    }
}
