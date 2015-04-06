/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

public class Anchor extends Empty {
    
    final double percentX;
    final double percentY;
    
    public Anchor(double percentX, double percentY) {
        super();
        
        this.percentX = percentX;
        this.percentY = percentY;
    }
    
    @Override
    protected void calculateAbsTransform() {
        
        this.absScale = this.localScale;
        
        if(plane != null) {
            this.absLoc.set(plane.width * percentX, plane.height * percentY);
        }
        this.absLoc.addLocal(this.localLoc.mult(this.absScale));
        
        updateSpatial();
    }
    

}
