/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import naftoreiclag.villagefive.PlayerController;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.data.CatModel;
import naftoreiclag.villagefive.data.PlayerModel;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.GR;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;
import naftoreiclag.villagefive.world.PhysWorld;
import naftoreiclag.villagefive.world.body.ControllerBody;
import naftoreiclag.villagefive.world.body.EntityBody;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.MotorJoint;
import org.dyn4j.dynamics.joint.MouseJoint;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;

public class PlayerEntity extends Entity
{
    public PlayerController controller;
    
    private ControllerBody rotControl;
    private MouseJoint locControl;
    
    public double maxTravelDist = 1d;
    public double physGiveupRate = 1d;
    
    private float currBlinkTime;
    
    public PlayerModel model;
    
    @Override
    public void createNode()
    {
        model = new CatModel();
        model.load();
        node = model.getNode();
    }
    
    Spatial ground;
    public void attachGround(Spatial ground)
    {
        this.ground = ground;
    }
    
    @Override
    public void onLocationChange(Vec2 loc)
    {
        if(world != null) {world.something(loc);}
        
        if(ground != null) { ground.setLocalTranslation(loc.toJmeVec3()); }
    }
    
    @Override
    public void setLocation(Vec2 loc)
    {
        super.setLocation(loc);
        
        locControl.setTarget(loc.toDyn4j());
    }
    
    public void travel(Vec2 dir, float tpf)
    {
        locControl.getTarget().add(dir.mult(tpf).toDyn4j());
    }
    
    public void turn(Angle angle, float tpf)
    {
        Transform trans = rotControl.getTransform();
        trans.setRotation(trans.getRotation() + angle.toDyn4j());
    }
    public void turnTo(Angle angle, float tpf)
    {
        Transform trans = rotControl.getTransform();
        trans.setRotation(angle.toDyn4j());
    }

    @Override
    public void createBody(PhysWorld world)
    {
        body = new EntityBody(this);
        body.addFixture(new Circle(0.7), 14);
        body.addFixture(new Rectangle(1, 1), 14);
        body.setMass();
        world.addBody(body);
        
        rotControl = new ControllerBody();
        rotControl.addFixture(new Circle(0.7), 14);
        rotControl.setMass(Mass.Type.INFINITE);
        world.addBody(rotControl);
        
        MotorJoint rotJoint = new MotorJoint(rotControl, body);
	    rotJoint.setLinearTarget(Vec2.ZERO_DYN4J);
	    rotJoint.setAngularTarget(0d);
	    rotJoint.setCorrectionFactor(0.1);
	    rotJoint.setMaximumForce(0.0);
	    rotJoint.setMaximumTorque(100.0);
	    world.addJoint(rotJoint);
        
        locControl = new MouseJoint(body, Vec2.ZERO_DYN4J, 5, 0.7, 999);
    }
    
    @Override
    public void applyAngularFriction(float tpf)
    {
        
    }
    
    @Override
    public void tick(float tpf)
    {
        super.tick(tpf);
        
        if(GR.chanceHertz(tpf, 0.1f))
        {
            blink();
        }
        
        Vec2 move = new Vec2(locControl.getTarget());
        move.subtractLocal(this.getLocation());
        if(move.lenSq() > this.maxTravelDist * this.maxTravelDist)
        {
            move.normalizeLocal().multLocal(this.maxTravelDist);
        }
        else
        {
            move.subtractLocal(move.normalize().multLocal(physGiveupRate * tpf));
        }
        move.addLocal(this.getLocation());
        locControl.setTarget(move.toDyn4j());

        if(currBlinkTime > 0)
        {
            currBlinkTime -= tpf;
            if(currBlinkTime < 0)
            {
                model.playAnimation(PlayerModel.anim_openEye);
            }
        }
    }
    
    public void blink()
    {
        model.playAnimation(PlayerModel.anim_closeEye);
        
        currBlinkTime = 0.1f;
    }
    
    @Override
    public String getEntityId()
    {
        return "Player";
    }
}
