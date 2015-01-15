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
import naftoreiclag.villagefive.PluginEntity;
import naftoreiclag.villagefive.world.World;

public class EntityRegistry 
{
    public static Map<String, Class<? extends Entity>> entities = new HashMap<String, Class<? extends Entity>>();
    public static Map<String, ModEntity> modEntities = new HashMap<String, ModEntity>();
    
    static
    {
        register(DoorEntity.class);
        register(FlowerEntity.class);
        register(PinguinEntity.class);
        register(PlayerEntity.class);
        register(StoolEntity.class);
        register(ForSaleEntity.class);
        //register(.class);
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
            ex.printStackTrace();

            return;
        }

        entities.put(entity.getTypeName(), gg);
    }

    public static void register(PluginEntity entity)
    {
        modEntities.put(entity.name, new ModEntity(entity));
    }

    public static Entity newInstance(String string)
    {
        
        
        Entity ddd = null;
        
        if(entities.containsKey(string))
        {

            try
            {
                ddd = entities.get(string).newInstance();
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
        else if(modEntities.containsKey(string))
        {
            ddd = modEntities.get(string).duplicate();
        }
        return ddd;
    }
}
