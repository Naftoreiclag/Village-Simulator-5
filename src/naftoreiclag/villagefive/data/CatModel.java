/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.data;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.util.AssetsLoaderUtil;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;

public class CatModel implements PlayerModel
{
    private Node body;
    private SkeletonControl bodySkele;
    private AnimControl bodyAnim;
    
    private AnimChannel LegChannel;
    private AnimChannel ArmChannel;
    
    public Texture eyeOpenTex;
    public Texture eyeCloseTex;
    public Material faceMat;
    public Node face;
    
    public void load()
    {
        body = ModelManipulator.loadNode("Models/anthro/Anthro_Feline.mesh.j3o");
        Material fur = SAM.ASSETS.loadMaterial("Materials/Michelle.j3m");
        body.setMaterial(fur);
        
        bodyAnim = body.getControl(AnimControl.class);
        LegChannel = bodyAnim.createChannel();
        LegChannel.setLoopMode(LoopMode.Loop);
        ArmChannel = bodyAnim.createChannel();
        ArmChannel.setLoopMode(LoopMode.Loop);
        
        bodySkele = body.getControl(SkeletonControl.class);
        
        eyeOpenTex = generateEyeOpenTexture();
        eyeCloseTex = generateEyeClosedTexture();

        faceMat = new Material(SAM.ASSETS, "Common/MatDefs/Misc/Unshaded.j3md");
        faceMat.setTexture("ColorMap", eyeOpenTex);
        faceMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        face = ModelManipulator.loadNode("Models/anthro/Face.mesh.j3o");
        face.setQueueBucket(RenderQueue.Bucket.Translucent);
        face.setMaterial(faceMat);
        face.setShadowMode(RenderQueue.ShadowMode.Off);
        bodySkele.getAttachmentsNode("Head").attachChild(face);
    }
    
    public Node getNode()
    {
        return body;
    }

    public void playAnimation(String name)
    {
        if(name.equals(PlayerModel.anim_walk))
        {
            LegChannel.setAnim("Legs_Walk");
            LegChannel.setSpeed(2.0f);
            ArmChannel.setAnim("Arms_Walk");
            ArmChannel.setSpeed(2.0f);
        }
        else if(name.equals(PlayerModel.anim_standstill))
        {
            LegChannel.setAnim("Stand");
            ArmChannel.setAnim("Arms_Relaxed");
        }
        else if(name.equals(PlayerModel.anim_openEye))
        {
            faceMat.setTexture("ColorMap", eyeOpenTex);
        }
        else if(name.equals(PlayerModel.anim_closeEye))
        {
            faceMat.setTexture("ColorMap", eyeCloseTex);
        }
    }
    
    //
    public static Texture2D generateEyeOpenTexture()
    {
        try
        {
            BufferedImage bi = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gg = bi.createGraphics();

            BufferedImage eye = AssetsLoaderUtil.loadImage("Textures/eye2.png");
            BufferedImage mouth = AssetsLoaderUtil.loadImage("Textures/debugChin.png");

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

            BufferedImage eye = AssetsLoaderUtil.loadImage("Textures/eye2.png");
            BufferedImage mouth = AssetsLoaderUtil.loadImage("Textures/debugChin.png");

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
    
}
