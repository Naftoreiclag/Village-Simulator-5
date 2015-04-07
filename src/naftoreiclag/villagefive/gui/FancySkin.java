/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import naftoreiclag.villagefive.SAM;
import static naftoreiclag.villagefive.gui.BitmapFont.getXIndex;
import static naftoreiclag.villagefive.gui.BitmapFont.getYIndex;
import naftoreiclag.villagefive.util.math.Vec2;
import org.lwjgl.BufferUtils;

public class FancySkin extends Element {
    
    protected Geometry box;
    protected final Texture texture;
    protected Material material;
    
    public FancySkin(Texture texture, double width, double height) {
        super(width, height);
        
        this.texture = texture;
        
        box = new Geometry("Text", meshFor(width, height, 64, 64, 10, 10, 10, 10));
        box.setQueueBucket(RenderQueue.Bucket.Gui);
        box.setCullHint(Spatial.CullHint.Never);
        
        material = new Material(SAM.ASSETS, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", texture);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        box.setMaterial(material);
        this.setOriginMid();
        
    }
    
    @Override
    public boolean collides(Vec2 absPoint) {
        return false;
    }

    @Override
    public void updateSpatial() {
        box.setLocalTranslation(
                (float) (absLoc.getX() - (origin.getX() * this.absScale.getX())),
                (float) (absLoc.getY() - (origin.getY() * this.absScale.getY())),
                (float) depth);
        box.setLocalScale((float) (this.absScale.getX()), (float) (this.absScale.getY()), 1);

        if(plane != null) {
            plane.updateSceneGraph();
        }
    }

    @Override
    public boolean hasSpatial() {
        return true;
    }

    @Override
    protected Spatial getSpatial() {
        return box;
    }

    private static Mesh meshFor(double width, double height, double imgWidth, double imgHeight, double leftMargin, double rightMargin, double topMargin, double bottomMargin) {
        FloatBuffer v = BufferUtils.createFloatBuffer(16 * 3);
        FloatBuffer t = BufferUtils.createFloatBuffer(16 * 2);
        IntBuffer indicies = BufferUtils.createIntBuffer(9 * 2 * 3);
        
        /*
         * Tryhard ascii art
         * 
         * h1--12---13---------14---15
         *      |    |          |    |
         * HD---8----9---------10---11
         *      |    |          |    |
         *      |    |          |    |
         *      |    |          |    |
         *      |    |          |    |
         * GC---4----5----------6----7
         *      |    |          |    |
         *  0---0----1----------2----3
         *      |    |          |    |
         *      0    A          B    1
         *           E          F    w
         */
        
        // Texture pos
        float A = (float) (leftMargin / imgWidth);
        float B = (float) ((imgWidth - rightMargin) / imgWidth);
        float C = (float) (bottomMargin / imgHeight);
        float D = (float) ((imgHeight - topMargin) / imgHeight);
        
        float E = (float) (leftMargin);
        float F = (float) (width - rightMargin);
        float G = (float) (bottomMargin);
        float H = (float) (height - topMargin);
        float w = (float) (width);
        float h = (float) (height);
        
        t.put(0).put(0);
        t.put(A).put(0);
        t.put(B).put(0);
        t.put(1).put(0);
        t.put(0).put(C);
        t.put(A).put(C);
        t.put(B).put(C);
        t.put(1).put(C);
        t.put(0).put(D);
        t.put(A).put(D);
        t.put(B).put(D);
        t.put(1).put(D);
        t.put(0).put(1);
        t.put(A).put(1);
        t.put(B).put(1);
        t.put(1).put(1);
        
        v.put(0).put(0).put(0);
        v.put(E).put(0).put(0);
        v.put(F).put(0).put(0);
        v.put(w).put(0).put(0);
        v.put(0).put(G).put(0);
        v.put(E).put(G).put(0);
        v.put(F).put(G).put(0);
        v.put(w).put(G).put(0);
        v.put(0).put(H).put(0);
        v.put(E).put(H).put(0);
        v.put(F).put(H).put(0);
        v.put(w).put(H).put(0);
        v.put(0).put(h).put(0);
        v.put(E).put(h).put(0);
        v.put(F).put(h).put(0);
        v.put(w).put(h).put(0);
        
        indicies.put(0).put(1).put(5);
        indicies.put(0).put(5).put(4);
        
        indicies.put(1).put(2).put(6);
        indicies.put(1).put(6).put(5);
        
        indicies.put(2).put(3).put(7);
        indicies.put(2).put(7).put(6);
        
        indicies.put(4).put(5).put(9);
        indicies.put(4).put(9).put(8);
        
        indicies.put(5).put(6).put(10);
        indicies.put(5).put(10).put(9);
        
        indicies.put(6).put(7).put(11);
        indicies.put(6).put(11).put(10);
        
        indicies.put(8).put(9).put(13);
        indicies.put(8).put(13).put(12);
        
        indicies.put(9).put(10).put(14);
        indicies.put(9).put(14).put(13);
        
        indicies.put(10).put(11).put(15);
        indicies.put(10).put(15).put(14);
        
        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, t);
        mesh.setBuffer(VertexBuffer.Type.Index,    3, indicies);
        mesh.updateBound();
        
        return mesh;
        
    }

}
