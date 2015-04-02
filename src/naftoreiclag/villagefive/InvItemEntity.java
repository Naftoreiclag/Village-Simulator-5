/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.entity.EntityRegistry;
import org.json.simple.JSONObject;

public final class InvItemEntity extends InvItem {

    // TODO: lazy loaded entities.
    Entity entity;
    
    public InvItemEntity(Entity entity) {
        this.entity = entity;
    }
    
    public InvItemEntity(JSONObject entityData) {
        String entityId = (String) entityData.get("instanceof");

        entity = EntityRegistry.newInstance(entityId);
    }

    
    @Override
    public String toJSONString() {
        JSONObject object = new JSONObject();
        
        object.put("id", "itemEntity");
        object.put("entityData", entity);
        
        
        return null;
    }

    @Override
    public String getType() {
        return "itemEntity";
    }

    @Override
    public String getId() {
        return "test";
    }
    
}
