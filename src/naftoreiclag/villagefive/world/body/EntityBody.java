/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.body;

import naftoreiclag.villagefive.world.entity.Entity;
import org.dyn4j.dynamics.Body;

public class EntityBody extends Body
{
    public final Entity owner;
    
    public EntityBody(Entity creator)
    {
        super();
        
        this.owner = creator;
    }
}
