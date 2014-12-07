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
    Node footL;
    Node footR;
    Node handL;
    Node handR;
    AnimControl cntrl;
    AnimChannel cnl;
    
    SkeletonControl skele;
    
    @Override
    public void assertNode(Node node)
    {
        super.assertNode(node);
        
        cntrl = node.getControl(AnimControl.class);
        cnl = cntrl.createChannel();
        cnl.setAnim("Stand");
        cnl.setLoopMode(LoopMode.Loop);
        
        skele = node.getControl(SkeletonControl.class);
        
        head = world.loadNode("Models/katty/Katty.mesh.j3o");
        skele.getAttachmentsNode("Head").attachChild(head);
        
        
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
