/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.serializable;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import naftoreiclag.villagefive.util.math.Polygon;

public class BasicWall extends WallType
{

    @Override
    public Geometry makeInside(Polygon polygon)
    {
        Mesh inM = polygon.genInsideWall(0.2f, 7f, 3f, 3f);
        return new Geometry("Inside", inM);
    }

    @Override
    public Geometry makeOutside(Polygon polygon)
    {
        Mesh outM = polygon.genOutsideWall(0.2f, 7f, 3f, 3f);
        return new Geometry("Outside", outM);
    }

}
