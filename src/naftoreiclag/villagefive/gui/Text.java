/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import java.awt.Font;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.util.math.Vec2;

public final class Text extends Element
{
    protected Geometry textBox;
    protected String text;
    
    public Text(BitmapFont font, String text)
    {
        super(font.getWidth(text), font.getHeight(text));
        this.text = text;
        textBox = new Geometry("Text", font.meshFor(text));
        textBox.setQueueBucket(RenderQueue.Bucket.Gui);
        textBox.setCullHint(CullHint.Never);
        Material background = new Material(SAM.ASSETS, "Common/MatDefs/Misc/Unshaded.j3md");
        background.setTexture("ColorMap", font.texture);
        background.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        textBox.setMaterial(background);
        //textBox.setMaterial(SAM.ASSETS.loadMaterial("Materials/debug.j3m"));
        
        this.setOriginMid();
    }
    
    
    @Override
    public void updateSpatial() {
        textBox.setLocalTranslation(
                (float) (absLoc.getX() - (origin.getX() * this.absScale.getX())), 
                (float) (absLoc.getY() - (origin.getY() * this.absScale.getY())), 
                (float) depth);
        textBox.setLocalScale((float) (this.absScale.getX()), (float) (this.absScale.getY()), 1);
        
        if(plane != null) {
            plane.needUpdate();
        }
    }

    @Override
    public boolean collides(Vec2 absPoint) {
        return false;
    }

    @Override
    public boolean hasSpatial() {
        return true;
    }

    @Override
    protected Spatial getSpatial() {
        return textBox;
    }
}
