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

public class EntityRegistry 
{
    public static Map<String, Class<? extends Entity>> entities = new HashMap<String, Class<? extends Entity>>();
    public static Map<String, AddonEntity> addonEntities = new HashMap<String, AddonEntity>();
    
    static
    {
        try
        {
            register(DoorEntity.class);
            register(PinguinEntity.class);
            register(PlayerEntity.class);
            register(StoolEntity.class);
            register(ForSaleEntity.class);
        }
        catch(Exception e)
        {
            System.out.println("dddd");
            printErrors(e);
        }
        //register(.class);
    }

    static void printErrors(Throwable t)
    {
        for(StackTraceElement e : t.getStackTrace())
        {
            System.out.println(e);
        }
    }
    
    public static <SomeEntity extends Entity> void register(Class<SomeEntity> gg)
    {
        SomeEntity entity;
        try
        {
            entity = gg.getConstructor().newInstance();

        }
        catch(Exception ex)
        {
            printErrors(ex);

            return;
        }

        entities.put(entity.getEntityId(), gg);
    }

    public static void register(LuaEntity entity)
    {
        AddonEntity e = new AddonEntity(entity);
        
        addonEntities.put(e.getEntityId(), e);
    }

    public static Entity newInstance(String string)
    {
        Entity returnVal = null;
        
        // Try find that in the entity registry
        if(entities.containsKey(string))
        {
            try
            {
                returnVal = entities.get(string).newInstance();
            }
            catch(InstantiationException ex)
            {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IllegalAccessException ex)
            {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(addonEntities.containsKey(string))
        {
            returnVal = addonEntities.get(string).duplicate();
        }
        return returnVal;
    }
    
    public static void debug()
    {
        for(Map.Entry<String, AddonEntity> pair : addonEntities.entrySet())
        {
            AddonEntity addon = pair.getValue();
            
            System.out.println(addon.getEntityId());
        }
    }

    public static boolean exists(String entity) {
        return entities.containsKey(entity) || addonEntities.containsKey(entity);
    }
}
