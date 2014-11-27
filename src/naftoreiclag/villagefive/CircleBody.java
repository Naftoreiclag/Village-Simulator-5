/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;

public class CircleBody
{
    public final Vector2f loc = new Vector2f();
    public final Vector2f mot = new Vector2f();

    private Spatial link;
    
    public void linkToSpatial(Spatial spatial)
    {
        this.link = spatial;
    }
    
    public void onLocationChange()
    {
        System.out.println(loc);
        link.setLocalTranslation(loc.x, 0, loc.y);
    }
}
