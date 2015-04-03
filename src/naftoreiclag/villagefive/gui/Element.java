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
    
    public Element(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    // Where is this stored?
    protected SpritePlane plane;
    
    // Whenever this element's location is updated, all of these elements will update too, as if they were attached.
    private List<Element> attachedElements = new ArrayList<Element>();
    
    // This only makes handling image transforms a bit easier. It is not affected by the parent element.
    protected Vec2 origin = new Vec2(0, 0);
    
    // Pointer to the element to which this is attached.
    private Element parentElement = null;
    
    // Local transform. This is used to calculate the absolute transform from the parent's absolute transform.
    private Vec2 localLoc = new Vec2(0, 0);
    private Vec2 localScale = new Vec2(1, 1);
    
    // Absolute transform. This is used to calculate each attached element's absolute transform.
    protected Vec2 absLoc = new Vec2(0, 0);
    protected Vec2 absScale = new Vec2(1, 1);

    // Z-value on a 2d plane.
    protected double depth;
    
    // Change the layer/depth
    public void setLayer(int layer) {
        this.depth = layer * SpritePlane.epsilonDiff;

        // Re-sort
        if(this.plane.elements.contains(this)) {
            this.plane.elements.remove(this);
        }
        this.plane.elements.add(this);

        updateSpatial();
    }
    
    // Used to optimize "raycasting"
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

    // Return true if this absolute point collides with you.
    public abstract boolean collides(Vec2 absPoint);
    
    // Used by non-point things
    public final double width;
    public final double height;

    public void attachElement(Element element) {
        this.attachedElements.add(element);

        element.parentElement = this;
        element.updateTransform();
    }

    // Calculate position based on the transform of the parent element
    private void calculateAbsTransform() {
        if(parentElement != null) {
            this.absLoc = this.localLoc.mult(parentElement.absScale).add(parentElement.absLoc);
            this.absScale = this.localScale.mult(parentElement.absScale);
        } else {
            this.absLoc = this.localLoc;
            this.absScale = this.localScale;
        }
        updateSpatial();
    }

    // Whenever localLoc or localScale is changed, call this to update the child elements.
    private void updateTransform() {
        calculateAbsTransform();
        for(Element e : attachedElements) {
            e.updateTransform();
        }
    }

    // Called whenever the spatial (if it has one) should be updated. i.e. whenever calculateAbsTransform() is called.
    public abstract void updateSpatial();

    public void setOrigin(double x, double y) {
        this.origin.set(x, y);
        updateTransform();
    }

    public void setOrigin(Vec2 newLoc) {
        this.origin.set(newLoc);
        updateTransform();
    }
    
    // Set the origin to be in the middle of the bounding box (Called upon creation)
    public final void setOriginMid() {
        this.origin.set(width / 2d, height / 2d);
        updateTransform();
    }

    public void setLoc(double x, double y) {
        this.localLoc.set(x, y);
        updateTransform();
    }

    public void setLoc(Vec2 newLoc) {
        this.localLoc.set(newLoc);
        updateTransform();
    }
    
    
    public void setScale(double scale) {
        this.localScale.set(scale, scale);
        updateTransform();
    }
    public void setScale(double x, double y) {
        this.localScale.set(x, y);
        updateTransform();
    }
    public void setScale(Vec2 scale) {
        this.localScale.set(scale);
        updateTransform();
    }
    public void setWidth(double width) {
        this.localScale.setX(width / this.width);
    }
    public void setHeight(double height) {
        this.localScale.setY(height / this.height);
    }
    public void setWidthKeepRatio(double width) {
        this.localScale.set(width / this.width);
    }
    public void setHeightKeepRatio(double height) {
        this.localScale.set(height / this.height);
    }
    
    // Get the given point's coordinates as expressed as an offset from my origin
    public Vec2 transLocal(Vec2 abs) {
        return abs.subtract(absLoc.subtract(origin));
    }
}
