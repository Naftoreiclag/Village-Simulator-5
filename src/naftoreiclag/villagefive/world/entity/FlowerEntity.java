/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.world.entity;

import naftoreiclag.villagefive.util.scenegraph.ModelManipulator;
import naftoreiclag.villagefive.world.entity.Entity;

public class FlowerEntity extends Entity
{
    @Override
    public void tick(float tpf)
    {
    }

    @Override
    public void loadNode()
    {
        node = ModelManipulator.loadNode("Models/Flower.mesh.j3o");
    }

}
