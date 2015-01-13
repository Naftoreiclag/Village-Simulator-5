/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import naftoreiclag.villagefive.util.math.Vec2;
import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;
import naftoreiclag.villagefive.world.body.EntityBody;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;

public class StoolEntity extends Entity
{
    @Override
    public void createNode()
    {
        node = ModelManipulator.loadNode("Models/stool/Stool.j3o");
    }
    

    @Override
    public void createBody()
    {
        Vec2 location = this.getLocation();
        body = new EntityBody(this);
        body.addFixture(new Circle(1), 5);
        body.setMass();
        this.setLocation(location);
    }
    
    @Override
    public String getTypeName()
    {
        return "Stool";
    }

}
