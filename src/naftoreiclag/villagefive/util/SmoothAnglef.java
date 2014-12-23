/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import static naftoreiclag.villagefive.util.Anglef.wrap;

public class SmoothAnglef extends Anglef
{
    // Target angle
    public float tx;
    
    //
    public float maxSpd = 5f;
    
    //
    public float smoothFactor = 20f;
    
    //
    Anglef dummy;
    
    public SmoothAnglef()
    {
        super();
        
        dummy = new Anglef();
    }
    
    public SmoothAnglef(float x)
    {
        super(x);
        
        dummy = new Anglef(x);
    }
    
    //
    public void tick(float tpf)
    {
        if(smoothEn)
        {

            dummy.lerp(tx, maxSpd * tpf);

            x = wrap(x + (direction(dummy.x) * (difference(dummy.x) / smoothFactor)));
        }
        else
        {
            x = this.tx;
        }

        //
    }

    public void setTarg(float tx)
    {
        this.tx = wrap(tx);
    }

    boolean smoothEn = true;
    public void disable()
    {
        smoothEn = false;
    }
    
    public void enable()
    {
        smoothEn = true;
    }
    
}
