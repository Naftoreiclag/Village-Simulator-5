/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import jme3tools.converters.ImageToAwt;
import static naftoreiclag.villagefive.util.scenegraph.GrassMaker.HEX_X;
import static naftoreiclag.villagefive.util.scenegraph.GrassMaker.HEX_Z;
import org.lwjgl.BufferUtils;

public class BitmapFont {
    
    BufferedImage image;
    Texture texture;
    
    static char defaultChar = 8;
    static char spaceChar = ' ';
    static char getSpaceWidthFrom = 'W';
    static int spacing = 2;
    
    public final int[] widths;
    
    int cellW;
    int cellH;
    
    public BitmapFont(Texture texture) {
        this.texture = texture;
        
        this.image = ImageToAwt.convert(texture.getImage(), false, true, 0);
        
        cellW = image.getWidth() / 8;
        cellH = image.getHeight() / 8;
        
        widths = new int[256];
        for(int i = 0; i < 256; ++ i) {
            
            int glyphW = cellW;
            for(int x = 0; x < cellW; ++ x) {
                
                boolean columnIsEmpty = true;
                for(int y = 0; y < cellH; ++ y) {
                    if((image.getRGB(getXIndex(i) + x, getYIndex(i) + y) & 0x000000FF) != 0) {
                        columnIsEmpty = false;
                        break;
                    }
                }
                
                if(columnIsEmpty) {
                    glyphW = 0;
                    break;
                }
            }
            
            widths[i] = glyphW + spacing;
        }
        
        // Space character
        if(widths[spaceChar] - spacing == 0) {
            widths[spaceChar] = widths[getSpaceWidthFrom];
        }
    }
    
    public int getWidth(String text) {
        
        int total = 0;
        char[] data = text.toCharArray();
        for(char c : data) {
            if(c >= 256) {
                c = defaultChar;
            }
            
            total += widths[c];
        }
        
        return total;
    }
    public int getHeight(String text) {
        return (countNewlineChars(text) + 1) * this.cellH;
    }
    public static int countNewlineChars(String text) {
        int total = 1;
        char[] data = text.toCharArray();
        for(char c : data) {
            if(c == '\n') {
                total ++;
            }
        }
        
        return total;
    }
    
    public static int getXIndex(int index) {
        return index % 8;
    }
    
    public static int getYIndex(int index) {
        return index / 8;
    }

    public static float[] glyphTexCoords = new float[]{
        0, 1,
        0, 0,
        1, 0,
        1, 1,
    };
    
    public Mesh meshFor(String text) {
        char[] data = text.toCharArray();
        int numGlyphs = data.length;// - countNewlineChars(text);
        FloatBuffer v = BufferUtils.createFloatBuffer(1 * 4 * 3);
        FloatBuffer n = BufferUtils.createFloatBuffer(1 * 4 * 3);
        FloatBuffer t = BufferUtils.createFloatBuffer(1 * 4 * 2);
        IntBuffer indicies = BufferUtils.createIntBuffer(1 * 2 * 3);
        
        v.put(0).put(0).put(0);
        v.put(0).put(1).put(0);
        v.put(1).put(1).put(0);
        v.put(1).put(0).put(0);
        n.put(0).put(0).put(0);
        n.put(0).put(1).put(0);
        n.put(1).put(1).put(0);
        n.put(1).put(0).put(0);
        
        t.put(0).put(1);
        t.put(0).put(0);
        t.put(1).put(0);
        t.put(1).put(1);
        
        
        indicies.put(0).put(2).put(1);
        indicies.put(0).put(3).put(2);
        /*
        // 3 vertexes, 3 floats each
        FloatBuffer v = BufferUtils.createFloatBuffer(numGlyphs * 4 * 3);
        FloatBuffer t = BufferUtils.createFloatBuffer(numGlyphs * 4 * 2);
        IntBuffer indicies = BufferUtils.createIntBuffer(numGlyphs * 2 * 3);
        
        int xOff = 0;
        int yOff = 0;
        
        // 1   2
        // 
        // 0   3
        
        int ind = 0;
        for(char c : data) {
            if(c >= 256) {
                c = defaultChar;
            }
            
            v.put(xOff).put(yOff).put(0);
            v.put(xOff).put(yOff + cellH).put(0);
            v.put(xOff + widths[c]).put(yOff + cellH).put(0);
            v.put(xOff + widths[c]).put(yOff).put(0);
            
            t.put(0).put(getYIndex(c));
            t.put(0).put(0);
            t.put(getXIndex(c)).put(0);
            t.put(getXIndex(c)).put(getYIndex(c));
            
            indicies.put(ind    ).put(ind + 2).put(ind + 1);
            indicies.put(ind    ).put(ind + 3).put(ind + 2);
            
            xOff += widths[c];
            ind += 4;
        }
        */
                
        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);
        mesh.setBuffer(VertexBuffer.Type.Normal,   3, n);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, t);
        mesh.setBuffer(VertexBuffer.Type.Index,    3, indicies);
        mesh.updateBound();
          
        /*
        System.out.println("grefewarhgr " + text);
        System.out.println("grefewarhgr " + numGlyphs);
        System.out.println("grefewarhgr " + xOff);
        System.out.println("grefewarhgr " + ind);
        */
        return mesh;
    }
}
