/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

import naftoreiclag.villagefive.util.math.Vec2;

public class Collision extends Element
{
    public Collision(double width, double height)
    {
        this.width = width;
        this.height = height;
        this.setOriginMid();
    }

    @Override
    public void updateSpatial()
    {
    }

    @Override
    public boolean collides(Vec2 absPoint)
    {
        Vec2 foo = this.transLocal(absPoint);
        
        return foo.getX() >= 0 && foo.getX() < width && foo.getY() >= 0 && foo.getY() < height;
    }

}
