/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.rays;

import naftoreiclag.villagefive.world.body.ControllerBody;
import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;

public class ControllerFilter extends CollisionAdapter
{
    @Override
	public boolean collision(Body bodyA, Body bodyB)
    {
        return (!(
               bodyA instanceof ControllerBody
               )) && (!(
               bodyB instanceof ControllerBody
               ));
    }
    
	@Override
	public boolean collision(Body bodyA, BodyFixture _, Body bodyB, BodyFixture __, Manifold ___) { return this.collision(bodyA, bodyB); }
	@Override
	public boolean collision(Body bodyA, BodyFixture _, Body bodyB, BodyFixture __, Penetration ___) { return this.collision(bodyA, bodyB); }
}
