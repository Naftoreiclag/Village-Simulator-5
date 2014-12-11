/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

public class KatCompleteEntity extends Entity
{
    @Override
    public void meow()
    {
        System.out.println("MEOW mew meeow");
    }
    
    Node head;
    Node tail;
    Node ears;
    
    Geometry eyeL;
    Geometry eyeR;
    
    Node footL;
    Node footR;
    Node handL;
    Node handR;
    Node mask;
    AnimControl bodyAnimControl;
    AnimChannel bodyAnimChannel;
    AnimControl tailAnimControl;
    AnimChannel tailAnimChannel;
    
    SkeletonControl skele;
    SkeletonControl face;
    
    @Override
    public void assertNode(Node newNode)
    {
        super.assertNode(newNode);
        
        bodyAnimControl = node.getControl(AnimControl.class);
        bodyAnimChannel = bodyAnimControl.createChannel();
        bodyAnimChannel.setAnim("Walk");
        bodyAnimChannel.setLoopMode(LoopMode.Loop);
        
        skele = node.getControl(SkeletonControl.class);
        
        head = world.loadNode("Models/katty/Katty.mesh.j3o");
        skele.getAttachmentsNode("Head").attachChild(head);
        
        ears = world.loadNode("Models/katty/KattyEars.mesh.j3o");
        head.attachChild(ears);
        
        tail = world.loadNode("Models/katty/KattyTail.mesh.j3o");
        skele.getAttachmentsNode("Tail").attachChild(tail);
        
        tailAnimControl = tail.getControl(AnimControl.class);
        tailAnimChannel = tailAnimControl.createChannel();
        tailAnimChannel.setAnim("Happy");
        tailAnimChannel.setLoopMode(LoopMode.Loop);
        
        footL = world.loadNode("Models/katty/KattyFoot.mesh.j3o");
        skele.getAttachmentsNode("Foot.L").attachChild(footL);
        footR = world.loadNode("Models/katty/KattyFoot.mesh.j3o");
        skele.getAttachmentsNode("Foot.R").attachChild(footR);
        
        handL = world.loadNode("Models/katty/KattyHand.mesh.j3o");
        skele.getAttachmentsNode("Hand.L").attachChild(handL);
        handR = world.loadNode("Models/katty/KattyHand.mesh.j3o");
        skele.getAttachmentsNode("Hand.R").attachChild(handR);
        
        
        eyeTex = world.assetManager.loadTexture("Textures/debugFace.png");
        mask = world.loadNode("Models/katty/Face.mesh.j3o");
        //Geometry geo = (Geometry) mask.getChild("Katty");
        
        /*
        
        Material mat = new Material(world.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", false);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setTexture("DiffuseMap", eyeTex);
        
        */
        
        Material mat = new Material(world.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", eyeTex);
        
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mask.setQueueBucket(Bucket.Transparent);
        mask.setMaterial(mat);
        mask.setShadowMode(RenderQueue.ShadowMode.Receive);
        //mask.setLocalScale(0.3f);
        
        
        head.attachChild(mask);
        
        /*
        eyeTex = world.assetManager.loadTexture("Textures/eye.png");
        
        face = head.getControl(SkeletonControl.class);
        eyeL = makeEye(false);//world.loadNode("Models/katty/KattyFoot.mesh.j3o");
        face.getAttachmentsNode("Eye.L").attachChild(eyeL);
        eyeR = makeEye(true);//world.loadNode("Models/katty/KattyFoot.mesh.j3o");
        face.getAttachmentsNode("Eye.R").attachChild(eyeR);
        */
    }

    @Override
    public String getModelName()
    {
        return "Models/katty/KattyBody.mesh.j3o";
    }

    private Mesh getEyeMesh(boolean right)
    {
        Mesh mesh = new Mesh();
        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(0.5f, 0, 0.5f);
        vertices[1] = new Vector3f(-0.5f, 0, 0.5f);
        vertices[2] = new Vector3f(-0.5f, 0, -0.5f);
        vertices[3] = new Vector3f(0.5f, 0, -0.5f);
        
        Vector3f[] normals = new Vector3f[4];
        normals[0] = new Vector3f(0, 1, 0);
        normals[1] = new Vector3f(0, 1, 0);
        normals[2] = new Vector3f(0, 1, 0);
        normals[3] = new Vector3f(0, 1, 0);

        Vector2f[] texCoord = new Vector2f[4];
        if(right)
        {
            texCoord[0] = new Vector2f(1, 1);
            texCoord[1] = new Vector2f(0, 1);
            texCoord[2] = new Vector2f(0, 0);
            texCoord[3] = new Vector2f(1, 0);
        }
        else
        {
            texCoord[0] = new Vector2f(0, 1);
            texCoord[1] = new Vector2f(1, 1);
            texCoord[2] = new Vector2f(1, 0);
            texCoord[3] = new Vector2f(0, 0);
        }

        int[] indexes = {0, 3, 2, 0, 2, 1};
        

        float[] colorArray =
        {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            1, 1, 1, 0
        };
        mesh.setBuffer(Type.Color, 4, colorArray);
        
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indexes));
        mesh.updateBound();

        return mesh;
    }

    Texture eyeTex;
    private Geometry makeEye(boolean right)
    {
        Geometry geo = new Geometry("Eyemesh", getEyeMesh(right));
        Material matVC = new Material(world.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matVC.setBoolean("VertexColor", true);
        matVC.setTexture("ColorMap", eyeTex);
        matVC.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geo.setQueueBucket(Bucket.Transparent);
        geo.setMaterial(matVC);
        geo.setShadowMode(RenderQueue.ShadowMode.Receive);
        geo.setLocalScale(0.3f);
        return geo;
    }

}