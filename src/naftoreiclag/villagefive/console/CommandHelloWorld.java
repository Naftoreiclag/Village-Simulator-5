/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import java.util.List;
import naftoreiclag.villagefive.world.World;

public class CommandHelloWorld extends Command {

    @Override
    public boolean process(Console console, String input, World world) {
        if(input.equalsIgnoreCase("helloworld")) {
            console.println("Sayin' hello world!");
            return true;
        }
        return false;
    }

    @Override
    public String[] getHelpLines() {
        return new String[]{"HELLO WORLD: Prints a message. I bet you can't guess what it is."};
    }

}
