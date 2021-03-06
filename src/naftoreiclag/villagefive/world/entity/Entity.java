/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.PlayerController;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.util.math.Angle;
import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.Mundane;
import naftoreiclag.villagefive.world.World;
import naftoreiclag.villagefive.world.body.EntityBody;
import naftoreiclag.villagefive.world.rays.InteractRay;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.RaycastResult;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

// Concrete entities define the behavior for all "instances" of itself.
// Important: All sub-classes must have one 0-argument constructor. (or have no written constructors at all)
// Position and rotation are stored in the node
public abstract class Entity extends Mundane implements JSONAware
{
    public static Texture defaultIcon = SAM.ASSETS.loadTexture("Textures/default_icon.png");
    
    protected Body body = null;
    protected Node node = null;
    
    public Entity()
    {
    }
    
    public void removeSelf()
    {
        this.world.removeEntity(this);
    }
    
    @Override
    public final Body getBody()
    {
        return body;
    }
    
    @Override
    public final Node getNode()
    {
        return node;
    }
    
    // Make sure you call the super when overriding this!
    public void tick(float tpf)
    {
        if(body != null)
        {
            Vec2 loc = new Vec2(this.body.getTransform().getTranslation());
            Angle rot = new Angle(this.body.getTransform());

            this.node.setLocalTranslation(loc.getXF(), 0f, loc.getYF());
            this.node.setLocalRotation(rot.toQuaternion());
            
            this.applyFriction(tpf);
            this.applyAngularFriction(tpf);
            
            this.onLocationChange(loc);
        }
    }
    
    public void attachSpatial(Spatial spatial)
    {
        this.node.attachChild(spatial);
    }
    
    public final String toJSONString()
    {
        JSONObject obj = new JSONObject();
        
        obj.put("instanceof", this.getEntityId());
        obj.put("location", new Vec2(this.node.getLocalTranslation()));
        
        this.addAdditionalData(obj);
        
        return obj.toJSONString();
    }
    
    public abstract String getEntityId();
    
    public void addAdditionalData(JSONObject data) {}
    
    public void onInteract(PlayerController interactor) {
        
    }
    
    public Texture getIcon() {
        return defaultIcon;
    }
    
    // Fire an "interaction ray" and return any entities it collided with.
    public Entity interactRay() {
        InteractRay ray = new InteractRay(this, this.getRotation().toNormalVec());
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
}
