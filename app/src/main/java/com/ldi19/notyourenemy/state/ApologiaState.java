package com.ldi19.notyourenemy.state;

import android.content.Context;

import com.ldi19.notyourenemy.app.SlidePair;

import java.util.ArrayList;

public class ApologiaState {

    private static String currentArgument;
    private static int currentSlide;
    private static boolean flowchartBackwardMovement = false;

    private static ArrayList<SlidePair> previousArgumentSlidePairList;


    /***********************************************************************************************
     *  SET APP STATE
     **********************************************************************************************/

    public static void setCurrentArgument(String defendant) { currentArgument = defendant; }
    public static void setCurrentSlideNumber(int slide) { currentSlide = slide; }
    public static void setFlowchartBackwardMovement(boolean pressed) { flowchartBackwardMovement = pressed; }

    public static void addPreviousSlidePair(String previousArgument, int previousSlide){
        if (previousArgumentSlidePairList == null) {
            previousArgumentSlidePairList = new ArrayList<SlidePair>();
        }
        previousArgumentSlidePairList.add(new SlidePair(previousArgument, previousSlide));
    }


    /***********************************************************************************************
     *  GET ERROR STATE
     **********************************************************************************************/

    public static String getCurrentArgument() { return currentArgument; }
    public static int getCurrentSlideNumber() { return currentSlide; }
    public static boolean getFlowchartBackwardMovement() { return flowchartBackwardMovement; }

    public static SlidePair popLastSlide(){ return previousArgumentSlidePairList.remove(previousArgumentSlidePairList.size() - 1); }
    public static SlidePair peekLastSlide(){ return previousArgumentSlidePairList.get(previousArgumentSlidePairList.size() - 1); }
    public static boolean areSlidesEmpty() { return previousArgumentSlidePairList == null || previousArgumentSlidePairList.isEmpty(); }
    public static boolean willBackProduceArgumentChange() {
        if (!previousArgumentSlidePairList.isEmpty()) {
            if (! peekLastSlide().getArgument().equalsIgnoreCase(currentArgument) ) {
                return true;
            }
        }
        return false;
    }

    public static int percentOfSlideProgress(Context theContext, String theArgument) {
        int totalSlidesForArgument = ApologiaDataInterface.getNumberOfSlidesForArgument(theContext, IdeologyState.getYouIdeology(), theArgument);
        int adjustedCurrentSlide = (currentSlide + 1) * 100;
        return adjustedCurrentSlide / totalSlidesForArgument;
    }


    /***********************************************************************************************
     *  CLEAR STATE
     **********************************************************************************************/

    public static void clearCurrentSlide() {
        currentArgument = null;
        currentSlide = -1;
    }

    public static void clearApologiaState() {
        currentArgument = null;
        previousArgumentSlidePairList = null;
        currentSlide = -1;
    }
}
