/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import com.jme3.math.FastMath;

public class Anglef
{
	public float a;
	
	public Anglef(float x)
	{
		this.a = x;
	}
    
	public Anglef()
	{
	}
	
	public Anglef tweenLocal(float other, float amount)
	{
        other = magicModulus(other, FastMath.TWO_PI);
        
		float diff = other - this.a;
		float sig = Math.signum(diff);
        
        float absDiff = Math.abs(diff);
		
		if(absDiff < amount)
		{
			this.a = other;
		}
		else
		{
            
			if(absDiff > FastMath.PI)
			{
				sig = -sig;
			}
			
			this.a = magicModulus(this.a + (amount * sig), FastMath.TWO_PI);
		}
		
		//this.a = other;
		return this;
	}
    
    public static float magicModulus(float a, float b)
    {
        return ((a % b) + b) % b;
    }
}

