/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import naftoreiclag.villagefive.util.math.Vec2;

public class SpritePlane
{
    // All elements in order of depth
    protected SortedSet<Element> elements;
    
    private boolean geoStateNeedsUpdating = true;
    
    // Haxxy fix for having duplicate depths in the SortedSet
    public static double epsilonDiff = 0.00001f;
    double epsilon = 0d;
    
    public int width;
    public int height;
    
    ViewPort viewPort;
    Node rootNode;
    public SpritePlane(ViewPort viewPort)
    {
        this.viewPort = viewPort;
        rootNode = new Node();
        rootNode.updateGeometricState();
        this.viewPort.attachScene(rootNode);
        elements = new TreeSet<Element>(Element.depthCompare);
        
        this.width = viewPort.getCamera().getWidth();
        this.height = viewPort.getCamera().getHeight();
    }
    
    
    
    public void attachElement(Element element)
    {
        element.setPlane(this);
        element.setDepth(element.depth + epsilon);
        epsilon += epsilonDiff;
        elements.add(element);
        
        if(element instanceof Sprite)
        {
            Sprite sprite = (Sprite) element;
            
            rootNode.attachChild(sprite.picture);
            needUpdate();
        }
    }
    
    public void needUpdate()
    {
        geoStateNeedsUpdating = true;
        
            rootNode.updateGeometricState();
    }
    
    public void tick(float tpf)
    {
        if(geoStateNeedsUpdating)
        {
            //rootNode.updateGeometricState();
            geoStateNeedsUpdating = false;
        }
    }
    
    public Element rayCast(Vec2 absLoc)
    {
        Iterator<Element> iter = elements.iterator();
        while(iter.hasNext())
        {
            Element element = iter.next();
            
            if(element.collides(absLoc))
            {
                return element;
            }
        }
        
        return null;
    }
}
