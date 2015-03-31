/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.util;

import java.util.ArrayList;
import java.util.List;

// APUS_
public class HistoryArray<T> {
    
    final public int size;
    private int nextInput = 0;
    List<T> array = new ArrayList<T>();

    public HistoryArray(int size) {
        this.size = size;
        
        for(int i = 0; i < size; ++ i) {
            array.add(null);
        }
    }

    public int wrap(int x) {
        return ((x % size) + size) % size;
    }

    public void add(T thing) {
        array.set(nextInput, thing);

        nextInput = wrap(nextInput + 1);
    }

    public void clear() {
        array.clear();
    }

    public T get(int pos) {
        if(pos >= size || pos < 0) {
            return null;
        } else {
            return array.get(wrap(nextInput - 1 + pos));
        }
    }

    public void set(int pos, T thing) {
        if(pos < size && pos >= 0) {
            array.set(wrap(nextInput - 1 + pos), thing);
        }
    }

    public int getSize() {
        return size;
    }
}
