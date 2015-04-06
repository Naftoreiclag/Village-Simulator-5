/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */
package naftoreiclag.villagefive.gui;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import naftoreiclag.villagefive.SAM;
import naftoreiclag.villagefive.util.math.Vec2;

public final class Sprite extends Element {
    protected Picture picture;
    protected Texture texture;

    public Sprite(Texture texture) {
        super(texture.getImage().getWidth(), texture.getImage().getHeight());
        this.texture = texture;
        picture = new Picture("Sprite");
        Material background = new Material(SAM.ASSETS, "Common/MatDefs/Misc/Unshaded.j3md");
        background.setTexture("ColorMap", texture);
        background.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        picture.setMaterial(background);


        this.setOriginMid();
    }

    @Override
    public void updateSpatial() {
        picture.setLocalTranslation(
                (float) (absLoc.getX() - (origin.getX() * this.absScale.getX())),
                (float) (absLoc.getY() - (origin.getY() * this.absScale.getY())),
                (float) depth);
        picture.setLocalScale((float) (this.width * this.absScale.getX()), (float) (this.height * this.absScale.getY()), 1);


        if(plane != null) {
            plane.updateSceneGraph();
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
        return picture;
    }
}
