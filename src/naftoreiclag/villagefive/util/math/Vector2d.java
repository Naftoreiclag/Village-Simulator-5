/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

// Version 18

public class Vector2d
{
	// TODO: add some vector pool or something for recycling of Vector2d's
	
	public double a;
	public double b;
	
	public Vector2d(double a, double b)
	{
		this.a = a;
		this.b = b;
	}
	
	public Vector2d()
	{
		this(0.0d, 0.0d);
	}
	
	public Vector2d clone()
	{
		return new Vector2d(this.a, this.b);
	}
	
	public Vector2d setZero()
	{
		this.a = 0;
		this.b = 0;
		
		return this;
	}
	
	public boolean isZero()
	{
		return a == 0 && b == 0;
	}
	
	public Vector2d addLocal(Vector2d other)
	{
		this.a += other.a;
		this.b += other.b;
		
		return this;
	}
	
	public Vector2d addLocalMultiplied(Vector2d other, double multiplicant)
	{
		this.a += other.a * multiplicant;
		this.b += other.b * multiplicant;
		
		return this;
	}
	
	public Vector2d add(Vector2d other)
	{
		return this.clone().addLocal(other);
	}
	
	public Vector2d addLocal(double a)
	{
		this.a += a;
		this.b += a;
		
		return this;
	}
	
	public Vector2d add(double a)
	{
		return this.clone().addLocal(a);
	}
	
	public Vector2d subtractLocal(Vector2d other)
	{
		this.a -= other.a;
		this.b -= other.b;
		
		return this;
	}
	
	public Vector2d subtract(Vector2d other)
	{
		return this.clone().subtractLocal(other);
	}
	
	public Vector2d subtractLocal(double a)
	{
		this.a -= a;
		this.b -= a;
		
		return this;
	}
	
	public Vector2d subtract(double a)
	{
		return this.clone().subtractLocal(a);
	}
	
	public Vector2d multiplyLocal(double a)
	{
		this.a *= a;
		this.b *= a;
		
		return this;
	}
	
	public Vector2d multiply(double a)
	{
		return this.clone().multiplyLocal(a);
	}
	
	public Vector2d divideLocal(double a)
	{
		this.a /= a;
		this.b /= a;
		
		return this;
	}
	
	public Vector2d divide(double a)
	{
		return this.clone().divideLocal(a);
	}
	
	public Vector2d inverseLocal()
	{
		this.a = -this.a;
		this.b = -this.b;
		
		return this;
	}
	
	public Vector2d inverse()
	{
		return this.clone().inverseLocal();
	}
	
	public double distanceSquared(Vector2d other)
	{
		return ((this.a - other.a) * (this.a - other.a)) + ((this.b - other.b) * (this.b - other.b));
	}
	
	public double distanceSquared(double a, double b)
	{
		return ((this.a - a) * (this.a - a)) + ((this.b - b) * (this.b - b));
	}
	
	public double magnitudeSquared()
	{
		return (this.a * this.a) + (this.b * this.b);
	}
	
	public double dotProduct(Vector2d other)
	{
		return (this.a * other.a) + (this.b * other.b);
	}
	
	public double crossProduct(Vector2d other)
	{
		return (this.a * other.b) - (this.b * other.a);
	}

	public Vector2d normalizeLocal()
	{
		return this.divideLocal(Math.sqrt(this.magnitudeSquared()));
	}

	public Vector2d normalize()
	{
		return this.clone().normalizeLocal();
	}
	
	// Vector 90 degrees counter-clockwise from this one
	public Vector2d perpendicularLocal()
	{
		double oldA = a;
		a = b;
		b = -oldA;
		
		return this;
	}
	
	public Vector2d perpendicular()
	{
		return this.clone().perpendicularLocal();
	}

	public double angleTo(Vector2d loc)
	{
		double ang = Math.atan2(loc.b - this.b, loc.a - this.a);
		
		return ang > 0 ? ang : ang + Math.PI * 2;
	}
    
    @Override
    public String toString()
    {
        return "Vector2d [" + a + ", " + b + "]";
    }
}
