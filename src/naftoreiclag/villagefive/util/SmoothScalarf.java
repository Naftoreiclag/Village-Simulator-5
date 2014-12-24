/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import com.jme3.math.FastMath;

public class SmoothScalarf extends Scalarf
{
    // Target amount
    public float tx;
    
    //
    public float maxSpd = 5f;
    
    //
    public float smoothFactor = 20f;
    
    //
    Scalarf dummy;
    
    //
    boolean clamp = false;
    float min;
    float max;
    
    public void setClamp(float min, float max)
    {
        this.min = min;
        this.max = max;
    }
    public void enableClamp(float min, float max)
    {
        this.min = min;
        this.max = max;
        this.clamp = true;
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
    
    public SmoothScalarf(float x)
    {
        super(x);
        
        dummy = new Scalarf(x);
    }
    
    //
    public void tick(float tpf)
    {
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
            x = FastMath.clamp(x, min, max);
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
