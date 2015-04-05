/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.entity.EntityRegistry;
import naftoreiclag.villagefive.world.entity.PlayerEntity;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public final class InvItem implements JSONAware {

    // TODO: lazy loaded entities.
    public Entity entity;
    protected Inventory inv;
    
    public InvItem(Entity entity) {
        this.entity = entity;
    }
    
    public InvItem(JSONObject entityData) {
        String entityId = (String) entityData.get("instanceof");

        entity = EntityRegistry.newInstance(entityId);
    }

    
    @Override
    public String toJSONString() {
        JSONObject object = new JSONObject();
        
        object.put("classId", "itemEntity");
        object.put("entityData", entity);
        
        
        return null;
    }

    public String getItemId() {
        return entity.getEntityId();
    }

    public void performTask(PlayerEntity caller) {
        Vec2 loc = caller.getLocation();
        caller.getWorld().materializeEntity(entity);
        entity.setLocation(loc);
        
        this.inv.deleteItem(this);
    }

    
}
