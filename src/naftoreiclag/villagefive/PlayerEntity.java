/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.scene.Spatial;

class PlayerEntity extends Entity
{
    public PlayerEntity(Spatial bod)
    {
        super(bod);
    }

    @Override
    public void update(float tpf)
    {
        fisBody.mot.set(1, 1);
    }

}
