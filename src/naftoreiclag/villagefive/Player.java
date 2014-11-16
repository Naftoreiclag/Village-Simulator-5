/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import naftoreiclag.villagefive.util.Vector2f;

public class Player
{
    public static final String walk = "Shuffle";
    public static final String stand = "Stand";
    
    public final Node node = new Node();
    AnimControl animControl;
    AnimChannel legs;
    
    public Player(Node body)
    {
        node.attachChild(body);
        animControl = body.getControl(AnimControl.class);
        legs = animControl.createChannel();
        legs.addBone("Bone.001");
        legs.addBone("Bone.002");
        legs.addBone("Bone.003");
        legs.addBone("Bone.004");
    }
    
    private float speed = 5.0f;
    private float blend = 1.0f;
    
    public void move(float direction, float tpf)
    {
        node.move(FastMath.cos(direction) * speed * tpf, 0, FastMath.sin(direction) * speed * tpf);
        if(!walk.equals(legs.getAnimationName()))
        {
            legs.setAnim(walk, blend);
            legs.setSpeed(2.0f);
        }
    }
    public void move(Vector2f d, float tpf)
    {
        node.move(d.a * speed * tpf, 0, d.b * speed * tpf);
        if(!walk.equals(legs.getAnimationName()))
        {
            legs.setAnim(walk, blend);
            legs.setSpeed(2.0f);
        }
    }

    public void stopMoving()
    {
        if(!stand.equals(legs.getAnimationName()))
        {
            legs.setAnim(stand, blend);
        }
    }
}
