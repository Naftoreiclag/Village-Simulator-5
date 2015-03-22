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
import naftoreiclag.villagefive.SAM;
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
    private ControllerBody rotControl;
    private MouseJoint locControl;
    
    public double maxTravelDist = 1d;
    public double physGiveupRate = 1d;
    
    public SkeletonControl skele;
    public AnimControl bodyAnimControl;
    public AnimChannel bodyAnimChannel;
    
    public Texture eyeOpenTex;
    public Texture eyeCloseTex;
    public Material faceMat;
    public Node face;
    
    public Material fur;
    
    private float currBlinkTime;
    
    @Override
    public void createNode()
    {
        node = ModelManipulator.loadNode("Models/anthro/Anthro.mesh.j3o");
        fur = SAM.a.loadMaterial("Materials/Michelle.j3m");
        node.setMaterial(fur);
        
        bodyAnimControl = node.getControl(AnimControl.class);
        bodyAnimChannel = bodyAnimControl.createChannel();
        bodyAnimChannel.setAnim("Legs_Walk");
        bodyAnimChannel.setLoopMode(LoopMode.Loop);
        
        skele = node.getControl(SkeletonControl.class);
        
        eyeOpenTex = generateEyeOpenTexture();
        eyeCloseTex = generateEyeClosedTexture();

        faceMat = new Material(world.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        faceMat.setTexture("ColorMap", eyeOpenTex);
        faceMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        
        face = ModelManipulator.loadNode("Models/anthro/Face.mesh.j3o");
        face.setQueueBucket(Bucket.Translucent);
        face.setMaterial(faceMat);
        face.setShadowMode(RenderQueue.ShadowMode.Receive);
        skele.getAttachmentsNode("Head").attachChild(face);
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
        System.out.println("trans: " + Math.round(trans.getRotation() * FastMath.RAD_TO_DEG));
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
        //world.addJoint(locControl);
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
            BufferedImage mouth = ImageIO.read(new File("assets/Textures/debugChin.png"));

            gg.drawImage(eye, 145, 94, null);
            gg.drawImage(mouth, 29, 164, null);
            gg.drawImage(eye, 110, 94, -eye.getWidth(), eye.getHeight(), null);
            
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
            BufferedImage mouth = ImageIO.read(new File("assets/Textures/debugChin.png"));

            gg.drawImage(eye, 145, 94, null);
            gg.drawImage(mouth, 29, 164, null);
            gg.drawImage(eye, 110, 94, -eye.getWidth(), eye.getHeight(), null);
            
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
