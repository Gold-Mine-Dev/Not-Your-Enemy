package com.ldi19.notyourenemy.state;

import android.content.Context;

import com.ldi19.notyourenemy.app.Ideology;

/**
 * APPLICATION STATE
 */

public class IdeologyState {
    private static Ideology youIdeology;
    private static Ideology meIdeology;


    /***********************************************************************************************
     *  SET APP STATE
     **********************************************************************************************/

    public static void setYouIdeology (Ideology yourIdeology) { youIdeology = yourIdeology; }
    public static void setMeIdeology (Ideology myIdeology) { meIdeology = myIdeology; }


    /***********************************************************************************************
     *  GET APP STATE
     **********************************************************************************************/

    public static Ideology getYouIdeology () { return youIdeology; }
    public static Ideology getMeIdeology () { return meIdeology; }


    /***********************************************************************************************
     *  APP STATE COMPUTED PROPERTIES
     **********************************************************************************************/

    public static int youColor(Context theContext) {
        return youIdeology.getColor(theContext);
    }
    public static int meColor(Context theContext) {
        return meIdeology.getColor(theContext);
    }
    public static int youColorLight(Context theContext) { return youIdeology.getColorLight(theContext); }
    public static int meColorLight(Context theContext) { return meIdeology.getColorLight(theContext); }
    public static boolean areIdeologiesSet() { return (youIdeology != null) && (meIdeology != null); }


    /***********************************************************************************************
     *  CLEAR STATE
     **********************************************************************************************/

    public static void clearIdeologyState() {
        youIdeology = null;
        meIdeology = null;
    }
}