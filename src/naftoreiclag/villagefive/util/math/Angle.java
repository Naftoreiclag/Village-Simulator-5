/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

// Do not trust any input
public class Angle
{
    public static double PI = Math.PI;
    public static double TWO_PI = PI * 2d;
    public static double HALF_PI = PI / 2d;
    
	private double x;
    public double getX() { return this.x; }
    public float getXF() { return (float) this.getX(); }
    public void setX(double x) { this.x = x; }
	
	public Angle(double x)
	{
		this.x = wrap(x);
	}
    
	public Angle() {}
    
    public com.jme3.math.Quaternion toQuaternion()
    {
        return (new com.jme3.math.Quaternion()).fromAngleAxis((float) this.getX(), com.jme3.math.Vector3f.UNIT_Y);
    }
    
    // Interpolate between two values linearly
	public void lerpLocal(double other, double amount)
	{
        // If we are already within range
		if(calcDiff(other) < amount)
		{
            // Then just be there already
			this.setX(wrap(other));
		}
		else
		{
            // Try move linearly
			this.setX(wrap(this.getX() + (amount * calcDir(other))));
		}
	}
    
    // The direction (clockwise or counter-clockwise) to get to another angle
	public final double calcDir(double other)
	{
        other = wrap(other);
        
		double displacement = other - this.getX();
		double dirSign = Math.signum(displacement);
        
        // If the distance with the given direction is reflex (ie > 180 degrees)
        if(displacement * dirSign > PI)
        {
            // Then flip the direction around, since the other way is obviously faster (acute)
            dirSign = -dirSign;
        }
		
		return dirSign;
	}
    
    // The measure of the angle formed with this and another angle (unsigned)
    public final double calcDiff(double other)
	{
        other = wrap(other);
        
		double diff = Math.abs(other - this.getX());
        
        // If the angle with the given direction is reflex (ie > 180 degrees)
        if(diff > PI)
        {
            // Then it can't be the shortest. It must be the other way around.
            return TWO_PI - diff;
        }
        
        return diff;
	}

    // Careful when using this, I actually dont know what it does
    public double calcSignedDiff(double other)
    {
        return this.calcDiff(other) * this.calcDir(other);
    }
    
    // Makes sure that this double is between 0 to tau
    public static double wrap(double a)
    {
        return ((a % TWO_PI) + TWO_PI) % TWO_PI;
    }
    
    // Like regular modulus, but it "wraps around" (e.g. magicmod(-1, 5) = 4)
    public static double magicModulus(double a, double b)
    {
        return ((a % b) + b) % b;
    }
    
    @Override
    public Angle clone()
    {
        return new Angle(this.getX());
    }

    public Angle inverseLocal()
    {
        this.setX(-this.getX());
        
        return this;
    }
    public Angle inverse()
    {
        return this.clone().inverseLocal();
    }

    public Vec2 toNormalVec()
    {
        return new Vec2(Math.sin(this.x), Math.cos(this.x));
    }

}

