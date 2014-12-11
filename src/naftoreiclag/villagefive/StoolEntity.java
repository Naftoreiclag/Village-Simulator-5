/* Copyright (c) 2014 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

public class StoolEntity extends Entity
{
    @Override
    public void meow()
    {
        System.out.println("im a stool");
    }

    @Override
    public String getModelName()
    {
        return "Models/stool/Stool.j3o";
    }

    @Override
    public void tick(float tpf)
    {
    }
}
