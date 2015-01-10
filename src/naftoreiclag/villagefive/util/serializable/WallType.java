/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import com.jme3.scene.Geometry;
import naftoreiclag.villagefive.util.math.Polygon;
import org.json.simple.JSONObject;

public abstract class WallType
{
    public static WallType from(JSONObject obj)
    {
        return null;
    }
    
    public Geometry produce(Polygon polygon)
    {
        return null;
        
    }
}
