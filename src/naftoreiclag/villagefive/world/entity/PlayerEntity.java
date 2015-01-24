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
import org.dyn4j.geometry.Transform;

public class PlayerEntity extends Entity
{
    private ControllerBody rotControl;
    private MouseJoint locControl;
    
    public double maxTravelDist = 1d;
    public double physGiveupRate = 1d;
    
    public Node head;
    public Node tail;
    public Node ears;
    
    public Node footL;
    public Node footR;
    public Node handL;
    public Node handR;
    public Node mask;
    
    public SkeletonControl skele;
    public AnimControl bodyAnimControl;
    public AnimControl tailAnimControl;
    
    public AnimChannel bodyAnimChannel;
    public AnimChannel tailAnimChannel;
    
    public Texture eyeOpenTex;
    public Texture eyeCloseTex;
    public Material faceMat;
    
    private float currBlinkTime;
    
    @Override
    public void createNode()
    {
        node = ModelManipulator.loadNode("Models/katty/KattyBody.mesh.j3o");
        
        bodyAnimControl = node.getControl(AnimControl.class);
        bodyAnimChannel = bodyAnimControl.createChannel();
        bodyAnimChannel.setAnim("Walk");
        bodyAnimChannel.setLoopMode(LoopMode.Loop);
        
        skele = node.getControl(SkeletonControl.class);
        
        head = ModelManipulator.loadNode("Models/katty/Katty.mesh.j3o");
        skele.getAttachmentsNode("Head").attachChild(head);
        
        ears = ModelManipulator.loadNode("Models/katty/KattyEars.mesh.j3o");
        head.attachChild(ears);
        
        tail = ModelManipulator.loadNode("Models/katty/KattyTail.mesh.j3o");
        skele.getAttachmentsNode("Tail").attachChild(tail);
        
        tailAnimControl = tail.getControl(AnimControl.class);
        tailAnimChannel = tailAnimControl.createChannel();
        tailAnimChannel.setAnim("Happy");
        tailAnimChannel.setLoopMode(LoopMode.Loop);
        
        footL = ModelManipulator.loadNode("Models/katty/KattyFoot.mesh.j3o");
        skele.getAttachmentsNode("Foot.L").attachChild(footL);
        footR = ModelManipulator.loadNode("Models/katty/KattyFoot.mesh.j3o");
        skele.getAttachmentsNode("Foot.R").attachChild(footR);
        
        handL = ModelManipulator.loadNode("Models/katty/KattyHand.mesh.j3o");
        skele.getAttachmentsNode("Hand.L").attachChild(handL);
        handR = ModelManipulator.loadNode("Models/katty/KattyHand.mesh.j3o");
        skele.getAttachmentsNode("Hand.R").attachChild(handR);
        
        
        mask = ModelManipulator.loadNode("Models/katty/Face.mesh.j3o");
        
        eyeOpenTex = generateEyeOpenTexture();
        eyeCloseTex = generateEyeClosedTexture();

        faceMat = new Material(world.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        faceMat.setTexture("ColorMap", eyeOpenTex);
        
        faceMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mask.setQueueBucket(Bucket.Transparent);
        mask.setMaterial(faceMat);
        mask.setShadowMode(RenderQueue.ShadowMode.Receive);
        
        head.attachChild(mask);
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
    
    public void turn(double angle, float tpf)
    {
        Transform trans = rotControl.getTransform();
        trans.setRotation(trans.getRotation() + angle);
    }
    public void turnTo(double angle, float tpf)
    {
        Transform trans = rotControl.getTransform();
        trans.setRotation(angle);
    }

    @Override
    public void createBody(PhysWorld world)
    {
        body = new EntityBody(this);
        body.addFixture(new Circle(0.7), 14);
        body.setMass();
        world.addBody(body);
        
        rotControl = new ControllerBody();
        rotControl.addFixture(new Circle(0.7), 14);
        rotControl.setMass(Mass.Type.INFINITE);
        world.addBody(rotControl);
        
        MotorJoint rotJoint = new MotorJoint(rotControl, body);
	    rotJoint.setLinearTarget(Vec2.ZERO_DYN4J);
	    rotJoint.setAngularTarget(0d);
	    rotJoint.setCorrectionFactor(0.3);
	    rotJoint.setMaximumForce(0.0);
	    rotJoint.setMaximumTorque(500.0);
	    world.addJoint(rotJoint);
        
        // frequency = "acceleration toward point"
        // ratio = 
        
        locControl = new MouseJoint(body, Vec2.ZERO_DYN4J, 5, 0.7, 999);
        //world.addJoint(locControl);
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
                faceMat.setTexture("ColorMap", eyeOpenTex);

            }
        }
    }
    
    public void blink()
    {
        
        faceMat.setTexture("ColorMap", eyeCloseTex);
        
        currBlinkTime = 0.1f;
    }

    //
    public static Texture2D generateEyeOpenTexture()
    {
        try
        {
            BufferedImage bi = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gg = bi.createGraphics();

            BufferedImage eye = ImageIO.read(new File("assets/Textures/eye2.png"));
            BufferedImage m = ImageIO.read(new File("assets/Textures/debugChin.png"));

            gg.drawImage(eye, 145, 70, null);
            gg.drawImage(m, 29, 140, null);
            gg.drawImage(eye, 110, 70, -eye.getWidth(), eye.getHeight(), null);
            
            Image image = new AWTLoader().load(bi, false);
            Texture2D tex = new Texture2D(image);
            tex.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
            tex.setMagFilter(Texture.MagFilter.Bilinear);

            return tex;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static Texture2D generateEyeClosedTexture()
    {
        try
        {
            BufferedImage bi = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gg = bi.createGraphics();

            BufferedImage eye = ImageIO.read(new File("assets/Textures/eye3.png"));
            BufferedImage m = ImageIO.read(new File("assets/Textures/debugChin.png"));

            gg.drawImage(eye, 145, 70, null);
            gg.drawImage(m, 29, 140, null);
            gg.drawImage(eye, 110, 70, -eye.getWidth(), eye.getHeight(), null);
            
            Image image = new AWTLoader().load(bi, false);
            Texture2D tex = new Texture2D(image);
            tex.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
            tex.setMagFilter(Texture.MagFilter.Bilinear);

            return tex;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
    @Override
    public String getTypeName()
    {
        return "Player";
    }
}
