package com.ldi19.notyourenemy.state;

import android.content.Context;

import com.ldi19.notyourenemy.app.Ideology;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ApologiaDataInterface {

    private static final String islamErrorFile = "errors_islam.json";
    private static final String atheistErrorFile = "errors_secular.json";
    private static final String hinduErrorFile = "errors_hindu.json";
    private static final String christianErrorFile = "errors_christian.json";



    //Specific Slide Values Retrieval

    public static int getNextSlideNumber(Context theContext, boolean isYes, Ideology theIdeology, String argumentName, int slideNumber) {
        if (isYes) {
            return findSlideIntValue(theContext, theIdeology, argumentName, slideNumber, "yes");
        } else {
            return findSlideIntValue(theContext, theIdeology, argumentName, slideNumber, "no");
        }
    }

    public static int getNumberOfSlidesForArgument(Context theContext, Ideology theIdeology, String argumentName) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(theContext, getErrorFileName(theIdeology)));
            JSONArray arguments_jArry = obj.getJSONArray("arguments");

            for (int i = 0; i < arguments_jArry.length(); i++) {
                JSONObject jo_argument = arguments_jArry.getJSONObject(i);
                String the_value = jo_argument.getString("name").trim();
                if (the_value.equalsIgnoreCase(argumentName.trim())) {

                    JSONArray slides_jArry = jo_argument.getJSONArray("slides");
                    return slides_jArry.length();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static boolean yesNextPresent(Context theContext, Ideology theIdeology, String argumentName, int slideNumber) {
        return findSlideIntValue(theContext, theIdeology, argumentName, slideNumber, "yes") != -1;
    }

    public static boolean noNextPresent(Context theContext, Ideology theIdeology, String argumentName, int slideNumber) {
        return findSlideIntValue(theContext, theIdeology, argumentName, slideNumber, "no") != -1;
    }

    public static boolean anyNextPresent(Context theContext, Ideology theIdeology, String argumentName, int slideNumber) {
        return yesNextPresent(theContext, theIdeology, argumentName, slideNumber) || noNextPresent(theContext, theIdeology, argumentName, slideNumber);
    }

    public static String getSlideContent(Context theContext, Ideology theIdeology, String argumentName, int slideNumber) {
        return findSlideStringValue(theContext, theIdeology, argumentName, slideNumber, "content");
    }



    //Comparative Value Retrieval

    public static String getProblem(Context theContext, Ideology ideology) {
        return findComparativeValue(theContext, "problem", ideology);
    }

    public static String getLaw(Context theContext, Ideology ideology) {
        return findComparativeValue(theContext, "law", ideology);
    }

    public static String getPunishment(Context theContext, Ideology ideology) {
        return findComparativeValue(theContext, "punishment", ideology);
    }

    public static String getHope(Context theContext, Ideology ideology) {
        return findComparativeValue(theContext, "hope", ideology);
    }



    //List Retrieval

    public static ArrayList<String> listOfIslamicArguments(Context theContext) { return listOfArgumentNames(theContext, islamErrorFile); }
    public static ArrayList<String> listOfAtheistArguments(Context theContext) { return listOfArgumentNames(theContext, atheistErrorFile); }
    public static ArrayList<String> listOfHinduArguments(Context theContext) { return listOfArgumentNames(theContext, hinduErrorFile); }
    public static ArrayList<String> listOfChristianArguments(Context theContext) { return listOfArgumentNames(theContext, christianErrorFile); }


    /***********************************************************************************************
     *  DATA OVERVIEW METHODS
     *  Probe JSON
     **********************************************************************************************/

    private static ArrayList<String> listOfArgumentNames(Context theContext, String fileName) {
        ArrayList<String> defendants = new ArrayList<>();

        //Get arguments names from JSON file
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(theContext, fileName));
            JSONArray m_jArry = obj.getJSONArray("arguments");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String the_value = jo_inside.getString("name");
                defendants.add(the_value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return defendants;
    }


    /***********************************************************************************************
     *  DATA RETRIEVAL METHODS
     *  Load Specific Values from JSON Objects / Arrays
     **********************************************************************************************/

    private static String findSlideStringValue(Context theContext, Ideology theIdeology, String argumentName, int slideNumber, String theSlideKey) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(theContext, getErrorFileName(theIdeology)));
            JSONArray arguments_jArry = obj.getJSONArray("arguments");

            for (int i = 0; i < arguments_jArry.length(); i++) {
                JSONObject jo_argument = arguments_jArry.getJSONObject(i);
                String the_value = jo_argument.getString("name").trim();
                if (the_value.equalsIgnoreCase(argumentName.trim())) {

                    JSONArray slides_jArry = jo_argument.getJSONArray("slides");

                    if (slides_jArry.getJSONObject(slideNumber).has(theSlideKey)) {
                        return slides_jArry.getJSONObject(slideNumber).getString(theSlideKey);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static int findSlideIntValue(Context theContext, Ideology theIdeology, String argumentName, int slideNumber, String theSlideKey) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(theContext, getErrorFileName(theIdeology)));
            JSONArray arguments_jArry = obj.getJSONArray("arguments");

            for (int i = 0; i < arguments_jArry.length(); i++) {
                JSONObject jo_argument = arguments_jArry.getJSONObject(i);
                String the_value = jo_argument.getString("name");
                if (the_value.equalsIgnoreCase(argumentName)) {

                    JSONArray slides_jArry = jo_argument.getJSONArray("slides");

                    if (slides_jArry.getJSONObject(slideNumber).has(theSlideKey)) {
                        return slides_jArry.getJSONObject(slideNumber).getInt(theSlideKey);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static String findComparativeValue(Context theContext, String keyNameToMatch, Ideology theIdeology) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(theContext, getErrorFileName(theIdeology)));
            return obj.getString(keyNameToMatch);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }



    /***********************************************************************************************
     *  DATA ACCESS METHODS
     *  Load JSON from assets - https://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
     **********************************************************************************************/
    private static String getErrorFileName (Ideology theIdeology) {
        if (theIdeology == Ideology.ISLAM) {
            return islamErrorFile;
        } else if (theIdeology == Ideology.SECULAR) {
            return atheistErrorFile;
        } else if (theIdeology == Ideology.HINDU) {
            return hinduErrorFile;
        } else { // Ideology.CHRISTIAN
            return christianErrorFile;
        }
    }

    private static String loadJSONFromAsset(Context theContext, String fileName) {
        String json;
        try {
            InputStream is = theContext.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
