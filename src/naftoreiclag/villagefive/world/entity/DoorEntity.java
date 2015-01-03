/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;
import naftoreiclag.villagefive.world.entity.Entity;
import org.dyn4j.dynamics.Body;

public class DoorEntity extends Entity
{
    @Override
    public void tick(float tpf)
    {}

    @Override
    public void createNode()
    {
        node = ModelManipulator.loadNode("Models/Frame.mesh.j3o");
    }

    @Override
    public void createBody()
    {
    }

}
