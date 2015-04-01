/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

// APUS_
import java.util.ArrayList;
import java.util.List;

// This class is used by the console to keep track of outputted lines in some kind of list.
// Functionally, new lines get added to the front of the list, and previous lines all get shifted to the back.
// Internally, it is the starting position that gets incremented. This is slightly more efficient.
public final class HistoryArray<T> {
    
    private int length = 0;
    private final int maxSize;
    private int nextInput = 0;
    private List<T> array = new ArrayList<T>();

    public HistoryArray(int size) {
        this.maxSize = size;
        clear();
    }

    public void add(T thing) {
        array.set(nextInput, thing);
        nextInput = wrap(nextInput + 1);
        
        if(length < maxSize) { ++ length; }
    }

    public T get(int pos) {
        if(pos >= maxSize || pos < 0) {
            return null;
        } else {
            return array.get(wrap(nextInput - 1 - pos));
        }
    }

    // Used internally to determine where the next write position should be in this circular list.
    private int wrap(int x) {
        return ((x % maxSize) + maxSize) % maxSize;
    }

    // What it says on the tin.
    public void clear() {
        array.clear();
        for(int i = 0; i < maxSize; ++ i) {
            array.add(null);
        }
        length = 0;
    }

    public int getMaxSize() {
        return maxSize;
    }
    
    // The number of non-null elements currently held in the list. Can be at most maxSize;
    public int getLength() {
        return length;
    }
}
