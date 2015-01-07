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
    private Vec2 loc = new Vec2(0, 0);
    
    public final int width;
    public final int height;
    
    private Vec2 origin = new Vec2(0, 0);
    
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
        picture.setPosition(0, 0);
    }
    
    public void setOrigin(double x, double y)
    {
        this.origin.set(x, y);
        updateLoc();
    }
    public void setOrigin(float x, float y)
    {
        this.origin.set(x, y);
        updateLoc();
    }
    public void setOrigin(Vec2 newLoc)
    {
        this.origin.set(newLoc);
        updateLoc();
    }

    public void setOriginMid()
    {
        this.origin.set(width / 2d, height / 2d);
        updateLoc();
    }
    
    public void setLoc(double x, double y)
    {
        this.loc.set(x, y);
        updateLoc();
    }
    public void setLoc(float x, float y)
    {
        this.loc.set(x, y);
        updateLoc();
    }
    public void setLoc(Vec2 newLoc)
    {
        this.loc.set(newLoc);
        updateLoc();
    }
    
    private void updateLoc()
    {
        Vec2 fin = loc.subtract(origin);
        
        picture.setPosition(fin.getXF(), fin.getYF());
        picture.getParent().updateGeometricState();
    }
}
