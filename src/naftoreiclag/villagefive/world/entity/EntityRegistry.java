/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import java.util.HashMap;
import java.util.Map;

public class EntityRegistry 
{
    public static Map<String, Class<? extends Entity>> entities = new HashMap<String, Class<? extends Entity>>();
    
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
}
