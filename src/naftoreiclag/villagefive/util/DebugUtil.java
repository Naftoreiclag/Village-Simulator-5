/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DebugUtil {
    
    public static void image(BufferedImage image) {
        try {
            ImageIO.write(image, "png", new File("debug123.png"));
        } catch(Exception _) {}
    }

}
