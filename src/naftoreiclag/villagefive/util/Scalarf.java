/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import com.jme3.math.FastMath;

public class Scalarf
{
    public float x;
	
	public Scalarf(float x)
	{
		this.x = x;
	}
    
	public Scalarf() {}
    
    // Interpolate between two values linearly
	public void lerp(float other, float amount)
	{
        // If we are already within range
		if(difference(other) < amount)
		{
            // Then just be there already
			this.x = other;
		}
		else
		{
            // Try move linearly
			this.x = this.x + (amount * direction(other));
		}
	}
    
    // The direction (increase or decrease)
	public float direction(float other)
	{
		return FastMath.sign(other - this.x);
	}
    
    // The absolute difference
    public float difference(float other)
	{
        return FastMath.abs(other - this.x);
	}
}
