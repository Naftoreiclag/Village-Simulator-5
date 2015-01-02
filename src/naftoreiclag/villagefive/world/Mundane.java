/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import naftoreiclag.villagefive.util.math.Vec2;

// so dumb
public abstract class Mundane
{
    public abstract void loadNode();
    protected abstract Node getNode();
    
    public void setLocation(Vec2 loc)
    {
        this.getNode().setLocalTranslation(loc.getXF(), 0f, loc.getYF());
    }
    public Vec2 getLocation()
    {
        Vector3f loc3f = this.getNode().getLocalTranslation();
        
        return new Vec2(loc3f.x, loc3f.z);
    }
    public void setLocationRelative(Vec2 loc)
    {
        this.setLocation(this.getLocation().add(loc));
    }
    public void setRotation(double dir)
    {
        this.getNode().setLocalRotation((new Quaternion()).fromAngleAxis((float) dir, Vector3f.UNIT_Y));
    }
}
