/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
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
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.util.math.OreDict;
import naftoreiclag.villagefive.util.math.SmoothAngle;
import naftoreiclag.villagefive.util.math.SmoothScalar;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.PhysWorld;
import naftoreiclag.villagefive.world.Resident;
import naftoreiclag.villagefive.world.body.EntityBody;
import naftoreiclag.villagefive.world.plot.Plot;
import naftoreiclag.villagefive.world.rays.InteractRay;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.RaycastResult;

public class PlayerController extends EntityController implements ActionListener, AnalogListener
{
    public PlayerEntity puppet;
    public World world;
    
    public List<Plot> property;

    public void setEntity(PlayerEntity entity)
    {
        this.puppet = entity;
        this.world = puppet.getWorld();
    }
    
    float turnSpd = 3f;
    float speed = 4.5f;
    private float scrollSpd = 500.0f;
    
    SmoothScalar zoomLevel = new SmoothScalar();
    SmoothAngle playerLook = new SmoothAngle();
    SmoothAngle camDispl = new SmoothAngle();

    public PlayerController()
    {
        playerLook.maxSpd *= 4f;

        camDispl.smoothFactor /= 2f;
        camDispl.maxSpd /= 2f;
        
        camDispl.disableSmoothing();
        zoomLevel.x = 35;
        zoomLevel.tx = 35;
        zoomLevel.maxSpd *= 2;
        zoomLevel.enableClamp(25, 50);
    }
    
    public void interact()
    {
        InteractRay ray = new InteractRay(puppet, puppet.getRotation().toNormalVec());

        List<RaycastResult> results = new ArrayList<RaycastResult>();

        world.physics.raycast(ray, 5.0f, false, false, results);
        
        if(results.isEmpty())
        {
            return;
        }
        
        RaycastResult hit = results.get(0);
        Body bod = hit.getBody();
        
        if(!(bod instanceof EntityBody))
        {
            return;
            
        }

        EntityBody oth = (EntityBody) bod;

        System.out.println("hit " + oth.owner.getTypeName());
        
        oth.owner.onInteract(this);
        
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
                                      KeyKeys.mouse_left,
                                      KeyKeys.mouse_scroll_up,
                                      KeyKeys.mouse_scroll_down,
                                      KeyKeys.openInv,
                                      KeyKeys.interact);
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

        updateFrustum(tpf);
        tickDumbAngles(tpf);
        tickMovementInput(tpf);

        // Move camera on its track
        cam.setLocation(OreDict.JmeAngleToVec3((float) camDispl.getX()).multLocal(15f).addLocal(0f, 7f, 0f).addLocal(OreDict.Vec2ToVec3(puppet.getLocation())));
        
        // lookie
        if(invOpen)
        {
            cam.lookAt(puppet.getNode().getLocalTranslation().add(Vector3f.UNIT_Y.mult(4)));
            inv.enable();
        }
        else
        {
            cam.lookAt(puppet.getNode().getLocalTranslation().add(Vector3f.UNIT_Y.mult(3)));
            inv.disable();
        }
        
        
    }
    
    Inventory inv;
    
    boolean invOpen = false;
    boolean isInvOpenKeyPressed = false;

    // When a key is pressed
    public void onAction(String key, boolean isPressed, float tpf)
    {
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
        if(key.equals(KeyKeys.interact))
        {
            this.interact();
        }
        if(key.equals(KeyKeys.openInv))
        {
            if(!isInvOpenKeyPressed)
            {
                invOpen = !invOpen;
            }
            isInvOpenKeyPressed = isPressed;
        }
    }
    
    
    public void onAnalog(String key, float value, float tpf)
    {
        if(key.equals(KeyKeys.mouse_scroll_up))
        {
            zoomLevel.tx -= value * tpf * scrollSpd;
        }
        if(key.equals(KeyKeys.mouse_scroll_down))
        {
            zoomLevel.tx += value * tpf * scrollSpd;
        }
    }
    
    private void updateFrustum(float tpf)
    {
        zoomLevel.tick(tpf);
        float aspect = (float) cam.c.getWidth() / cam.c.getHeight();
        cam.c.setFrustumPerspective(zoomLevel.getXf(), aspect, cam.c.getFrustumNear(), cam.c.getFrustumFar());
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
        
        
        float targAngle = FastMath.HALF_PI - dir.getAngle().getXF();
        return targAngle;
    }

    void setGround(Spatial ground)
    {
        this.ground = ground;
    }

    private void tickDumbAngles(float tpf)
    {
        playerLook.tick(tpf);

        if(rotCamLeft)
        {
            camDispl.tx -= 2f * tpf;
        }
        if(rotCamRight)
        {
            camDispl.tx += 2f * tpf;
        }
        camDispl.tick(tpf);

        puppet.applyTorque(-playerLook.calcSignedDiff(puppet.getRotation().getX()));
    }

    private void tickMovementInput(float tpf)
    {
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
                playerLook.tx = playerLook.getX();
            }

        }
        else
        {
            if(groundGoto != null)
            {
                groundGoto.subtractLocal(puppet.getNode().getLocalTranslation());
                playerLook.tx = FastMath.atan2(groundGoto.x, groundGoto.z);
            }
            else
            {
                playerLook.tx = whereDoesThePlayerWantToGo();
            }

            puppet.applyImpulse(OreDict.JmeAngleToVec2((float) playerLook.getX()).multLocal(tpf * speed));

            if("Stand".equals(puppet.bodyAnimChannel.getAnimationName()))
            {
                puppet.bodyAnimChannel.setAnim("Walk");
                puppet.bodyAnimChannel.setSpeed(2.0f);
            }
        }
    }

    Resident resid;
    void setResidence(Resident resid)
    {
        this.resid = resid;
    }


}
