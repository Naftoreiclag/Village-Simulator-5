/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Bone;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.scene.Node;

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
    Node footL;
    Node footR;
    Node handL;
    Node handR;
    AnimControl bodyAnimControl;
    AnimChannel bodyAnimChannel;
    AnimControl tailAnimControl;
    AnimChannel tailAnimChannel;
    
    SkeletonControl skele;
    
    @Override
    public void assertNode(Node newNode)
    {
        super.assertNode(newNode);
        
        bodyAnimControl = node.getControl(AnimControl.class);
        bodyAnimChannel = bodyAnimControl.createChannel();
        bodyAnimChannel.setAnim("Stand");
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
        
        
    }

    @Override
    public String getModelName()
    {
        return "Models/katty/KattyBody.mesh.j3o";
    }
}
