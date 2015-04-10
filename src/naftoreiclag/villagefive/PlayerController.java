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
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.data.PlayerModel;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.OreDict;
import naftoreiclag.villagefive.util.math.SmoothAngle;
import naftoreiclag.villagefive.util.math.SmoothScalar;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.HorizQuad;
import naftoreiclag.villagefive.world.body.EntityBody;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.plot.Plot;
import naftoreiclag.villagefive.world.rays.InteractRay;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Mass;


// Does it really make sense to keep this separate from the entity? We'll just have to wait and see.
// Maybe I could just make some kind of "player access" class to reduce entity.controller.entity.method(); calls.
// Also, this would eventually allow me to make some kind of generic "HumanEntity" later on, which could be controlled by some AIController or something.

// Entity controllers are different from entity behaviors!

public final class PlayerController extends EntityController implements ActionListener, AnalogListener {
    public World world;
    private PlayerEntity player;
    
    public SmoothScalar zoomLevel = new SmoothScalar();
    public Angle playerLook = new Angle();
    public SmoothAngle cameraTrackLocation = new SmoothAngle();
    public ReiCamera camera;
    public Spatial ground;
    
    public void setEntity(PlayerEntity entity) {
        HorizQuad quad = new HorizQuad(-300, -300, 300, 300);
        ground = new Geometry("", quad);
        ground.setMaterial(SAM.ASSETS.loadMaterial("Materials/empty.j3m"));
        
        this.player = entity;
        this.player.attachSpatial(ground);
        this.player.controller = this;
        this.world = this.player.getWorld();
    }
    public PlayerEntity getEntity() {
        return player;
    }
    
    float turnSpeed = 3f;
    float walkSpeed = 4.5f;
    
    
    private float scrollSpd = 500.0f;
    
    boolean turningLeft = false;
    boolean turningRight = false;
    boolean movingFwd = false;
    boolean movingBwd = false;
    boolean leftClick = false;
    boolean rotCamLeft = false;
    boolean rotCamRight = false;
    
    public Entity grabbedEnt;
    public WeldJoint grabJoint;

    public PlayerController() {
        cameraTrackLocation.smoothFactor /= 2f;
        cameraTrackLocation.maxSpd /= 2f;

        cameraTrackLocation.disableSmoothing();
        zoomLevel.x = 35;
        zoomLevel.tx = 35;
        zoomLevel.maxSpd *= 999;
        zoomLevel.enableClamp(25, 50);
    }
    
    public void hookToInputs() {
        SAM.INPUT.addListener(this, 
            KeyKeys.move_backward,
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

    // Fire an "interaction ray" and return any entities it collided with.
    public Entity interactRay() {
        InteractRay ray = new InteractRay(player, player.getRotation().toNormalVec());
        List<RaycastResult> results = new ArrayList<RaycastResult>();
        world.physics.raycast(ray, 5.0f, false, false, results);
        if(results.isEmpty()) {
            return null;
        }

        for(RaycastResult hit : results) {
            Body bod = hit.getBody();
            if(bod instanceof EntityBody) {
                return ((EntityBody) bod).owner;
            }
        }

        return null;
    }

   
    public void interact() {
        Entity owner = interactRay();
        if(owner == null) {
            return;
        }

        owner.onInteract(this);
    }

    public void grab() {
        this.grabbedEnt = interactRay();

        if(this.grabbedEnt == null) {
            return;
        }

        grabJoint = new WeldJoint(this.player.getBody(), this.grabbedEnt.getBody(), Vec2.ZERO_DYN4J);
        this.grabbedEnt.getBody().setMass(Mass.Type.NORMAL);
        this.player.getBody().getWorld().addJoint(grabJoint);

    }
    

    // correct
    public Vec2 whereClickingOnGround() {
        Vector3f origin = camera.getCamera().getWorldCoordinates(SAM.INPUT.getCursorPosition(), 0.0f);
        Vector3f direction = camera.getCamera().getWorldCoordinates(SAM.INPUT.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();

        ground.collideWith(ray, results);
        if(results.size() > 0) {
            return new Vec2(results.getClosestCollision().getContactPoint());
        } else {
            return null;
        }
    }

    public void tick(float tpf) {
        updateFrustum(tpf);
        tickDumbAngles(tpf);
        tickMovementInput(tpf);

        world.updateChunkLODs(this.player.getLocation());

        // Move camera on its track
        
        Vec2 loccc = new Vec2(cameraTrackLocation).multLocal(15).addLocal(player.getLocation());
        camera.setLocation(loccc.getXF(), 7, loccc.getYF());
        camera.lookAt(player.getNode().getLocalTranslation().add(Vector3f.UNIT_Y.mult(3)));
    }

    // When a key is pressed
    public void onAction(String key, boolean keyState, float tpf) {
        if(key.equals(KeyKeys.move_forward)) {
            movingFwd = keyState;
        }
        if(key.equals(KeyKeys.move_backward)) {
            movingBwd = keyState;
        }
        if(key.equals(KeyKeys.move_left)) {
            turningLeft = keyState;
        }
        if(key.equals(KeyKeys.move_right)) {
            turningRight = keyState;
        }
        if(key.equals(KeyKeys.rotate_camera_left)) {
            rotCamLeft = keyState;
        }
        if(key.equals(KeyKeys.rotate_camera_right)) {
            rotCamRight = keyState;
        }
        if(key.equals(KeyKeys.mouse_left)) {
            leftClick = keyState;
        }
        if(key.equals(KeyKeys.interact)) {
            if(keyState) {
                if(grabbedEnt == null) {
                    this.grab();
                    System.out.println("grab");
                } else {
                    this.player.getBody().getWorld().removeJoint(grabJoint);
                    grabbedEnt = null;
                    grabJoint = null;
                    System.out.println("ungrab");
                }
            }
        }
    }

    public void onAnalog(String key, float value, float tpf) {
        if(key.equals(KeyKeys.mouse_scroll_up)) {
            player.selectedItem --;
        }
        if(key.equals(KeyKeys.mouse_scroll_down)) {
            player.selectedItem ++;
        }
    }

    private void updateFrustum(float tpf) {
        zoomLevel.tick(tpf);
        float aspect = (float) camera.getCamera().getWidth() / camera.getCamera().getHeight();
        camera.getCamera().setFrustumPerspective(zoomLevel.getXf(), aspect, camera.getCamera().getFrustumNear(), camera.getCamera().getFrustumFar());
    }

    void setCamera(ReiCamera cam) {
        this.camera = cam;
    }

    private Angle whereDoesThePlayerWantToGo(Vec2 fwd) {
        //new Vec2(camera.getCamera().getDirection().x, camera.getCamera().getDirection().z);
        Vec2 dir = new Vec2();

        if(movingFwd) {
            dir.addLocal(fwd);
        }
        if(movingBwd) {
            dir.subtractLocal(fwd);
        }
        if(turningLeft) {
            dir.addLocal(fwd.getY(), -fwd.getX());
        }
        if(turningRight) {
            dir.addLocal(-fwd.getY(), fwd.getX());
        }
        return dir.getAngle();
    }

    // correct
    private void tickDumbAngles(float tpf) {
        //playerLook.tick(tpf);

        if(rotCamLeft) {
            cameraTrackLocation.tx -= 2f * tpf;
        }
        if(rotCamRight) {
            cameraTrackLocation.tx += 2f * tpf;
        }
        cameraTrackLocation.tick(tpf);

        player.turnTo(playerLook, tpf);

        //System.out.println(this.camDispl);
    }
    public boolean walking = true;

    private void tickMovementInput(float tpf) {

        Vec2 gGoto = null;
        if(movingFwd || movingBwd || turningLeft || turningRight) {
            gGoto = whereClickingOnGround();
        }
        
        if(gGoto == null) {
            playerLook.setX(player.getRotation());
            if(walking) {
                player.model.playAnimation(PlayerModel.anim_standstill);
                walking = false;
            }

        } else {
            playerLook.setX(whereDoesThePlayerWantToGo(whereClickingOnGround().subtractLocal(player.getLocation())));

            player.setVelocity(playerLook.toNormalVec().multLocal(walkSpeed), tpf);

            if(!walking) {
                player.model.playAnimation(PlayerModel.anim_walk);
                walking = true;
            }
        }
    }
}
