/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import naftoreiclag.villagefive.util.math.Vec2;

public final class Sprite
{
    protected Picture picture;
    public Vec2 pos = new Vec2(0, 0);
    
    public Sprite(String file)
    {
        picture = new Picture(file);
        Material background = new Material(SAM.a, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = SAM.a.loadTexture(file);
        texture.setWrap(Texture.WrapMode.Repeat);
        background.setTexture("ColorMap", texture);
        background.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        picture.setMaterial(background);
        picture.setWidth(texture.getImage().getWidth());
        picture.setHeight(texture.getImage().getHeight());
        picture.setPosition(0, 0);
    }
    
    public void setPos(double x, double y)
    {
        this.pos.set(x, y);
        updatePos();
    }
    public void setPos(float x, float y)
    {
        this.pos.set(x, y);
        updatePos();
    }
    public void setPos(Vec2 newPos)
    {
        this.pos.set(newPos);
        updatePos();
    }
    
    private void updatePos()
    {
        picture.setPosition(pos.getXF(), pos.getYF());
        picture.getParent().updateGeometricState();
    }
}
