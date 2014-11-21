package naftoreiclag.villagefive;

import java.util.ArrayList;
import java.util.List;

/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

public class Space
{
    List<CircleBody> bodies = new ArrayList<CircleBody>();
    
    void update(float tpf)
    {
        for(CircleBody bod : bodies)
        {
            bod.loc.x += bod.mot.x * tpf;
            bod.loc.y += bod.mot.y * tpf;
            
            bod.onLocationChange();
        }
    }

    void attachBody(CircleBody body)
    {
        bodies.add(body);
    }
}
