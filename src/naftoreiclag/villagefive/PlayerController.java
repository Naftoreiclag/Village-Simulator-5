/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.ReiCamera;
import naftoreiclag.villagefive.world.entity.PlayerEntity;
import naftoreiclag.villagefive.world.World;
import naftoreiclag.villagefive.util.KeyKeys;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.math.OreDict;
import naftoreiclag.villagefive.util.math.SmoothAnglef;
import naftoreiclag.villagefive.util.math.Vec2;

public class PlayerController extends EntityController implements ActionListener
{
    public PlayerEntity puppet;
    public World world;

    public void setEntity(PlayerEntity entity)
    {
        this.puppet = entity;
        this.world = puppet.world;
    }
    
    float turnSpd = 3f;
    float speed = 4.5f;
    
    SmoothAnglef lookDir = new SmoothAnglef();
    SmoothAnglef camDir = new SmoothAnglef();

    public PlayerController()
    {
        lookDir.maxSpd *= 4f;

        camDir.smoothFactor /= 2f;
        camDir.maxSpd /= 2f;
        
        camDir.disableSmoothing();
    }
    
    ReiCamera cam;
    Spatial ground;
    boolean turningLeft = false;
    boolean turningRight = false;
    boolean movingFwd = false;
    boolean movingBwd = false;
    boolean leftClick = false;
    boolean rotCamLeft = false;
    boolean rotCamRight = false;
    InputManager inputManager;

    public void setManager(InputManager inputManager)
    {
        this.inputManager = inputManager;
        this.inputManager.addListener(this, KeyKeys.move_backward, 
                                 KeyKeys.move_forward, 
                                 KeyKeys.move_left, 
                                 KeyKeys.move_right, 
                                 KeyKeys.rotate_camera_left, 
                                 KeyKeys.rotate_camera_right, 
                                 KeyKeys.mouse_left);
    }

    public Vector3f whereClickingOnGround()
    {
        Vector3f origin = cam.c.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.c.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();

        ground.collideWith(ray, results);
        if(results.size() > 0)
        {
            return results.getClosestCollision().getContactPoint();
        }
        else
        {
            return null;
        }
    }

    public void tick(float tpf)
    {
        if(!enabled)
        {
            return;
        }

        lookDir.tick(tpf);

        if(rotCamLeft)
        {
            camDir.tx -= 2f * tpf;
        }
        if(rotCamRight)
        {
            camDir.tx += 2f * tpf;
        }
        camDir.tick(tpf);

        puppet.setRotation(lookDir.x);

        Vector3f groundGoto = null;
        if(leftClick)
        {
            groundGoto = whereClickingOnGround();
        }

        if(!movingFwd && !movingBwd && !turningLeft && !turningRight && groundGoto == null)
        {
            if("Walk".equals(puppet.bodyAnimChannel.getAnimationName()))
            {
                puppet.bodyAnimChannel.setAnim("Stand");
                lookDir.tx = lookDir.x;
            }

        }
        else
        {
            if(groundGoto != null)
            {
                groundGoto.subtractLocal(puppet.getNode().getLocalTranslation());
                lookDir.tx = FastMath.atan2(groundGoto.x, groundGoto.z);
            }
            else
            {
                lookDir.tx = whereDoesThePlayerWantToGo();
            }

            puppet.setLocationRelative(OreDict.JmeAngleToVec2(lookDir.x).multLocal(tpf * speed));

            if("Stand".equals(puppet.bodyAnimChannel.getAnimationName()))
            {
                puppet.bodyAnimChannel.setAnim("Walk");
                puppet.bodyAnimChannel.setSpeed(2.0f);
            }
        }


        cam.setLocation(OreDict.JmeAngleToVec3(camDir.x).multLocal(15f).addLocal(0f, 7f, 0f).addLocal(OreDict.Vec2ToVec3(puppet.getLocation())));
        cam.lookAt(puppet.getNode().getLocalTranslation().add(Vector3f.UNIT_Y.mult(3)));
    }

    // When a key is pressed
    public void onAction(String key, boolean isPressed, float tpf)
    {
        if(!enabled)
        {
            return;
        }
        System.out.println("key " + key + " = " + isPressed + ";");
        if(key.equals(KeyKeys.move_forward))
        {
            movingFwd = isPressed;
        }
        if(key.equals(KeyKeys.move_backward))
        {
            movingBwd = isPressed;
        }
        if(key.equals(KeyKeys.move_left))
        {
            turningLeft = isPressed;
        }
        if(key.equals(KeyKeys.move_right))
        {
            turningRight = isPressed;
        }
        if(key.equals(KeyKeys.rotate_camera_left))
        {
            rotCamLeft = isPressed;
        }
        if(key.equals(KeyKeys.rotate_camera_right))
        {
            rotCamRight = isPressed;
        }
        if(key.equals(KeyKeys.mouse_left))
        {
            leftClick = isPressed;
        }
    }

    void setCamera(ReiCamera cam)
    {
        this.cam = cam;
    }

    private float whereDoesThePlayerWantToGo()
    {
        Vec2 fwd = new Vec2(cam.c.getDirection().x, cam.c.getDirection().z);
        Vec2 dir = new Vec2();
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
            dir.addLocal(fwd.getY(), -fwd.getX());
        }
        if(turningRight)
        {
            dir.addLocal(-fwd.getY(), fwd.getX());
        }
        
        
        float targAngle = FastMath.HALF_PI - dir.getAngle();
        return targAngle;
    }

    void setGround(Spatial ground)
    {
        this.ground = ground;
    }
    
    private boolean enabled = true;
    
    void disableInput()
    {
        this.enabled = false;
    }
    void enable()
    {
        this.enabled = true;
    }

}
