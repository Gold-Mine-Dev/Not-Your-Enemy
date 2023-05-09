package com.ldi19.notyourenemy.app;

/**
 * Created by macair on 19/07/2018.
 */

public class SlidePair {

    private final String theArgument;
    private final int theSlideNumber;

    public SlidePair(String argument, int slideNumber) {
        this.theArgument = argument;
        this.theSlideNumber = slideNumber;
    }

    public String getArgument() { return theArgument; }

    public int getSlideNumber() { return theSlideNumber; }
}