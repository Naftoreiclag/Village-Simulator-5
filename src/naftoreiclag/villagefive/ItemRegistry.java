/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import naftoreiclag.villagefive.world.entity.Entity;
import naftoreiclag.villagefive.world.entity.EntityRegistry;

public class ItemRegistry {
    
    public static InvItem genNewItem(String id) {
        
        // is it an entity?
        Entity e = EntityRegistry.newInstance(id);
        if(e != null) {
            InvItemEntity i = new InvItemEntity(e);
            return i;
        }
        
        return null;
    }
}
