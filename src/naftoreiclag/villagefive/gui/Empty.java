/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.math.Vec2;

public class Empty extends Element {
    
    public Empty() {
        super(1, 1);
    }

    @Override
    public boolean collides(Vec2 absPoint) {
        return false;
    }

    @Override
    public void updateSpatial() {
    }

    @Override
    public boolean hasSpatial() {
        return false;
    }

    @Override
    protected Spatial getSpatial() {
        return null;
    }
}
