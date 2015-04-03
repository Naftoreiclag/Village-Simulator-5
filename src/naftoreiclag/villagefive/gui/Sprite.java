/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.util.math.Vec2;

public final class Sprite extends Element
{
    protected Picture picture;
    protected Texture texture;
    
    public Sprite(String file)
    {
        
        picture = new Picture(file);
        Material background = new Material(SAM.ASSETS, "Common/MatDefs/Misc/Unshaded.j3md");
        texture = SAM.ASSETS.loadTexture(file);
        texture.setWrap(Texture.WrapMode.Repeat);
        background.setTexture("ColorMap", texture);
        background.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        picture.setMaterial(background);
        picture.setLocalScale((float) texture.getImage().getWidth(), (float) texture.getImage().getHeight(), 1);
        
        this.width = texture.getImage().getWidth();
        this.height = texture.getImage().getHeight();
        this.setOriginMid();
    }
    
    
    @Override
    public void updateSpatial()
    {
        picture.setLocalTranslation(
                (float) (absLoc.getX() - (origin.getX() * this.absScale)), 
                (float) (absLoc.getY() - (origin.getY() * this.absScale)), 
                (float) depth);
        picture.setLocalScale((float) (this.width * this.absScale), (float) (this.height * this.absScale), 1);
        
        
        if(plane != null)
        {
            plane.needUpdate();
        }
    }

    @Override
    public boolean collides(Vec2 absPoint)
    {
        return false;
    }

}
