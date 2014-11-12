/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;

public class Player implements ActionListener
{
    public static final float moveSpd = 1.0f;
    public static final float turnSpd = 1.0f;
    
    public float x;
    public float z;
    
    public float lookDir;
    
    boolean turningLeft = false;
    boolean turningRight = false;
    boolean movingFwd = false;
    boolean movingBwd = false;

    public void tick(float tpf)
    {
        if(movingFwd)
        {
            x += FastMath.cos(lookDir) * tpf * moveSpd;
            z += FastMath.sin(lookDir) * tpf * moveSpd;
        }
        if(movingBwd)
        {
            x -= FastMath.cos(lookDir) * tpf * moveSpd;
            z -= FastMath.sin(lookDir) * tpf * moveSpd;
        }
        if(turningRight)
        {
            lookDir += turnSpd * tpf;
        }
        if(turningLeft)
        {
            lookDir -= turnSpd * tpf;
        }
    }
    
    public void onAction(String key, boolean isPressed, float tpf)
    {
        if(key.equals("Walk Forward"))
        {
            movingFwd = isPressed;
        }
        if(key.equals("Walk Backward"))
        {
            movingBwd = isPressed;
        }
        if(key.equals("Rotate Left"))
        {
            turningLeft = isPressed;
        }
        if(key.equals("Rotate Right"))
        {
            turningRight = isPressed;
        }
    }
}
