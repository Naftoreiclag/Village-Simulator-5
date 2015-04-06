/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import javax.imageio.ImageIO;

public class BitmapFontFixer {
    private static int numGlyphColumns = 16;
    private static int numGlyphRows = 16;
    public static void fix(String path) {
        try
        {
            BufferedImage image = ImageIO.read(new File(path));
            
            int totalW = image.getWidth();
            int totalH = image.getHeight();
            int cellW = totalW / numGlyphColumns;
            int cellH = totalH / numGlyphRows;
            BufferedImage image2 = new BufferedImage(totalW, totalH, IndexColorModel.TRANSLUCENT);
            
            
            Graphics2D g = image2.createGraphics();
            for(int x = 0; x < numGlyphColumns * cellW; x += cellW) {
                for(int y = 0; y < numGlyphRows * cellH; y += cellH) {
                    
                    int unnecessaryPadding = 0;
                    
                    for(int xi = 0; xi < cellW; ++ xi) {
                        
                        boolean columnEmpty = true;
                        for(int yi = 0; yi < cellH; ++ yi) {
                            
                            int alpha = (image.getRGB(x + xi, y + yi) >> 24) & 0xff;
                            
                            if(alpha > 0) {
                                columnEmpty = false;
                                break;
                            }
                        }
                        
                        if(!columnEmpty) {
                            unnecessaryPadding = xi;
                            break;
                        }
                        
                    }
                    
                    g.drawImage(image, x - unnecessaryPadding, y, x + cellW - unnecessaryPadding, y + cellH, x, y, x + cellW, y + cellH, null);
                    
                }
            }
            
            DebugUtil.image(image2);
            
        
        } catch(Exception e) {}
    }
}
