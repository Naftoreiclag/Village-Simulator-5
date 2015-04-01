/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.world.entity.Entity;
import org.json.simple.JSONObject;

public final class InvItemEntity extends InvItem {

    JSONObject entityData;
    
    public InvItemEntity(JSONObject data) {
        entityData = (JSONObject) data.get("entityData");
    }
    
    @Override
    public String toJSONString() {
        JSONObject object = new JSONObject();
        
        object.put("id", "itemEntity");
        object.put("entityData", entityData);
        
        return null;
    }
    
}
