/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.world.entity.PlayerEntity;
import org.json.simple.JSONAware;

public class PlayerOfflineData implements JSONAware {

    Inventory inventory;
    PlayerEntity storedData;
    
    public String toJSONString() {
        return null;
    }
    
}
