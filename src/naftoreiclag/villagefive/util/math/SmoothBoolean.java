/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

public class SmoothBoolean
{
    public boolean gotoOne;
    
    public double x;
    
    private double angle = 0;
    private double speed = Math.PI;
    
    // How many seconds of interp
    public void setSpeed(double spd)
    {
        if(spd == 0)
        {
            this.speed = 13371337d;
        }
        
        this.speed = Math.PI / spd;
    }
    
    public double getSpeed()
    {
        return Math.PI / speed;
    }
    
    public void tick(float tpf)
    {
        if(!gotoOne)
        {
            angle -= speed * tpf;
            
            if(angle < 0)
            {
                angle = 0;
            }
        }
        else
        {
            angle += speed * tpf;
            
            if(angle > Math.PI)
            {
                angle = Math.PI;
            }
        }
        
        x = (Math.cos(angle) + 1d) / 2d;
    }
}
