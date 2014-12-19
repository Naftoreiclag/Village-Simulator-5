/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import naftoreiclag.villagefive.util.SmoothAnglef;

public class PlayerController extends EntityController implements ActionListener
{
    public KatCompleteEntity puppet;

    public void setEntity(KatCompleteEntity entity)
    {
        this.puppet = entity;
    }
    SmoothAnglef lookDir = new SmoothAnglef();
    float turnSpd = 3f;
    float speed = 3.5f;
    SmoothAnglef camDir = new SmoothAnglef();

    public PlayerController()
    {

        camDir.smoothFactor /= 2f;
        camDir.maxSpd /= 2f;
    }
    Camera cam;
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
    }

    public Vector3f whereClickingOnGround()
    {
        Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
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


        puppet.node.setLocalRotation((new Quaternion()).fromAngleAxis(lookDir.x, Vector3f.UNIT_Y));


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
                lookDir.setTarg(lookDir.x);
            }

        }
        else
        {
            if(groundGoto != null)
            {
                groundGoto.subtractLocal(puppet.node.getLocalTranslation());
                lookDir.setTarg(FastMath.atan2(groundGoto.x, groundGoto.z));
            }
            else
            {
                lookDir.setTarg(whereDoesThePlayerWantToGo());
            }


            puppet.move(new Vector2f(FastMath.cos(FastMath.HALF_PI - lookDir.x), FastMath.sin(FastMath.HALF_PI - lookDir.x)).multLocal(tpf * speed));




            if("Stand".equals(puppet.bodyAnimChannel.getAnimationName()))
            {
                puppet.bodyAnimChannel.setAnim("Walk");
                puppet.bodyAnimChannel.setSpeed(2.0f);
            }
        }


        cam.setLocation((new Vector3f(FastMath.cos(FastMath.HALF_PI - camDir.x) * 15.0f, 7.0f, FastMath.sin(FastMath.HALF_PI - camDir.x) * 15.0f)).addLocal(puppet.node.getLocalTranslation()));
        cam.lookAt(puppet.node.getLocalTranslation().add(Vector3f.UNIT_Y.mult(3)), Vector3f.UNIT_Y);
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
        if(key.equals("Rotate Cam Left"))
        {
            rotCamLeft = isPressed;
        }
        if(key.equals("Rotate Cam Right"))
        {
            rotCamRight = isPressed;
        }
        if(key.equals("Left Click"))
        {
            leftClick = isPressed;
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

    void setGround(Spatial ground)
    {
        this.ground = ground;
    }
}
