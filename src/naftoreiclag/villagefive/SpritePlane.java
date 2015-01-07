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
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class SpritePlane
{
    // All elements in order of depth
    SortedSet<Element> sprites;
    
    private boolean geoStateNeedsUpdating = true;
    
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
        Comparator<Element> comp = new Comparator<Element>()
        {
            @Override
            public int compare(Element right, Element left)
            {
                if(right.depth == left.depth)
                {
                    return 0;
                }
                else if(right.depth > left.depth)
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }
        };
        sprites = new TreeSet<Element>(comp);
        
        this.width = viewPort.getCamera().getWidth();
        this.height = viewPort.getCamera().getHeight();
    }
    
    public void attach(Sprite sprite)
    {
        sprites.add(sprite);
        rootNode.attachChild(sprite.picture);
        sprite.setPlane(this);
        needUpdate();
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
}
