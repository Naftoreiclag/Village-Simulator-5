/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import naftoreiclag.villagefive.util.Anglef;
import naftoreiclag.villagefive.util.PhysicsAnglef;

public class PlayerController extends EntityController implements ActionListener
{
    public KatCompleteEntity puppet;
    
    public void setEntity(KatCompleteEntity entity)
    {
        this.puppet = entity;
    }
    
    PhysicsAnglef lookDir = new PhysicsAnglef();
    float turnSpd = 3f;
    
    float speed = 3.5f;
    
    Camera cam;
    
    boolean turningLeft = false;
    boolean turningRight = false;
    boolean movingFwd = false;
    boolean movingBwd = false;

    public void tick(float tpf)
    {
        puppet.node.getLocalRotation().fromAngleAxis(lookDir.x, Vector3f.UNIT_Y);
        
        if(!movingFwd && !movingBwd && !turningLeft && !turningRight)
        {
            if("Walk".equals(puppet.bodyAnimChannel.getAnimationName()))
            {
                puppet.bodyAnimChannel.setAnim("Stand");
            }
            
            return;
        }
        
        
        float targAngle = whereDoesThePlayerWantToGo();
        //lookDir.tweenLocal(targAngle, tpf * turnSpd);
        lookDir.tarX = targAngle;
        lookDir.tick(tpf);
        puppet.move(new Vector2f(FastMath.cos(FastMath.HALF_PI - lookDir.x), FastMath.sin(FastMath.HALF_PI - lookDir.x)).multLocal(tpf * speed));
        
        
        
        
        if("Stand".equals(puppet.bodyAnimChannel.getAnimationName()))
        {
            puppet.bodyAnimChannel.setAnim("Walk");
            puppet.bodyAnimChannel.setSpeed(2.0f);
        }
    }

    public void onAction(String key, boolean isPressed, float tpf)
    {
        System.out.println("key " + key + " = " + isPressed + ";");
        if(key.equals("Walk Forward"))
        {
            movingFwd = isPressed;
        }
        if(key.equals("Walk Backward"))
        {
            movingBwd = isPressed;
        }
        if(key.equals("Rotate Left"))
        {
            turningLeft = isPressed;
        }
        if(key.equals("Rotate Right"))
        {
            turningRight = isPressed;
        }
    }

    void setCamera(Camera cam)
    {
        this.cam = cam;
    }

    private float whereDoesThePlayerWantToGo()
    {
        Vector2f fwd = new Vector2f(cam.getDirection().x, cam.getDirection().z);
        Vector2f dir = new Vector2f();
        if(movingFwd)
        {
            dir.addLocal(fwd);
        }
        if(movingBwd)
        {
            dir.subtractLocal(fwd);
        }
        if(turningLeft)
        {
            dir.addLocal(fwd.y, -fwd.x);
        }
        if(turningRight)
        {
            dir.addLocal(-fwd.y, fwd.x);
        }
        float targAngle = FastMath.HALF_PI - dir.getAngle();
        return targAngle;
    }


}
