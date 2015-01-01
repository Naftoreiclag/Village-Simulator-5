/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

// Universal 2d vector
public class Vec2
{
	private double x;
	private double y;

    public double getX() { return this.x; }
    public float getXf() { return (float) this.getX(); }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return this.y; }
    public float getYf() { return (float) this.getY(); }
    public void setY(double y) { this.y = y; }
	
    // Default constructor
	public Vec2()
	{
		this(0.0d, 0.0d);
	}
	
    // Optional
	public Vec2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
    @Override
	public Vec2 clone()
	{
		return new Vec2(this.getX(), this.getY());
	}
	
    // Addition
	
	public Vec2 addLocal(Vec2 other)
	{
		this.setX(this.getX() + other.getX());
		this.setY(this.getY() + other.getY());
		
		return this;
	}
	public Vec2 add(Vec2 other)
	{
		return this.clone().addLocal(other);
	}
	
	public Vec2 addLocal(double xy)
	{
		this.setX(this.getX() + xy);
		this.setY(this.getY() + xy);
		
		return this;
	}
	public Vec2 add(double xy)
	{
		return this.clone().addLocal(xy);
	}
	
	public Vec2 addLocal(double x, double y)
	{
		this.setX(this.getX() + x);
		this.setY(this.getY() + y);
		
		return this;
	}
	public Vec2 add(double x, double y)
	{
		return this.clone().addLocal(x, y);
	}
    
    
	public Vec2 addLocal(float xy)
	{
		this.setX(this.getX() + xy);
		this.setY(this.getY() + xy);
		
		return this;
	}
	public Vec2 add(float xy)
	{
		return this.clone().addLocal(xy);
	}
	
	public Vec2 addLocal(float x, float y)
	{
		this.setX(this.getX() + x);
		this.setY(this.getY() + y);
		
		return this;
	}
	public Vec2 add(float x, float y)
	{
		return this.clone().addLocal(x, y);
	}
	
    // Subtraction
    
	public Vec2 subtractLocal(Vec2 other)
	{
		this.setX(this.getX() - other.getX());
		this.setY(this.getY() - other.getY());
		
		return this;
	}
	public Vec2 subtract(Vec2 other)
	{
		return this.clone().subtractLocal(other);
	}
	
	public Vec2 subtractLocal(double xy)
	{
		this.setX(this.getX() - xy);
		this.setY(this.getY() - xy);
		
		return this;
	}
	public Vec2 subtract(double xy)
	{
		return this.clone().subtractLocal(xy);
	}
    
	public Vec2 subtractLocal(double x, double y)
	{
		this.setX(this.getX() - x);
		this.setY(this.getY() - x);
		
		return this;
	}
	public Vec2 subtract(double x, double y)
	{
		return this.clone().subtractLocal(x, y);
	}
	
	public Vec2 subtractLocal(float xy)
	{
		this.setX(this.getX() - xy);
		this.setY(this.getY() - xy);
		
		return this;
	}
	public Vec2 subtract(float xy)
	{
		return this.clone().subtractLocal(xy);
	}
    
	public Vec2 subtractLocal(float x, float y)
	{
		this.setX(this.getX() - x);
		this.setY(this.getY() - x);
		
		return this;
	}
	public Vec2 subtract(float x, float y)
	{
		return this.clone().subtractLocal(x, y);
	}
	
    // Multiplication
    
	public Vec2 multLocal(double xy)
	{
		this.setX(this.getX() * xy);
		this.setY(this.getY() * xy);
		
		return this;
	}
	
	public Vec2 mult(double xy)
	{
		return this.clone().multLocal(xy);
	}
    
	public Vec2 multLocal(float xy)
	{
		this.setX(this.getX() * xy);
		this.setY(this.getY() * xy);
		
		return this;
	}
	
	public Vec2 mult(float xy)
	{
		return this.clone().multLocal(xy);
	}
	
    // Division
    
	public Vec2 divideLocal(double a)
	{
		this.setX(this.getX() / a);
		this.setY(this.getY() / a);
		
		return this;
	}
	
	public Vec2 divide(double a)
	{
		return this.clone().divideLocal(a);
	}
    
	public Vec2 divideLocal(float a)
	{
		this.setX(this.getX() / a);
		this.setY(this.getY() / a);
		
		return this;
	}
	
	public Vec2 divide(float a)
	{
		return this.clone().divideLocal(a);
	}
    
    // Other
	
	public Vec2 inverseLocal()
	{
		this.setX(-this.getX());
		this.setY(-this.getY());
		
		return this;
	}
	
	public Vec2 inverse()
	{
		return this.clone().inverseLocal();
	}
	
	public double distSq(Vec2 other)
	{
		return ((this.getX() - other.getX()) * (this.getX() - other.getX())) + ((this.getY() - other.getY()) * (this.getY() - other.getY()));
	}
    
    public double dist(Vec2 other)
    {
        return Math.sqrt(this.distSq(other));
    }
    public float distF(Vec2 other)
    {
        return (float) this.dist(other);
    }
	
	public double lenSq()
	{
		return (this.getX() * this.getX()) + (this.getY() * this.getY());
	}
	public float lenSqF()
	{
		return (float) this.lenSq();
	}
	public double len()
	{
		return Math.sqrt(this.lenSq());
	}
	public float lenF()
	{
		return (float) this.len();
	}
	
	
	public double dotd(Vec2 other)
	{
		return (this.getX() * other.getX()) + (this.getY() * other.getY());
	}
	
	public float dot(Vec2 other)
	{
		return (float) this.dotd(other);
	}
	
	public double crossProduct(Vec2 other)
	{
		return (this.getX() * other.getY()) - (this.getY() * other.getX());
	}

	public Vec2 normalizeLocal()
	{
		return this.divideLocal(Math.sqrt(this.lenSqF()));
	}

	public Vec2 normalize()
	{
		return this.clone().normalizeLocal();
	}
	
	// Vector 90 degrees counter-clockwise from this one
	public Vec2 perpendicularLocal()
	{
		double oldA = getX();
		setX(getY());
		setY(-oldA);
		
		return this;
	}
	
	public Vec2 perpendicular()
	{
		return this.clone().perpendicularLocal();
	}

	public double angleTo(Vec2 loc)
	{
		double ang = Math.atan2(loc.getY() - this.getY(), loc.getX() - this.getX());
		
		return ang > 0 ? ang : ang + Math.PI * 2;
	}
    
    @Override
    public String toString()
    {
        return "Vector2d [" + getX() + ", " + getY() + "]";
    }

    public double getAngled()
    {
        return Math.atan2(getY(), getX());
    }

    public float getAngle()
    {
        return (float) this.getAngled();
    }

}
