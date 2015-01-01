/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

import com.jme3.math.FastMath;

public class SmoothScalarf extends Scalarf
{
    // Target amount
    public double tx;
    
    //
    public double maxSpd = 5f;
    
    //
    public double smoothFactor = 20f;
    
    //
    Scalarf dummy;
    
    //
    boolean clamp = false;
    double min;
    double max;
    
    public void setClamp(double min, double max)
    {
        this.min = min;
        this.max = max;
    }
    public void enableClamp(double min, double max)
    {
        this.min = min;
        this.max = max;
        enableClamp();
    }
    public void enableClamp()
    {
        this.clamp = true;
    }
    public void disableClamp()
    {
        this.clamp = false;
    }
    
    public SmoothScalarf()
    {
        super();
        
        dummy = new Scalarf();
    }
    
    public SmoothScalarf(double x)
    {
        super(x);
        
        dummy = new Scalarf(x);
    }
    
    //
    public void tick(double tpf)
    {
        if(clamp)
        {
            if(tx > max)
            {
                tx = max;
            }
            if(tx < min)
            {
                tx = min;
            }
        }
        
        if(smooth)
        {
            dummy.lerp(tx, maxSpd * tpf);

            x = x + (direction(dummy.x) * (difference(dummy.x) / smoothFactor));
        }
        else
        {
            x = this.tx;
        }
        
        
        if(clamp)
        {
            if(dummy.x > max)
            {
                dummy.x = max;
            }
            if(dummy.x < min)
            {
                dummy.x = min;
            }
        }

        //
    }

    boolean smooth = true;
    public void disableSmoothing()
    {
        smooth = false;
    }
    
    public void enableSmoothing()
    {
        smooth = true;
    }
    
}
