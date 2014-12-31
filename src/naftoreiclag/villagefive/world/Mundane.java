/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

// so dumb
public abstract class Mundane
{
    public abstract void loadNode();
    protected abstract Node getNode();
    
    public void setLocation(Vector2f loc)
    {
        this.getNode().setLocalTranslation(loc.x, 0f, loc.y);
    }
    public Vector2f getLocation()
    {
        Vector3f loc3f = this.getNode().getLocalTranslation();
        
        return new Vector2f(loc3f.x, loc3f.z);
    }
    public void setLocationRelative(Vector2f loc)
    {
        this.setLocation(this.getLocation().add(loc));
    }
    public void setRotation(float dir)
    {
        this.getNode().setLocalRotation((new Quaternion()).fromAngleAxis(dir, Vector3f.UNIT_Y));
    }
}
