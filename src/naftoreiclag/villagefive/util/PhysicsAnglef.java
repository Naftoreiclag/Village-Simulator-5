/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import com.jme3.math.FastMath;
import static naftoreiclag.villagefive.util.Anglef.normalize;

// T
public class PhysicsAnglef
{
    // Displacement from origin
	public float x;
    
    // Velocity
    public float v = 0;
    
    // Acceleration
    public float a = 0;
    
    // Maximum acceleration
    public float maxA = 0.1f;
    
    // Maximum velocity
    public float maxV = 1.0f;
    
    // Target displacement
    public float tarX;
	
	public PhysicsAnglef(float x)
	{
		this.x = x;
	}
    
	public PhysicsAnglef() {}
    
    public void tick(float tpf)
    {
        v += a * tpf;
        if(v > maxV)
        {
            v = maxV;
        }
        else if(v < -maxV)
        {
            v = -maxV;
        }
        x += v * tpf;
        
        float dist = distance();
        float dir = direction();
        
        a = (maxA * dir);
    }
    
    // The direction of the force needed to get to a target X
	public float direction()
	{
		float displacement = this.tarX - this.x;
		float dirSign = Math.signum(displacement);
        
        // If the distance with the given direction is obtuse
        if(displacement * dirSign > FastMath.PI)
        {
            // Then flip the direction around, since the other way is obviously faster (acute)
            dirSign = -dirSign;
        }
		
		return dirSign;
	}
    
    // The shortest distance to targetX
    public float distance()
	{
		float displacement = FastMath.abs(this.tarX - this.x);
        
        // If the distance with the given direction is obtuse
        if(displacement > FastMath.PI)
        {
            return FastMath.TWO_PI - displacement;
        }
        
        return displacement;
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

