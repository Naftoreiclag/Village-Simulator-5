/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import java.awt.image.BufferedImage;
import jme3tools.converters.ImageToAwt;
import naftoreiclag.villagefive.SAM;

public class AssetsLoaderUtil {
    @Deprecated
    public static BufferedImage loadImage(String path) {
        
        BufferedImage raw = ImageToAwt.convert(SAM.ASSETS.loadTexture(path).getImage(), false, true, 0);
        
        BufferedImage ret = new BufferedImage(raw.getWidth(), raw.getHeight(), raw.getType());
        
        ret.getGraphics().drawImage(raw, 0, ret.getHeight(), ret.getWidth(), -ret.getHeight(), null);
        
        return ret;
    }
}
