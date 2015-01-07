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

public final class Sprite extends Element
{
    protected Picture picture;
    
    public Sprite(String file)
    {
        picture = new Picture(file);
        Material background = new Material(SAM.a, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = SAM.a.loadTexture(file);
        texture.setWrap(Texture.WrapMode.Repeat);
        background.setTexture("ColorMap", texture);
        background.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        picture.setMaterial(background);
        
        this.width = texture.getImage().getWidth();
        this.height = texture.getImage().getHeight();
        
        picture.setWidth(width);
        picture.setHeight(height);
        this.setOriginMid();
    }
    
    
    @Override
    public void whereSpatialWouldHaveBeenUpdated()
    {
        picture.setLocalTranslation(absLoc.getXF() - origin.getXF(), absLoc.getYF() - origin.getYF(), (float) depth);
        
        if(plane != null)
        {
            plane.needUpdate();
        }
    }
}
