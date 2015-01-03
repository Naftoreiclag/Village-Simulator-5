/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import naftoreiclag.villagefive.world.World;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.Mundane;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Transform;

// Concrete entities define the behavior for all "instances" of itself.
// Important: All sub-classes must have one 0-argument constructor. (or have no written constructors at all)
// Position and rotation are stored in the node
public abstract class Entity extends Mundane
{
    protected Body body = null;
    protected Node node = null;
    
    public void destroySelf()
    {
        this.world.destroyEntity(this);
    }
    
    
    @Override
    public final Body getBody()
    {
        return body;
    }
    
    @Override
    public final Node getNode()
    {
        return node;
    }
    
    // Make sure you call the super!
    public void tick(float tpf)
    {
        if(body != null)
        {
            Vec2 loc = new Vec2(this.body.getTransform().getTranslation());
            Angle rot = new Angle(this.body.getTransform().getRotation());

            this.node.setLocalTranslation(loc.getXF(), 0f, loc.getYF());
            this.node.setLocalRotation(rot.toQuaternion());
            
            this.applyFriction();
            
            this.onLocationChange(loc);
        }
        
    }
    
    public void attachSpatial(Spatial spatial)
    {
        this.node.attachChild(spatial);
    }
    
}
