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
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import jme3tools.converters.ImageToAwt;
import naftoreiclag.villagefive.util.DebugUtil;
import static naftoreiclag.villagefive.util.scenegraph.GrassMaker.HEX_X;
import static naftoreiclag.villagefive.util.scenegraph.GrassMaker.HEX_Z;
import org.lwjgl.BufferUtils;

public class BitmapFont {
    
    BufferedImage image;
    Texture texture;
    
    private static char defaultChar = 8;
    private static char spaceChar = ' ';
    private static char getSpaceWidthFrom = 'w';
    private static int spacing = 2;
    
    private final int[] widths;
    private final int charHeight;
    
    private int totalW;
    private int totalH;
    
    private int cellW;
    private int cellH;
    
    private static int numGlyphColumns = 16;
    private static int numGlyphRows = 16;
    
    public BitmapFont(Texture texture) {
        this.texture = texture;
        
        this.image = ImageToAwt.convert(texture.getImage(), false, true, 0);
        totalW = image.getWidth();
        totalH = image.getHeight();
        
        cellW = totalW / numGlyphColumns;
        cellH = totalH / numGlyphRows;
        
        charHeight = cellH / 2 + 8;
        
        widths = new int[256];
        
        for(int y = 0; y < numGlyphRows; ++ y) {
            for(int x = 0; x < numGlyphColumns; ++ x) {
                
                int glyphW = 0;
                for(int pX = 0; pX < cellW; ++ pX) {
                    
                    // Remembers if every pixel in this column is empty
                    boolean columnIsEmpty = true;
                    for(int pY = 0; pY < cellH; ++ pY) {
                        int alpha = getAlpha(image, x, y, pX, pY);
                        
                        // If this pixel is opaque, then this column is not empty.
                        if(alpha > 0) {
                            columnIsEmpty = false;
                            break;
                        }
                    }
                    
                    // If there is stuff in this column, then the width of the glyph must be at least pX + 1
                    if(!columnIsEmpty) {
                        glyphW = pX + 1;
                        // Do not break here, since there could still be pixels farther to the right.
                    }
                }
                widths[xyToGlyphIndex(x, y)] = glyphW;
            }
        }
        
        // Space character
        if(widths[spaceChar] == 0) {
            widths[spaceChar] = widths[getSpaceWidthFrom];
        }
    }
    
    public int getWidth(String text) {
        
        int total = 0;
        char[] data = text.toCharArray();
        for(char c : data) {
            if(c == '\n') {
                continue;
            }
            if(c >= 256) {
                c = defaultChar;
            }
            
            total += widths[c] + spacing;
            
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
    
    private int getAlpha(BufferedImage image, int x, int y, int pX, int pY) {
        
        int xCoord = x * cellW;
        int yCoord = y * cellH;
        xCoord += pX;
        yCoord += pY;
        
        // Flip
        yCoord = (image.getHeight() - 1) - yCoord;
        
        return (image.getRGB(xCoord, yCoord) >> 24) & 0xff;
    }
    
    private static int xyToGlyphIndex(int x, int y) {
        return (y * numGlyphColumns) + x;
    }
    
    public static int getXIndex(int index) {
        return index % 16;
    }
    
    public static int getYIndex(int index) {
        return (index - getXIndex(index)) / 16;
    }
    
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
            if(c == '\n') {
                xOff = 0;
                yOff -= charHeight;
                continue;
            }
            if(c >= 256) {
                c = defaultChar;
            }
            
            v.put(xOff).put(yOff).put(0);
            v.put(xOff).put(yOff + cellH).put(0);
            v.put(xOff + cellW).put(yOff + cellH).put(0);
            v.put(xOff + cellW).put(yOff).put(0);
            
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
            
            xOff += widths[c] + spacing;
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
