package com.example.siulkilulki.findsmsmessage;

/**
 * Created by siulkilulki on 19.01.16.
 */
public class Tuple<X, Y> {
    public X first;
    public Y second;

    public Tuple(X first, Y second) {
        this.first = first;
        this.second = second;
    }
    public Tuple() {
        this.first = null;
        this.second = null;
    }
}