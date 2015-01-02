/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.util.scenegraph;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

public class HorizQuad extends Mesh
{
    public HorizQuad(float x1, float y1, float x2, float y2)
    {
        this.setBuffer(VertexBuffer.Type.Position, 3, new float[]
        {
            x1, 0f, y1,
            x2, 0f, y1,
            x2, 0f, y2,
            x1, 0f, y2
        });
        this.setBuffer(VertexBuffer.Type.Index, 3, new int[]
        {
            0, 2, 1,
            0, 3, 2
        });
        this.setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]
        {
            0, 0,
            1, 0,
            1, 1,
            0, 1
        });

        this.updateBound();
    }
}
