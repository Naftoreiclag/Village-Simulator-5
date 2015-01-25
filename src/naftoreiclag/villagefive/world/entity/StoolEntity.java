/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;
import naftoreiclag.villagefive.world.PhysWorld;
import naftoreiclag.villagefive.world.body.EntityBody;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Circle;

public class StoolEntity extends Entity
{
    EntityBody body2;
    
    @Override
    public void createNode()
    {
        node = ModelManipulator.loadNode("Models/stool/Stool.j3o");
    }
    

    @Override
    public void createBody(PhysWorld world)
    {
        Vec2 location = this.getLocation();
        
        body = new EntityBody(this);
        body.addFixture(new Circle(1), 5);
        body.setMass();
        world.addBody(body);
        
        body2 = new EntityBody(this);
        body2.addFixture(new Circle(1), 5);
        body2.translate(new Vec2(0, 1).toDyn4j());
        body2.setMass();
        
        WeldJoint joint = new WeldJoint(body, body2, Vec2.ZERO_DYN4J);
        world.addJoint(joint);
        
        this.setLocation(location);
        
        world.addBody(body2);
    }
    
    @Override
    public String getTypeName()
    {
        return "Stool";
    }

}
