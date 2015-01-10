/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.rays;

import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.world.entity.Entity;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.RaycastAdapter;
import org.dyn4j.geometry.Ray;

public class InteractRay extends Ray
{
    Entity whois;
    
    public InteractRay(Entity entity, Vec2 dir)
    {
        this(entity, entity.getLocation(), dir);
    }
    
    public InteractRay(Entity entity, Vec2 start, Vec2 dir)
    {
        super(start.toDyn4j(), dir.toDyn4j());
        
        this.whois = entity;
    }
    
    public static class RaycastInteractFilter extends RaycastAdapter
    {
        @Override
        public boolean allow(Ray ray, Body body)
        {
            if(ray instanceof InteractRay)
            {
                InteractRay r = (InteractRay) ray;

                if(body == r.whois.getBody())
                {
                    return false;
                }
            }

            return true;
        }
    }
}
