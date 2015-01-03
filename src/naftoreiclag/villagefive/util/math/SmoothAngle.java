/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

import static naftoreiclag.villagefive.util.math.Angle.wrap;

public class SmoothAngle extends Angle
{
    // Target angle
    public double tx;
    
    //
    public double maxSpd = 5;
    
    //
    public double smoothFactor = 20;
    
    //
    Angle dummy;
    
    public SmoothAngle()
    {
        super();
        
        dummy = new Angle();
    }
    
    public SmoothAngle(double x)
    {
        super(x);
        
        dummy = new Angle(x);
    }
    
    //
    public void tick(double tpf)
    {
        if(smoothEn)
        {

            dummy.lerpLocal(tx, maxSpd * tpf);

            setX(wrap(getX() + (calcDir(dummy.getX()) * (calcDiff(dummy.getX()) / smoothFactor))));
        }
        else
        {
            setX(this.tx);
        }

        //
    }

    boolean smoothEn = true;
    public void disableSmoothing()
    {
        smoothEn = false;
    }
    
    public void enableSmoothing()
    {
        smoothEn = true;
    }
    
}
