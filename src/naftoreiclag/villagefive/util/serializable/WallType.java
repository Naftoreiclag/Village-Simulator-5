/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.math.Polygon;

public abstract class WallType
{
    public abstract Spatial makeInside(Polygon polygon);
    
    public abstract Spatial makeOutside(Polygon polygon);
}
