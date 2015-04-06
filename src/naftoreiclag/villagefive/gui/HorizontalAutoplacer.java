/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.math.Vec2;

public class HorizontalAutoplacer extends Empty {
    
    public static enum Placement {
        center,
        left,
        right
    }
    
    public final double padding;
    public final Placement placement;
    
    public HorizontalAutoplacer(double padding, Placement placement) {
        super();
        this.padding = padding;
        this.placement = placement;
    }
    
    @Override
    public void attachElement(Element element) {
        super.attachElement(element);
        
        updateArrangement();
    }
    
    public void updateArrangement() {
        if(attachedElements.isEmpty()) {
            return;
        } else {
           
            
            double xOff = 0;
            if(placement == Placement.left) {
                xOff = 0;
            } else {
                double totalWidth = 0;
                for(Element e : attachedElements) {
                    totalWidth += e.width * e.absScale.getX();
                }
                totalWidth += (attachedElements.size() - 1) * padding * absScale.getX();
                
                if(placement == Placement.right) {
                    xOff = -totalWidth;
                } else if(placement == Placement.center) {
                    xOff = totalWidth * -0.5;
                }
            }
            
            for(Element e : attachedElements) {
                e.setLoc(xOff + (e.getOriginX() * e.getLocalScaleX()), e.getLocalY());
                xOff += e.getLocalScaleX() * e.width;
                xOff += padding * absScale.getX();
            }
        }
    }
}
