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
import java.awt.Font;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.util.math.Vec2;

public final class Text extends Element
{
    protected Geometry picture;
    protected String text;
    
    public Text(BitmapFont font, String text)
    {
        super(font.getWidth(text), font.getHeight(text));
        this.text = text;
        picture = new Geometry("Text", font.meshFor(text));
        Material background = new Material(SAM.ASSETS, "Common/MatDefs/Misc/Unshaded.j3md");
        background.setTexture("ColorMap", font.texture);
        background.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        picture.setMaterial(background);
        
        this.setOriginMid();
    }
    
    
    @Override
    public void updateSpatial()
    {
        picture.setLocalTranslation(
                (float) (absLoc.getX() - (origin.getX() * this.absScale.getX())), 
                (float) (absLoc.getY() - (origin.getY() * this.absScale.getY())), 
                (float) depth);
        picture.setLocalScale((float) (this.width * this.absScale.getX()), (float) (this.height * this.absScale.getY()), 1);
        
        
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
