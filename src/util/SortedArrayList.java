package util;

import java.util.ArrayList;
import java.util.Collections;

public class SortedArrayList<T> extends ArrayList<T> {

    /**
     * Inserts value into sorted position in this ArrayList
     * Taken from https://stackoverflow.com/questions/4031572/sorted-array-list-in-java  
     * @param value - Value to be added
     */
    public void insertSorted(T value) {
        add(value);
        Comparable<T> cmp = (Comparable<T>) value;
        for (int i = size()-1; i > 0 && cmp.compareTo(get(i-1)) < 0; i--)
            Collections.swap(this, i, i-1);
    }
}
