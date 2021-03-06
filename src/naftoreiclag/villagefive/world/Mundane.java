/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.Vec2;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Transform;

/*
 * Modifying transform example:
 * - Controller calls applyImpulse(); or something
 * - Entity acts as a facade and calls applyImpulse() to the body
 * - At some point, the world calls physWorld.update() which performs the impulse and updates the body position
 * - Immediately after, the Entity's tick() is called.
 * - Then, the entity sees that the body body has been updated and updates the spatial position.
 * 
 * Getting transform example:
 * - Controller calls getLocation();
 * - Entity gets the location from the spatial, which is determined from the body.
 * 
 * This class is anything that exists in the world. It only provides some default functionality.
 * 
 * 
 */
public abstract class Mundane
{
    private double linearFrictionCoeff = 10;
    private double angularFrictionCoeff = 10;
    private double forceCoeff = 2000;
    private double impulseCoeff = 20;
    private double torqueCoeff = 20;
    private double velocityCoeff = 1;
    
    protected World world = null;
    // This can only be called once
    public final void assertWorld(World world)
    {
        if(this.world == null)
        {
            this.world = world;
        }
    }
    public final World getWorld()
    {
        return world;
    }
    
    public abstract void createNode();
    protected abstract Node getNode();
    
    public abstract void createBody(PhysWorld world);
    protected abstract Body getBody();
    
    public void setVelocity(Vec2 velocity, float tpf)
    {
        if(this.getBody() == null) { return; }
        
        this.getBody().setLinearVelocity(velocity.mult(this.velocityCoeff).toDyn4j());
    }
    
    /*
     * Raw physics stuff
     */

    public void applyForce(Vec2 force, float tpf)
    {
        if(this.getBody() == null) { return; }
        
        this.getBody().applyForce(force.mult(this.forceCoeff * tpf).toDyn4j());
    }
    public void applyImpulse(Vec2 force, float tpf)
    {
        if(this.getBody() == null) { return; }
        
        this.getBody().applyImpulse(force.mult(this.impulseCoeff * tpf).toDyn4j());
    }
    public void applyImpulse(Vec2 impulse, Vec2 point, float tpf)
    {
        if(this.getBody() == null) { return; }
        
        this.getBody().applyImpulse(impulse.mult(this.impulseCoeff * tpf).toDyn4j(), point.toDyn4j());
    }
    public void applyTorque(double torque, float tpf)
    {
        if(this.getBody() == null) { return; }
        
        this.getBody().applyTorque(torque * this.torqueCoeff * tpf);
    }
    
    // ???
    public void applyFriction(float tpf)
    {
        if(this.getBody() == null) { return; }
        
        this.getBody().setLinearVelocity(Vec2.ZERO_DYN4J);
    }

    public void applyAngularFriction(float tpf)
    {
        if(this.getBody() == null) { return; }
        
        this.getBody().setAngularVelocity(0);
    }
    
    public void setLocation(Vec2 loc)
    {
        if(this.getBody() != null)
        {
            Transform trans = this.getBody().getTransform();
            trans.setTranslation(loc.getX(), loc.getY());
            this.getBody().setTransform(trans);
        }
        if(this.getNode() != null)
        {
            this.getNode().setLocalTranslation(loc.getXF(), 0f, loc.getYF());
        }
        
        onLocationChange(loc);
    }
    public void setLocationRelative(Vec2 loc)
    {
        this.setLocation(this.getLocation().add(loc));
    }
    public void setRotation(Angle dir)
    {
        if(this.getBody() != null)
        {
            Transform trans = this.getBody().getTransform();
            trans.setRotation(dir.getX());
            this.getBody().setTransform(trans);
        }
        else if(this.getNode() != null)
        {
            this.getNode().setLocalRotation(dir.toQuaternion());
        }
        
        
    }
    public Angle getRotation()
    {
        if(this.getBody() != null)
        {
            return new Angle(this.getBody().getTransform());
        }
        else if(this.getNode() != null)
        {
            //errr
            //this.getNode().setLocalRotation(dir.toQuaternion());
        }
        
        return new Angle();
        
        
    }
    
    public Vec2 getLocation()
    {
        if(this.getNode() == null) { return new Vec2(); }
        
        Vector3f loc3f = this.getNode().getLocalTranslation();
        
        return new Vec2(loc3f.x, loc3f.z);
    }
    
    // Overridable things
    public void onLocationChange(Vec2 loc) {};
}
