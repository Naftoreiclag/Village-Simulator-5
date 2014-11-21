/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.collision;

import naftoreiclag.villagefive.util.Vector2f;

public class Circle
{
    public Vector2f loc;
    public final float rad;
    public final float radsq;
    public final float pushResistance;
    public Vector2f motion = new Vector2f();

    public Circle(Vector2f loc, float rad, float strength, float weight)
    {
        this.loc = loc;
        this.rad = rad;
        this.radsq = rad * rad;
        this.pushResistance = weight;
    }

    public Circle(float x, float y, float rad, float strength, float weight)
    {
        this(new Vector2f(x, y), rad, strength, weight);
    }
}