/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;


import java.io.Serializable;

// juicy beans
public class Plot implements Serializable
{
    private double x, z;
    private double angle;
    
    private int width, height;

    public double getX() { return x; }
    public double getZ() { return z; }
    public double getAngle() { return angle; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public void setX(double x) { this.x = x; }
    public void setZ(double z) { this.z = z; }
    public void setAngle(double angle) { this.angle = angle; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
}
