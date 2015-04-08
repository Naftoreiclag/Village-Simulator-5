/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.world.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import naftoreiclag.villagefive.addon.LuaEntity;
import naftoreiclag.villagefive.addon.LuaAddon;
import static naftoreiclag.villagefive.addon.AddonManager.addonCollection;
import naftoreiclag.villagefive.world.World;

public class EntityRegistry {
    public static Map<String, Class<? extends Entity>> javaEntities = new HashMap<String, Class<? extends Entity>>();
    public static Map<String, LuaEntity> luaEntities = new HashMap<String, LuaEntity>();

    static {
        try {
            register(DoorEntity.class);
            register(PinguinEntity.class);
            register(PlayerEntity.class);
            register(StoolEntity.class);
        } catch(Exception e) {
            printErrors(e);
        }
    }

    static void printErrors(Throwable t) {
        for(StackTraceElement e : t.getStackTrace()) {
            System.out.println(e);
        }
    }

    public static <SomeEntity extends Entity> void register(Class<SomeEntity> gg) {
        SomeEntity entity;
        try {
            entity = gg.getConstructor().newInstance();

        } catch(Exception ex) {
            printErrors(ex);

            return;
        }

        javaEntities.put(entity.getEntityId(), gg);
    }

    public static void register(LuaEntity entity) {

        luaEntities.put(entity.parent.id + ":" + entity.id, entity);
    }

    public static Entity newInstance(String string) {
        Entity returnVal = null;

        // Try find that in the entity registry
        if(javaEntities.containsKey(string)) {
            try {
                returnVal = javaEntities.get(string).newInstance();
            } catch(InstantiationException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            } catch(IllegalAccessException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if(luaEntities.containsKey(string)) {
            returnVal = new AddonEntity(luaEntities.get(string));
        }
        return returnVal;
    }

    public static boolean exists(String entity) {
        return javaEntities.containsKey(entity) || luaEntities.containsKey(entity);
    }
}
