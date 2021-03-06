/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

// Class adapter

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

// Universal 2d vector
public class Vec2 implements JSONAware
{
    public static final Vec2 ZERO = new Vec2(0, 0);
    public static final Vec2 ONE = new Vec2(1, 1);
    public static final org.dyn4j.geometry.Vector2 ZERO_DYN4J = new org.dyn4j.geometry.Vector2(0, 0);
    
	private double x;
	private double y;

    public double getX() { return this.x; }
    public float getXF() { return (float) this.getX(); }
    public int getXI() { return (int) this.getX(); }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return this.y; }
    public float getYF() { return (float) this.getY(); }
    public int getYI() { return (int) this.getY(); }
    public void setY(double y) { this.y = y; }
	
    // Adapters
    public Vec2(org.dyn4j.geometry.Vector2 strange) {
        this(strange.x, strange.y);
    }
    public Vec2(com.jme3.math.Vector3f strange) {
        this(strange.x, strange.z);
    }
    public Vec2(com.jme3.math.Vector2f strange) {
        this(strange.x, strange.y);
    }
    public Vec2(Angle angle) {
        this.x = Math.cos(angle.getX());
        this.y = Math.sin(angle.getX());
    }
    public org.dyn4j.geometry.Vector2 toDyn4j() {
        return new org.dyn4j.geometry.Vector2(this.getX(), this.getY());
    }
    public com.jme3.math.Vector3f toJmeVec3() {
        return new com.jme3.math.Vector3f(this.getXF(), 0f, this.getYF());
    }
    public com.jme3.math.Vector2f toJmeVec2() {
        return new com.jme3.math.Vector2f(this.getXF(), this.getYF());
    }

    // Default constructor
    public Vec2() {
        this(0.0d, 0.0d);
    }

    // Optional
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Optional
    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vec2 clone() {
        return new Vec2(this.getX(), this.getY());
    }

    // Addition
    public Vec2 addLocal(Vec2 other) {
        this.setX(this.getX() + other.getX());
        this.setY(this.getY() + other.getY());

        return this;
    }
    public Vec2 add(Vec2 other) {
        return this.clone().addLocal(other);
    }

    public Vec2 addLocal(double xy) {
        this.setX(this.getX() + xy);
        this.setY(this.getY() + xy);

        return this;
    }
    public Vec2 add(double xy) {
        return this.clone().addLocal(xy);
    }

    public Vec2 addLocal(double x, double y) {
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);

        return this;
    }
    public Vec2 add(double x, double y) {
        return this.clone().addLocal(x, y);
    }

    public Vec2 addLocal(float xy) {
        this.setX(this.getX() + xy);
        this.setY(this.getY() + xy);

        return this;
    }
    public Vec2 add(float xy) {
        return this.clone().addLocal(xy);
    }

    public Vec2 addLocal(float x, float y) {
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);

        return this;
    }
    public Vec2 add(float x, float y) {
        return this.clone().addLocal(x, y);
    }

    // Subtraction
    public Vec2 subtractLocal(Vec2 other) {
        this.setX(this.getX() - other.getX());
        this.setY(this.getY() - other.getY());

        return this;
    }
    public Vec2 subtract(Vec2 other) {
        return this.clone().subtractLocal(other);
    }

    public Vec2 subtractLocal(double xy) {
        this.setX(this.getX() - xy);
        this.setY(this.getY() - xy);

        return this;
    }
    public Vec2 subtract(double xy) {
        return this.clone().subtractLocal(xy);
    }

    public Vec2 subtractLocal(double x, double y) {
        this.setX(this.getX() - x);
        this.setY(this.getY() - x);

        return this;
    }
    public Vec2 subtract(double x, double y) {
        return this.clone().subtractLocal(x, y);
    }

    public Vec2 subtractLocal(float xy) {
        this.setX(this.getX() - xy);
        this.setY(this.getY() - xy);

        return this;
    }
    public Vec2 subtract(float xy) {
        return this.clone().subtractLocal(xy);
    }

    public Vec2 subtractLocal(float x, float y) {
        this.setX(this.getX() - x);
        this.setY(this.getY() - x);

        return this;
    }
    public Vec2 subtract(float x, float y) {
        return this.clone().subtractLocal(x, y);
    }

    // Multiplication
    public Vec2 multLocal(double xy) {
        this.setX(this.getX() * xy);
        this.setY(this.getY() * xy);

        return this;
    }
    public Vec2 mult(double xy) {
        return this.clone().multLocal(xy);
    }

    public Vec2 multLocal(float xy) {
        this.setX(this.getX() * xy);
        this.setY(this.getY() * xy);

        return this;
    }
    public Vec2 mult(float xy) {
        return this.clone().multLocal(xy);
    }
    
    public Vec2 multLocal(Vec2 xy) {
        this.setX(this.getX() * xy.x);
        this.setY(this.getY() * xy.y);

        return this;
    }
    public Vec2 mult(Vec2 xy) {
        return this.clone().multLocal(xy);
    }

    // Division
    public Vec2 divideLocal(double xy) {
        this.setX(this.getX() / xy);
        this.setY(this.getY() / xy);

        return this;
    }
    public Vec2 divide(double xy) {
        return this.clone().divideLocal(xy);
    }

    public Vec2 divideLocal(float xy) {
        this.setX(this.getX() / xy);
        this.setY(this.getY() / xy);

        return this;
    }
    public Vec2 divide(float xy) {
        return this.clone().divideLocal(xy);
    }
    
    public Vec2 divideLocal(Vec2 xy) {
        this.setX(this.getX() / xy.getX());
        this.setY(this.getY() / xy.getY());

        return this;
    }
    public Vec2 divide(Vec2 xy) {
        return this.clone().divideLocal(xy);
    }

    // Chunk stuff
    public Vec2 gridLocal(double width, double height) {
        this.x = x / width;
        this.y = y / height;

        if(this.x < 0) {
            --this.x;
        }
        if(this.y < 0) {
            --this.y;
        }

        this.floorLocal();

        return this;
    }
    public Vec2 grid(double width, double height) {
        return this.clone().gridLocal(width, height);
    }

    // Rounding
    public Vec2 floorLocal() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);

        return this;
    }
    public Vec2 floor() {
        return this.clone().floorLocal();
    }

    public Vec2 ceilLocal() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);

        return this;
    }
    public Vec2 ceil() {
        return this.clone().ceilLocal();
    }

    public Vec2 roundLocal() {
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);

        return this;
    }
    public Vec2 round() {
        return this.clone().roundLocal();
    }

    // Other
    public Vec2 inverseLocal() {
        this.setX(-this.getX());
        this.setY(-this.getY());

        return this;
    }
    public Vec2 inverse() {
        return this.clone().inverseLocal();
    }

    public double distSq(Vec2 other) {
        return ((this.getX() - other.getX()) * (this.getX() - other.getX())) + ((this.getY() - other.getY()) * (this.getY() - other.getY()));
    }
    public float distSqF(Vec2 other) {
        return (float) this.distSq(other);
    }
    public double dist(Vec2 other) {
        return Math.sqrt(this.distSq(other));
    }
    public float distF(Vec2 other) {
        return (float) this.dist(other);
    }

    public double lenSq() {
        return (this.getX() * this.getX()) + (this.getY() * this.getY());
    }
    public float lenSqF() {
        return (float) this.lenSq();
    }
    public double len() {
        return Math.sqrt(this.lenSq());
    }
    public float lenF() {
        return (float) this.len();
    }

    public double dot(Vec2 other) {
        return (this.getX() * other.getX()) + (this.getY() * other.getY());
    }
    public float dotF(Vec2 other) {
        return (float) this.dot(other);
    }

    public double crossProduct(Vec2 other) {
        return (this.getX() * other.getY()) - (this.getY() * other.getX());
    }
    public float crossProductF(Vec2 other) {
        return (float) this.crossProduct(other);
    }

    public Vec2 normalizeLocal() {
        if(x == 0 && y == 0) {
            return Vec2.ZERO.clone();
        }

        return this.divideLocal(Math.sqrt(this.lenSqF()));
    }
    public Vec2 normalize() {
        return this.clone().normalizeLocal();
    }

    // Vector 90 degrees counter-clockwise from this one
    public Vec2 perpendicularLocal() {
        double oldA = getX();
        setX(getY());
        setY(-oldA);

        return this;
    }
    public Vec2 perpendicular() {
        return this.clone().perpendicularLocal();
    }

    @Deprecated
    public double angleTo(Vec2 loc) {
        double ang = Math.atan2(loc.getY() - this.getY(), loc.getX() - this.getX());

        return ang > 0 ? ang : ang + Math.PI * 2;
    }

    @Override
    public String toString() {
        return "Vector2d [" + getX() + ", " + getY() + "]";
    }

    public Angle getAngle() {
        return new Angle(Math.atan2(this.getY(), this.getX()));
    }

    public String toJSONString() {
        JSONObject obj = new JSONObject();

        obj.put("x", this.getX());
        obj.put("y", this.getY());

        return obj.toJSONString();
    }

    public Vec2(JSONObject data) {
        this.x = (Double) data.get("x");
        this.y = (Double) data.get("y");
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void set(float xy) {
        this.x = xy;
        this.y = xy;
    }
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void set(double xy) {
        this.x = xy;
        this.y = xy;
    }
    public void set(Vec2 mirror) {
        this.x = mirror.x;
        this.y = mirror.y;
    }
    public void set(com.jme3.math.Vector2f mirror) {
        this.x = mirror.x;
        this.y = mirror.y;
    }

    public void debug() {
        System.out.println(this);
    }
}
