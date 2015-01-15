/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import java.util.List;

public class Plugin {

    public final String name;
    public final String namespace;
    public final String description;
    
    public List<PluginEntity> entities;
    
    Plugin(String name, String namespace, String description)
    {
        this.name = name;
        this.namespace = namespace;
        this.description = description;
    }

}
