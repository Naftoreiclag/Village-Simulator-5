/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import naftoreiclag.villagefive.util.math.Vec2;

/*
 * 2d axis-aligned thingy on a SpritePlane.
 * 
 * Has node-like parenting (following) that can be used to "attach" different element together.
 * 
 */
public abstract class Element {
    private List<Element> followers = new ArrayList<Element>();
    private Element leader = null;
    private Vec2 localLoc = new Vec2(0, 0);

    public void setDepth(double x) {
        this.depth = x;

        // Re-sort
        if(this.plane.elements.contains(this)) {
            this.plane.elements.remove(this);
        }
        this.plane.elements.add(this);

        updateSpatial();
    }

    public abstract boolean collides(Vec2 absPoint);
    public static Comparator<Element> depthCompare = new Comparator<Element>() {
        @Override
        public int compare(Element right, Element left) {
            if(right.depth == left.depth) {
                return 0;
            } else if(right.depth > left.depth) {
                return 1;
            } else {
                return -1;
            }
        }
    };
    protected double depth;
    protected Vec2 absLoc = new Vec2(0, 0);
    protected Vec2 origin = new Vec2(0, 0);
    public SpritePlane plane;
    public double width;
    public double height;

    public void addFollower(Element glue) {
        this.followers.add(glue);

        glue.leader = this;
        glue.updateLoc();
    }

    // Optional form of "addFollower"
    public void follow(Element leader) {
        leader.addFollower(this);
    }

    // "Iterate" though all the followers and sub-followers to match my new position
    private void followLeader() {
        if(leader != null) {
            this.absLoc = this.localLoc.add(leader.absLoc);
        } else {
            this.absLoc = this.localLoc;
        }
        for(Element e : followers) {
            e.followLeader();
        }
        updateSpatial();
    }

    // Called whenver any location vector (i.e. loc, origin,)
    private void updateLoc() {
        // Begin recursive func
        followLeader();
    }

    // Called whenever the spatial (if it has one) should be updated
    public abstract void updateSpatial();

    public void setOrigin(double x, double y) {
        this.origin.set(x, y);
        updateLoc();
    }

    public void setOrigin(float x, float y) {
        this.origin.set(x, y);
        updateLoc();
    }

    public void setOrigin(Vec2 newLoc) {
        this.origin.set(newLoc);
        updateLoc();
    }
    
    // Set the origin to be in the middle of the bounding box (Called upon creation)
    public final void setOriginMid() {
        this.origin.set(width / 2d, height / 2d);
        updateLoc();
    }

    public void setLoc(double x, double y) {
        this.localLoc.set(x, y);
        updateLoc();
    }

    public void setLoc(float x, float y) {
        this.localLoc.set(x, y);
        updateLoc();
    }

    public void setLoc(Vec2 newLoc) {
        this.localLoc.set(newLoc);
        updateLoc();
    }
    
    // Get the given point's coordinates as expressed as an offset from my origin
    public Vec2 transLocal(Vec2 abs) {
        return abs.subtract(absLoc.subtract(origin));
    }

    void setPlane(SpritePlane plane) {
        this.plane = plane;
    }
}
