/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.math.Polygon;

public class PitWall extends WallType
{
    @Override
    public Spatial makeInside(Polygon polygon)
    {
        Node node = new Node();
        
        return node;
    }

    @Override
    public Spatial makeOutside(Polygon polygon)
    {
        return null;
    }
}
