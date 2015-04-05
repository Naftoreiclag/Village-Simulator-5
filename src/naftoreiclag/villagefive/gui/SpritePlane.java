/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.gui;

import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import naftoreiclag.villagefive.util.math.Vec2;

// This class is to make it easier to draw in 2D.
// Essentially, this is a replacement for Nifty that sacrifices xml loading and common gui elements for tighter control.

// Hi, future me! Are you trying to understand this code? THEN GOOD LUK NOOB!!!

public class SpritePlane {
    // All elements in order of depth
    protected SortedSet<Element> elements;
    
    // If this is true, then the geometric state needs an update.
    private boolean geoStateNeedsUpdating = true;
    
    // This is the difference in z value when calculating layering.
    public static double epsilonDiff = 0.00001d;
    int lastLayer = 0;
    public int width;
    public int height;
    ViewPort viewPort;
    Node rootNode;

    public SpritePlane(ViewPort viewPort) {
        this.viewPort = viewPort;
        rootNode = new Node();
        rootNode.updateGeometricState();
        this.viewPort.attachScene(rootNode);
        elements = new TreeSet<Element>(Element.depthCompare);

        this.width = viewPort.getCamera().getWidth();
        this.height = viewPort.getCamera().getHeight();
    }

    public void attachElement(Element element) {
        
        if(element.plane == this) {
            return;
        }
        
        if(element.plane != null) {
            element.plane.removeElement(element);
        }
        
        element.plane = this;
        element.setLayer(lastLayer);
        lastLayer ++;
        elements.add(element);

        if(element.hasSpatial()) {
            rootNode.attachChild(element.getSpatial());
            needUpdate();
        }
        
    }

    public void removeElement(Element element) {
        
        if(element == null) {
            return;
        }
        
        // Remove all sub-elements
        for(Element e : element.attachedElements) {
            this.removeElement(e);
        }

        if(element.hasSpatial()) {
            rootNode.detachChild(element.getSpatial());
            needUpdate();
        }
        
        if(elements.contains(element)) {
            elements.remove(element);
        }
        
        element.plane = null;
    }

    public void needUpdate() {
        geoStateNeedsUpdating = true;

        rootNode.updateGeometricState();
    }

    public void tick(float tpf) {
        if(geoStateNeedsUpdating) {
            //rootNode.updateGeometricState();
            geoStateNeedsUpdating = false;
        }
    }

    public Element pick(Vec2 absLoc) {
        Iterator<Element> iter = elements.iterator();
        while(iter.hasNext()) {
            Element element = iter.next();

            if(element.collides(absLoc)) {
                return element;
            }
        }

        return null;
    }
}
