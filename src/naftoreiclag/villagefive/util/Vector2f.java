/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

// Version 19

import com.jme3.math.FastMath;

public class Vector2f
{
    public static Vector2f getNormalFromAngle(float angle)
    {
        return new Vector2f(FastMath.cos(angle), FastMath.sin(angle));
    }
    
    // TODO: add some vector pool or something for recycling of Vector2d's
    
    public float a;
    public float b;
    
    public Vector2f(float a, float b)
    {
        this.a = a;
        this.b = b;
    }
    
    public Vector2f()
    {
        this(0.0f, 0.0f);
    }
    
    @Override
    public Vector2f clone()
    {
        return new Vector2f(this.a, this.b);
    }
    
    public Vector2f setZero()
    {
        this.a = 0;
        this.b = 0;
        
        return this;
    }
    
    public boolean isZero()
    {
        return a == 0 && b == 0;
    }
    
    public Vector2f addLocal(Vector2f other)
    {
        this.a += other.a;
        this.b += other.b;
        
        return this;
    }
    
    public Vector2f addLocalMultiplied(Vector2f other, float multiplicant)
    {
        this.a += other.a * multiplicant;
        this.b += other.b * multiplicant;
        
        return this;
    }
    
    public Vector2f add(Vector2f other)
    {
        return this.clone().addLocal(other);
    }
    
    public Vector2f addLocal(float a)
    {
        this.a += a;
        this.b += a;
        
        return this;
    }
    
    public Vector2f add(float a)
    {
        return this.clone().addLocal(a);
    }
    
    public Vector2f subtractLocal(Vector2f other)
    {
        this.a -= other.a;
        this.b -= other.b;
        
        return this;
    }
    
    public Vector2f subtract(Vector2f other)
    {
        return this.clone().subtractLocal(other);
    }
    
    public Vector2f subtractLocal(float a)
    {
        this.a -= a;
        this.b -= a;
        
        return this;
    }
    
    public Vector2f subtract(float a)
    {
        return this.clone().subtractLocal(a);
    }
    
    public Vector2f multiplyLocal(float a)
    {
        this.a *= a;
        this.b *= a;
        
        return this;
    }
    
    public Vector2f multiply(float a)
    {
        return this.clone().multiplyLocal(a);
    }
    
    public Vector2f divideLocal(float a)
    {
        this.a /= a;
        this.b /= a;
        
        return this;
    }
    
    public Vector2f divide(float a)
    {
        return this.clone().divideLocal(a);
    }
    
    public Vector2f inverseLocal()
    {
        this.a = -this.a;
        this.b = -this.b;
        
        return this;
    }
    
    public Vector2f inverse()
    {
        return this.clone().inverseLocal();
    }
    
    public float distanceSquared(Vector2f other)
    {
        return ((this.a - other.a) * (this.a - other.a)) + ((this.b - other.b) * (this.b - other.b));
    }
    
    public float distanceSquared(float a, float b)
    {
        return ((this.a - a) * (this.a - a)) + ((this.b - b) * (this.b - b));
    }
    
    public float magnitudeSquared()
    {
        return (this.a * this.a) + (this.b * this.b);
    }
    
    public float dotProduct(Vector2f other)
    {
        return (this.a * other.a) + (this.b * other.b);
    }
    
    public float crossProduct(Vector2f other)
    {
        return (this.a * other.b) - (this.b * other.a);
    }

    public Vector2f normalizeLocal()
    {
        return this.divideLocal(FastMath.sqrt(this.magnitudeSquared()));
    }

    public Vector2f normalize()
    {
        return this.clone().normalizeLocal();
    }
    
    // Vector 90 degrees counter-clockwise from this one
    public Vector2f perpendicularLocal()
    {
        float oldA = a;
        a = b;
        b = -oldA;
        
        return this;
    }
    
    public Vector2f perpendicular()
    {
        return this.clone().perpendicularLocal();
    }

    public float angleTo(Vector2f loc)
    {
        float ang = FastMath.atan2(loc.b - this.b, loc.a - this.a);
        
        return ang > 0 ? ang : ang + FastMath.PI * 2;
    }
}
