/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import com.jme3.math.FastMath;

public class Anglef
{
	public float x;
	
	public Anglef(float x)
	{
		this.x = x;
	}
    
	public Anglef() {}
    
    // Tween between two values linearly
	public Anglef tweenLocal(float other, float amount)
	{
        other = normalize(other);
        
		float displacement = other - this.x;
		float dirSign = Math.signum(displacement);
        
        // If the distance with the given direction is obtuse
        if(displacement * dirSign > FastMath.PI)
        {
            // Then flip the direction around, since the other way is obviously faster (acute)
            dirSign = -dirSign;
        }
		
        // If we are already within range
		if(displacement * dirSign < amount)
		{
            // Then just be there already
			this.x = other;
		}
		else
		{
            // Try move linearly
			this.x = normalize(this.x + (amount * dirSign));
		}
		
		return this;
	}
    
    // Makes sure that this float is between 0 to tau
    public static float normalize(float a)
    {
        return ((a % FastMath.TWO_PI) + FastMath.TWO_PI) % FastMath.TWO_PI;
    }
    
    // Like regular modulus, but it "wraps around" (e.g. magicmod(-1, 5) = 4)
    public static float magicModulus(float a, float b)
    {
        return ((a % b) + b) % b;
    }
}

