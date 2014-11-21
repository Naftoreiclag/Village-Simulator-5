/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.collision;

import naftoreiclag.villagefive.util.Vector2f;

public class Line
{
    public Vector2f a;
    public Vector2f b;

    public Line(Vector2f a, Vector2f b)
    {
        this.a = a;
        this.b = b;
    }

    public Line(float ax, float ay, float bx, float by)
    {
        this(new Vector2f(ax, ay), new Vector2f(bx, by));
    }
}
