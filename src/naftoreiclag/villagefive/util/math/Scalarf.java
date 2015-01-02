/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

import com.jme3.math.FastMath;

public class Scalarf
{
    public double x;
	
	public Scalarf(double x)
	{
		this.x = x;
	}
    
	public Scalarf() {}
    
    // Interpolate between two values linearly
	public void lerp(double other, double amount)
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
	public double direction(double other)
	{
		return Math.signum(other - this.x);
	}
    
    // The absolute difference
    public double difference(double other)
	{
        return Math.abs(other - this.x);
	}
}
