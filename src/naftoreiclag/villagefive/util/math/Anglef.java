/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

import com.jme3.math.FastMath;

public class Anglef
{
	public double x;
	
	public Anglef(double x)
	{
		this.x = x;
	}
    
	public Anglef() {}
    
    // Interpolate between two values linearly
	public void lerp(double other, double amount)
	{
        // If we are already within range
		if(difference(other) < amount)
		{
            // Then just be there already
			this.x = wrap(other);
		}
		else
		{
            // Try move linearly
			this.x = wrap(this.x + (amount * direction(other)));
		}
	}
    
    // The direction (clockwise or counter-clockwise) to get to another angle
	public double direction(double other)
	{
        other = wrap(other);
        
		double displacement = other - this.x;
		double dirSign = Math.signum(displacement);
        
        // If the distance with the given direction is reflex (ie > 180 degrees)
        if(displacement * dirSign > FastMath.PI)
        {
            // Then flip the direction around, since the other way is obviously faster (acute)
            dirSign = -dirSign;
        }
		
		return dirSign;
	}
    
    // The measure of the angle formed with this and another angle (unsigned)
    public double difference(double other)
	{
        other = wrap(other);
        
		double diff = Math.abs(other - this.x);
        
        // If the angle with the given direction is reflex (ie > 180 degrees)
        if(diff > FastMath.PI)
        {
            // Then it can't be the shortest. It must be the other way around.
            return FastMath.TWO_PI - diff;
        }
        
        return diff;
	}
    
    // Makes sure that this double is between 0 to tau
    public static double wrap(double a)
    {
        return ((a % FastMath.TWO_PI) + FastMath.TWO_PI) % FastMath.TWO_PI;
    }
    
    // Like regular modulus, but it "wraps around" (e.g. magicmod(-1, 5) = 4)
    public static double magicModulus(double a, double b)
    {
        return ((a % b) + b) % b;
    }
}

