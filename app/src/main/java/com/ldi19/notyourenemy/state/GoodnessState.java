package com.ldi19.notyourenemy.state;

import android.content.Context;

import java.util.ArrayList;

public class GoodnessState {

    /****************************************************************************************
     ** MARK:: Static Variables
     ****************************************************************************************/
    private static int currentClause = -1;
    private static int currentSlide = -1;
    private static ArrayList<Integer> exhaustedClauses;
    private static ArrayList<String> descriptorAdjectives;
    private static ArrayList<String> descriptorNouns;

    /****************************************************************************************
     ** MARK:: Back Variables
     ****************************************************************************************/

    private static ArrayList<Integer> previousSlides;
    private static ArrayList<Integer> previousClauses;

    /****************************************************************************************
     ** MARK:: Argument Constants
     ****************************************************************************************/
    private static float fontFactor0 = 1.00f; //100%
    private static float fontFactor1 = 0.80f; //80%
    private static float fontFactor2 = 0.70f; //70%
    private static float fontFactor3 = 0.60f; //60%


    public static void initalizeGoodnessState(Context interfaceContext) {
        GoodnessDataInterface.initializeInterface(interfaceContext);
        currentClause = -1;
        currentSlide = -1;
        exhaustedClauses = new ArrayList<>();
        descriptorAdjectives = new ArrayList<>();
        descriptorNouns = new ArrayList<>();
        previousSlides = new ArrayList<>();
        previousClauses = new ArrayList<>();
        nextClause();
    }

    public static void clearGoodnessState() {
        currentClause = -1;
        currentSlide = -1;
        exhaustedClauses = new ArrayList<>();
        descriptorAdjectives = new ArrayList<>();
        descriptorNouns = new ArrayList<>();
        previousSlides = new ArrayList<>();
        previousClauses = new ArrayList<>();
    }

    public static String getSlideText() {
        return slideText();
    }

    public static float getFontScale() {
        int fontFactor = GoodnessDataInterface.getFontFactor(currentClause, currentSlide);
        switch (fontFactor) {
            case 0:
                return fontFactor0;
            case 1:
                return fontFactor1;
            case 2:
                return fontFactor2;
            case 3:
                return fontFactor3;
            default:
                return fontFactor0;
        }
    }

    public static String getDescriptionStatement() {
        if (descriptorNouns.size() < 1) {
            return "You have committed no fault in your life (seriously?)";
        } else if (descriptorNouns.size() == 1) {
            if (isFirstCharVowel(descriptorNouns.get(0))) {
                return "According to your words, you are an <em>" + descriptorNouns.get(0) + "</em>";
            } else {
                return "According to your words, you are a <em>" + descriptorNouns.get(0) + "</em>";
            }
        } else {
            String constructedString = "";
            if (isFirstCharVowel(descriptorAdjectives.get(0))) {
                constructedString = "According to your words, you are an <em>";
            } else {
                constructedString = "According to your words, you are a <em>";
            }

            for (int i = 0; i < descriptorNouns.size() - 1; i++) {
                if (i == 0) {
                    constructedString += descriptorAdjectives.get(i);
                } else {
                    constructedString +=  ", " + descriptorAdjectives.get(i);
                }
            }
            constructedString += " " + descriptorNouns.get(descriptorNouns.size() - 1) + "</em>";

            return constructedString;
        }
    }

    public static String getDescriptionStatementShort() {
        if (descriptorNouns.size() < 1) {
            return "None";
        } else if (descriptorNouns.size() == 1) {
            return descriptorNouns.get(0);
        } else {
            String constructedString = "";
            for (int i = 0; i < descriptorNouns.size() - 1; i++) {
                if (i == 0) {
                    constructedString += descriptorAdjectives.get(i);
                } else {
                    constructedString +=  ", " + descriptorAdjectives.get(i);
                }
            }
            constructedString += " " + descriptorNouns.get(descriptorNouns.size() - 1);

            return constructedString;
        }
    }

    public static int currentPercentOfArgumentProgress() {
        int totalSlides = 0;
        int currentSlideCount = 0;

        for (int i = 0; i < GoodnessDataInterface.getNumberOfClauses(); i++) {
            totalSlides = totalSlides + GoodnessDataInterface.getNumberOfSlidesForClause(i);
        }

        for (int j = 0; j < exhaustedClauses.size(); j++) {
            currentSlideCount = currentSlideCount + GoodnessDataInterface.getNumberOfSlidesForClause(exhaustedClauses.get(j));
        }
        currentSlideCount = currentSlideCount + currentSlide;

        return (currentSlideCount * 100) / totalSlides;
    }

    public static boolean proceedWithYes() {
        if (yesForcesNextClause()) {
            return nextClause();
        } else {
            previousClauses.add(currentClause);
            previousSlides.add(currentSlide);
            currentSlide = getYes();
            return true;
        }
    }
    public static boolean proceedWithNo() {
        if (noForcesNextClause()) {
            return nextClause();
        } else {
            previousClauses.add(currentClause);
            previousSlides.add(currentSlide);
            currentSlide = getNo();
            return true;
        }
    }
    public static boolean proceedWithForward() {
        if(!getNoun().isEmpty()) {
            descriptorNouns.add(getNoun());
        }
        if(!getAdjective().isEmpty()) {
            descriptorAdjectives.add(getAdjective());
        }

        return nextClause();
    }


    // Data State Queries
    public static boolean yesButtonDisplay() { return GoodnessDataInterface.yesNextPresent(currentClause, currentSlide); }
    public static boolean noButtonDisplay() { return GoodnessDataInterface.noNextPresent(currentClause, currentSlide); }
    public static boolean numericButtonDisplay() { return GoodnessDataInterface.numericButtons(currentClause, currentSlide); }
    public static boolean forceArrowButtonDisplay() { return GoodnessDataInterface.forceArrowButton(currentClause, currentSlide); }
    public static boolean anyButtonDisplay() { return GoodnessDataInterface.anyNextPresent(currentClause, currentSlide); }
    public static boolean clausesExhausted() { return exhaustedClauses.size() == GoodnessDataInterface.getNumberOfClauses() - 1; }
    public static boolean threeDescriptorsReached() { return descriptorNouns.size() >= 3; }
    public static boolean yesForcesNextClause() { return GoodnessDataInterface.yesEndsClause(currentClause, currentSlide); }
    public static boolean noForcesNextClause() { return GoodnessDataInterface.noEndsClause(currentClause, currentSlide); }


    /****************************************************************************************
     ** MARK:: Private Ineraction Helper Methods
     ****************************************************************************************/

    private static boolean nextClause() {
        ArrayList<Integer> clausesRemaining = new ArrayList<>();;
        if(currentClause != -1) {
            previousClauses.add(currentClause);
            previousSlides.add(currentSlide);
            exhaustedClauses.add(currentClause);
        }

        for (int i = 0; i < GoodnessDataInterface.getNumberOfClauses(); i++) {
            if (!exhaustedClauses.contains(i)) {
                clausesRemaining.add(i);
            }
        }

        if(clausesRemaining.size() == 0) {
            return false; // movement failed
        }

        int randomNum = (int)(Math.random() * clausesRemaining.size());
        currentClause = clausesRemaining.get(randomNum);
        currentSlide = 0;

        return true;
    }



    private static boolean isFirstCharVowel(String word) {
        return "AEIOUaeiou".indexOf(word.charAt(0)) != -1;
    }

    // Data Retrieval Queries
    private static int getYes() { return GoodnessDataInterface.getYesSlideNumber(currentClause, currentSlide); }
    private static int getNo() { return GoodnessDataInterface.getNoSlideNumber(currentClause, currentSlide); }
    private static String getNoun() { return GoodnessDataInterface.getSlideNoun(currentClause, currentSlide); }
    private static String getAdjective() { return GoodnessDataInterface.getSlideAdjective(currentClause, currentSlide); }
    private static String slideText() { return GoodnessDataInterface.getSlideText(currentClause, currentSlide); }

    /****************************************************************************************
     ** MARK:: Backwards Methods
     ****************************************************************************************/

    public static boolean canReverse() {
        if (previousSlides == null) {
            return false;
        }
        return previousSlides.size() > 0;
    }

    public static void proceedWithBack() {
        if (canReverse()) {
            int oldCurrentClause = currentClause;



            currentClause = previousClauses.get(previousClauses.size() - 1);
            currentSlide = previousSlides.get(previousSlides.size() - 1);
            previousClauses.remove(previousClauses.size() - 1);
            previousSlides.remove(previousSlides.size() - 1);

            if(currentClause != oldCurrentClause) {
                if (exhaustedClauses.contains(currentClause)) {
                    exhaustedClauses.remove(exhaustedClauses.indexOf(currentClause));
                }
            }

            removeLastDescriptors();
        }
    }

    public static void removeLastDescriptors() {
        if(!getAdjective().isEmpty()) {
            if(descriptorAdjectives.contains(getAdjective())) {
                descriptorAdjectives.remove(descriptorAdjectives.indexOf(getAdjective()));
            }
        }
        if(!getNoun().isEmpty()) {
            if(descriptorNouns.contains(getNoun())) {
                descriptorNouns.remove(descriptorNouns.indexOf(getNoun()));
            }
        }
    }
}
