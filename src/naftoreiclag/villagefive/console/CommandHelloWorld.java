/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import java.util.List;

public class CommandHelloWorld extends Command {

    @Override
    public boolean process(Console console, String input) {
        if(input.equalsIgnoreCase("helloworld")) {
            console.println("Sayin' hello world!");
            return true;
        }
        return false;
    }

    @Override
    public String[] getHelpLines() {
        return new String[]{"helloworld: Prints a message. I bet you can't guess what it is."};
    }

}
