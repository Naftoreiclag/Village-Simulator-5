/* Copyright (c) 2014-2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util.math;

import java.util.Random;

public class GR
{
    public static Random r = new Random();

    /* Delta is time in seconds that have passed.
     * Probability is the chance that true would be returned every second.
     * This method simulates if true would have been returned given a certain delta.
     * 
     * Probability of 0.5   occurs about every 2 deltas
     * Probability of 0.333 occurs about every 3 deltas
     * 
     * Probability is like hertz.
     * 
     * Note: this function becomes inaccurate if 1/hertz < 1, since on average it would have happened multiple times per second.
     * 
     */
    public static boolean chanceHertz(double delta, double probabilityHertz)
    {
        return r.nextDouble() > Math.pow(1 - probabilityHertz, delta);
    }
}
