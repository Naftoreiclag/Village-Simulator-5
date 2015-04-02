/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public abstract class InvItem implements JSONAware {

    public static InvItem makeFrom(JSONObject data) {
        String id = (String) data.get("id");
        
        if(id.equals("itemEntity")) {
            return new InvItemEntity(data);
        }
        
        return null;
    }
    
    public abstract String getClassId();

    public abstract String getItemId();
    
}
