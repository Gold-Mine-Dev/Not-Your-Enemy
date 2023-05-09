package com.ldi19.notyourenemy.state;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class GoodnessDataInterface {

    private static final String goodnessExamFile = "exam_goodness.json";
    private static Context theContext = null;


    public static void initializeInterface (Context operationContext) {
        theContext = operationContext;
    }

    //Specific Slide Values Retrieval

    public static int getYesSlideNumber(int clauseNumber, int slideNumber) {
        return findSlideIntValue(clauseNumber, slideNumber, "yes");
    }

    public static int getNoSlideNumber(int clauseNumber, int slideNumber) {
        return findSlideIntValue(clauseNumber, slideNumber, "no");
    }

    public static boolean yesNextPresent(int clauseNumber, int slideNumber) {
        return getYesSlideNumber(clauseNumber, slideNumber) != -1;
    }

    public static boolean noNextPresent(int clauseNumber, int slideNumber) {
        return getNoSlideNumber(clauseNumber, slideNumber) != -1;
    }

    public static boolean anyNextPresent(int clauseNumber, int slideNumber) {
        return yesNextPresent(clauseNumber, slideNumber) || noNextPresent(clauseNumber, slideNumber);
    }

    public static String getSlideText(int clauseNumber, int slideNumber) {
        return findSlideStringValue(clauseNumber, slideNumber, "text");
    }

    public static String getSlideAdjective(int clauseNumber, int slideNumber) {
        return findSlideStringValue(clauseNumber, slideNumber, "descriptorAdjective");
    }

    public static String getSlideNoun(int clauseNumber, int slideNumber) {
        return findSlideStringValue(clauseNumber, slideNumber, "descriptorNoun");
    }

    public static boolean yesEndsClause(int clauseNumber, int slideNumber) {
        return findSlideBooleanValue(clauseNumber, slideNumber, "yesEndsClause");
    }

    public static boolean noEndsClause(int clauseNumber, int slideNumber) {
        return findSlideBooleanValue(clauseNumber, slideNumber, "noEndsClause");
    }

    public static boolean numericButtons(int clauseNumber, int slideNumber) {
        return findSlideBooleanValue(clauseNumber, slideNumber, "numericButtons");
    }

    public static boolean forceArrowButton(int clauseNumber, int slideNumber) {
        return findSlideBooleanValue(clauseNumber, slideNumber, "forceArrowButton");
    }

    public static int getFontFactor(int clauseNumber, int slideNumber) {
        return findSlideIntValue(clauseNumber, slideNumber, "font_factor");
    }

    public static int getNumberOfSlidesForClause(int clauseNumber) {
        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset(theContext));
            return ((JSONArray) obj.get(clauseNumber)).length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getNumberOfClauses() {
        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset(theContext));
            return obj.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    


    /***********************************************************************************************
     *  DATA RETRIEVAL METHODS
     *  Load Specific Values from JSON Objects / Arrays
     **********************************************************************************************/

    private static String findSlideStringValue(int clauseNumber, int slideNumber, String theSlideKey) {
        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset(theContext));
            JSONObject jo_argument = (JSONObject) ((JSONArray) obj.get(clauseNumber)).get(slideNumber);
            return jo_argument.getString(theSlideKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static int findSlideIntValue(int clauseNumber, int slideNumber, String theSlideKey) {
        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset(theContext));
            JSONObject jo_argument = (JSONObject) ((JSONArray) obj.get(clauseNumber)).get(slideNumber);
            return jo_argument.getInt(theSlideKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static boolean findSlideBooleanValue(int clauseNumber, int slideNumber, String theSlideKey) {
        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset(theContext));
            JSONObject jo_argument = (JSONObject) ((JSONArray) obj.get(clauseNumber)).get(slideNumber);
            return jo_argument.getBoolean(theSlideKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }



    /***********************************************************************************************
     *  DATA ACCESS METHODS
     *  Load JSON from assets - https://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
     **********************************************************************************************/

    private static String loadJSONFromAsset(Context theContext) {
        String json;
        try {
            InputStream is = theContext.getAssets().open(goodnessExamFile);
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
