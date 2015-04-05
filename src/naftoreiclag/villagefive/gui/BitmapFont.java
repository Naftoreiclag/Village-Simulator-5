/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.gui;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.tools.Color;
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
        
        cellW = image.getWidth() / 16;
        cellH = image.getHeight() / 16;
        
        //System.out.println("fewfewf:" + image.getColorModel());
        
        widths = new int[256];
        for(char i = 0; i < 256; ++ i) {
                    //System.out.println(getXIndex(i) + ", " + getYIndex(i));
            
            int glyphW = cellW;
            for(int x = 0; x < cellW; ++ x) {
                
                boolean columnIsEmpty = true;
                for(int y = 0; y < cellH; ++ y) {
                    
                    int alpha = (image.getRGB(getXIndex(i) * cellW + x, image.getHeight() - (getYIndex(i) * cellH + y) >> 24)) & 0xff;
                    
                    
                    if(alpha > 0) {
                        columnIsEmpty = false;
                        break;
                    }
                }
                
                if(columnIsEmpty) {
                    glyphW = x;
                    break;
                }
            }
            
            if(glyphW > 0) {
                System.out.println(glyphW);
                System.out.println(i);
            }
            
            widths[i] = glyphW + spacing;
        }

        for(int y = 0; y < cellH; ++ y) {

            for(int x = 0; x < cellW; ++ x) {

                    int alpha = (image.getRGB(getXIndex('R') * cellW + x, image.getHeight() - (getYIndex('R') * cellH + y)) >> 24) & 0xff;
                    
                    System.out.print(alpha > 0 ? '#' : ' ');
            }
            System.out.println();
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
        return index % 16;
    }
    
    public static int getYIndex(int index) {
        return index / 16;
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
        
        // 3 vertexes, 3 floats each
        FloatBuffer v = BufferUtils.createFloatBuffer(numGlyphs * 4 * 3);
        FloatBuffer t = BufferUtils.createFloatBuffer(numGlyphs * 4 * 2);
        IntBuffer indicies = BufferUtils.createIntBuffer(numGlyphs * 2 * 3);
        
        int xOff = 0;
        int yOff = 0;
        
        //   C   D
        // A 1   2
        //  
        // B 0   3
        
        int ind = 0;
        for(char c : data) {
            if(c >= 256) {
                c = defaultChar;
            }
            
            v.put(xOff).put(yOff).put(0);
            v.put(xOff).put(yOff + cellH).put(0);
            v.put(xOff + widths[c]).put(yOff + cellH).put(0);
            v.put(xOff + widths[c]).put(yOff).put(0);
            
            float C = ((float) getXIndex(c)) / 16f;
            float D = ((float) getXIndex(c) + 1) / 16f;
            float A = ((float) getYIndex(c)) / 16f;
            float B = ((float) getYIndex(c) + 1) / 16f;
            
            t.put(C).put(1-B);
            t.put(C).put(1-A);
            t.put(D).put(1-A);
            t.put(D).put(1-B);
            
            indicies.put(ind    ).put(ind + 2).put(ind + 1);
            indicies.put(ind    ).put(ind + 3).put(ind + 2);
            
            xOff += widths[c];
            ind += 4;
        }
                
        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, v);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, t);
        mesh.setBuffer(VertexBuffer.Type.Index,    3, indicies);
        mesh.updateBound();
        
        return mesh;
    }
}
