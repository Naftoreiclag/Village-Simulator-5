/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.collision;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.util.Vector2f;

public class Space
{
    // The objects which reside in this space
    public List<Circle> circles = new ArrayList<Circle>();
    public List<Line> lines = new ArrayList<Line>();

    // Increment physics simulation by a certain amount
    public void simulate(long delta)
    {
        delta = 1L;

        for(Circle circle : circles)
        {
            simulateCircle(circle, delta);
        }
    }

    // I had to use uppercase letters here to avoid (more) confusion
    public void simulateCircle(Circle circle, long delta)
    {
        // Move it
        circle.loc.addLocalMultiplied(circle.motion, delta);

        // Remember whether the current state is suspected to be dirty (circles in illegal positions)
        boolean suspectedDirty = true;

        // Repeat until it is proven clean
        while(suspectedDirty)
        {
            // If nothing illegal is found on the circle, then this value will stay false.
            suspectedDirty = false;

            // Check relation of this circle to surrounding lines.
            for(Line line : lines)
            {
                // Horizontal optimization
                if(line.a.b == line.b.b)
                {
                    // ((A to B is left to right) and (Past point B)) or ((B to A is left to right) and (Past point B))
                    if((line.a.a < line.b.a && circle.loc.a > line.b.a) || (line.b.a < line.a.a && circle.loc.a < line.b.a))
                    {
                        continue;
                    }
                    // ((A to B is left to right) and (Past point A)) or ((B to A is left to right) and (Past point A))
                    else if((line.a.a < line.b.a && circle.loc.a < line.a.a) || (line.b.a < line.a.a && circle.loc.a > line.a.a))
                    {
                        // Get the lines between important points
                        Vector2f AC = circle.loc.subtract(line.a);

                        // Get distance squared of AC
                        float AC_distsq = AC.magnitudeSquared();

                        // If it is less than the square of the radius (circle collides with point A)
                        if(AC_distsq <= circle.radsq)
                        {
                            // Move it out of the way somehow
                            circle.loc.subtractLocal(AC).addLocal(AC.divide(FastMath.sqrt(AC_distsq)).multiplyLocal(circle.rad + 0.5f));

                            // Since we moved the circle in question, it's possible it moved into an illegal position
                            suspectedDirty = true;
                            break;
                        }
                    }
                    // Within
                    else
                    {
                        // find the distance
                        float DC_dist = circle.loc.b - line.a.b;

                        // Touching
                        if(Math.abs(DC_dist) <= circle.rad)
                        {
                            // Move it out of the way somehow
                            circle.loc.b += -DC_dist + (Math.signum(DC_dist) * (circle.rad + 0.5f));

                            // Since we moved the circle in question, it's possible it moved into an illegal position
                            suspectedDirty = true;
                            break;
                        }
                    }
                }
                // Vertical optimization
                else if(line.a.b == line.b.b)
                {
                    // ((A to B is top to bottom) and (Past point B)) or ((B to A is top to bottom) and (Past point B))
                    if((line.a.b < line.b.b && circle.loc.b > line.b.b) || (line.b.b < line.a.b && circle.loc.b < line.b.b))
                    {
                        continue;
                    }
                    // ((A to B is top to bottom) and (Past point A)) or ((B to A is top to bottom) and (Past point A))
                    else if((line.a.b < line.b.b && circle.loc.b < line.a.b) || (line.b.b < line.a.b && circle.loc.b > line.a.b))
                    {
                        // Get the lines between important points
                        Vector2f AC = circle.loc.subtract(line.a);

                        // Get distance squared of AC
                        float AC_distsq = AC.magnitudeSquared();

                        // If it is less than the square of the radius (circle collides with point A)
                        if(AC_distsq <= circle.radsq)
                        {
                            // Move it out of the way somehow
                            circle.loc.subtractLocal(AC).addLocal(AC.divide(FastMath.sqrt(AC_distsq)).multiplyLocal(circle.rad + 0.5f));

                            // Since we moved the circle in question, it's possible it moved into an illegal position
                            suspectedDirty = true;
                            break;
                        }
                    }
                    // Within
                    else
                    {
                        // find the distance
                        float DC_dist = circle.loc.a - line.a.a;

                        // Touching
                        if(Math.abs(DC_dist) <= circle.rad)
                        {
                            // Move it out of the way somehow
                            circle.loc.a += -DC_dist + (Math.signum(DC_dist) * (circle.rad + 0.5f));

                            // Since we moved the circle in question, it's possible it moved into an illegal position
                            suspectedDirty = true;
                            break;
                        }
                    }
                }

                // Line is not perfectly horizontal or vertical, so we must do this the hard way

                // Get the lines between important points
                Vector2f AC = circle.loc.subtract(line.a);
                Vector2f AB = line.b.subtract(line.a);

                // Find the length of AB (the line)
                float AB_distsq = (AB.a * AB.a) + (AB.b * AB.b);

                // See if we can get away with a cheaper check by using the dot product
                float AC_dot_AB = (AC.a * AB.a) + (AC.b * AB.b);
                float fractonOfDC = AC_dot_AB / AB_distsq; // This is a value that expresses D as a point on line AB where D is the closest point to C

                // Past point B
                if(fractonOfDC > 1)
                {
                    continue;
                }
                // Past point A
                else if(fractonOfDC < 0)
                {
                    // Get distance squared of AC
                    float AC_distsq = AC.magnitudeSquared();

                    // If it is less than the square of the radius (circle collides with point A)
                    if(AC_distsq <= circle.radsq)
                    {
                        // Move it out of the way somehow
                        circle.loc.subtractLocal(AC).addLocal(AC.divide(FastMath.sqrt(AC_distsq)).multiplyLocal(circle.rad + 0.5f));

                        // Since we moved the circle in question, it's possible it moved into an illegal position
                        suspectedDirty = true;
                        break;
                    }
                }
                // Within the line
                else
                {
                    // See if the distance between D and C is less than the radius
                    Vector2f D = line.a.add(AB.multiply(fractonOfDC));
                    Vector2f DC = circle.loc.subtract(D);
                    float DC_distsq = DC.magnitudeSquared();
                    if(DC_distsq <= circle.radsq)
                    {
                        // Move it out of the way somehow
                        circle.loc.subtractLocal(DC).addLocal(DC.divide(FastMath.sqrt(DC_distsq)).multiplyLocal(circle.rad + 0.5f));

                        // Since we moved the circle in question, it's possible it moved into an illegal position
                        suspectedDirty = true;
                        break;
                    }
                }
            }

            for(Circle otherCircle : circles)
            {
                if(circle == otherCircle)
                {
                    continue;
                }

                Vector2f DC = circle.loc.subtract(otherCircle.loc);
                float DC_distsq = DC.magnitudeSquared();

                if(DC_distsq < (circle.rad + otherCircle.rad) * (circle.rad + otherCircle.rad))
                {
                    // Other circle is totally immobile
                    if(otherCircle.pushResistance == -1)
                    {
                        // Move it out of the way somehow
                        circle.loc.subtractLocal(DC).addLocal(DC.divide(FastMath.sqrt(DC_distsq)).multiplyLocal(circle.rad + otherCircle.rad + 0.5f));

                        // Since we moved the circle in question, it's possible it moved into an illegal position
                        suspectedDirty = true;
                        break;
                    }
                    // Other circle has no resistance
                    else if(otherCircle.pushResistance == 0)
                    {
                        // Move other one out of the way somehow
                        otherCircle.motion.subtractLocal(DC.divide(FastMath.sqrt(DC_distsq)).multiplyLocal(circle.rad + otherCircle.rad + 0.5f)).addLocal(DC);

                        // Since we moved the other one, we need to make sure it's new position is not dirty.
                        simulateCircle(otherCircle, delta);
                    }
                    // Both circles have finite strength/resistance
                    // To make pushing non-insane to do, there is no "sliding" between pushable circles.
                    else
                    {
                        // 
                        float relativeResistance = 1;

                        Vector2f impulse = DC.divide(FastMath.sqrt(DC_distsq));

                        // Move it out of the way somehow
                        circle.loc.addLocal(impulse.multiply(circle.rad + otherCircle.rad + 0.5f).multiplyLocal(relativeResistance)).subtractLocal(DC);

                        // Since we moved the circle in question, it's possible it moved into an illegal position
                        suspectedDirty = true;

                        // Move other one out of the way somehow
                        otherCircle.motion.subtractLocal(impulse.multiply(circle.rad + otherCircle.rad + 0.5f).multiplyLocal(relativeResistance)).addLocal(DC);

                        // Since we moved the other one, we need to make sure it's new position is not dirty.
                        simulateCircle(otherCircle, delta);

                        break;
                    }
                }
            }
        }

        circle.motion.setZero();
    }
}
