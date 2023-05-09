package com.ldi19.notyourenemy;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ldi19.notyourenemy.state.ApologiaState;
import com.ldi19.notyourenemy.state.IdeologyState;
import com.ldi19.notyourenemy.state.GoodnessState;
import com.ldi19.notyourenemy.app.RichTransitionFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_21_Ending extends RichTransitionFragment {

    private FrameLayout creditsFragmentLayout;       //Linear Layout for introFragmentLayout animation
    AlertDialog.Builder builder;

    public Fragment_21_Ending() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        creditsFragmentLayout = (FrameLayout) inflater.inflate(R.layout.fragment_21_ending, container, false);
        loadBackground((ImageView) creditsFragmentLayout.findViewById(R.id.intro_background));


        //Set information button listener
        ImageButton informationButton = (ImageButton) creditsFragmentLayout.findViewById(R.id.info_button);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( creditsFragmentLayout.getContext());

                // set title
                alertDialogBuilder.setTitle("Data To Be Sent");

                // set dialog message
                alertDialogBuilder
                        .setMessage(Html.fromHtml("Not Your Enemy (NYE) only sends the interviewers belief, the interviewees belief and the interviewees personal justice description phrase \""+"<i>"+"ex. according to you, you are a..."+"</i>"+"\". No personally identifiable info is sent"))
                        .setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAlertButton));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAlertButton));
            }
        });

        //Set Progress Bar Color
        ProgressBar progressBar = creditsFragmentLayout.findViewById(R.id.send_activity_indicator);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorChristian), android.graphics.PorterDuff.Mode.MULTIPLY);



        //Set next button listener
        ImageButton restartButton = (ImageButton) creditsFragmentLayout.findViewById(R.id.end_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdeologyState.clearIdeologyState();
                GoodnessState.clearGoodnessState();
                ApologiaState.clearApologiaState();
                ((MainActivity) getActivity()).newSession();
            }
        });

        // Disable/Enable Submissions based on strings_config.xml setting
        String enabled_string = getResources().getString(R.string.api_enabled);
        Button saveButton = (Button) creditsFragmentLayout.findViewById(R.id.save_button);
        Button sendButton = (Button) creditsFragmentLayout.findViewById(R.id.right_button);
        if (!enabled_string.equalsIgnoreCase("true")) {
            //Display unavailability warning
            TextView warnLeft = (TextView) creditsFragmentLayout.findViewById(R.id.warn_left);
            TextView warnRight = (TextView) creditsFragmentLayout.findViewById(R.id.warn_right);
            warnLeft.setVisibility(View.VISIBLE);
            warnRight.setVisibility(View.VISIBLE);

            saveButton.setEnabled(false);
            sendButton.setEnabled(false);
            saveButton.setAlpha(0.25f);
            sendButton.setAlpha(0.25f);
        } else {
            //Button listeners - Enable save and send actions
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPost();
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePost();
                }
            });
        }


        return creditsFragmentLayout;
    }

    public void savePost() {
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("description", GoodnessState.getDescriptionStatementShort());
        postData.put("interviewee", IdeologyState.getYouIdeology().getShortString());
        postData.put("interviewer", IdeologyState.getMeIdeology().getShortString());
        String storageString = "";

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        storageString  = sharedPref.getString(getString(R.string.storage_string_key), "");

        try {
            storageString = "".equals(storageString) ? getPostDataString(postData) : getPostDataString(postData) + " -- " + storageString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("FAIL","unsupencex");
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.storage_string_key), storageString);
        editor.apply();

        displayCompleteSaving();
    }

    public void sendPost() {
        displayStartSending();

        Thread notMainUIThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<Boolean> future = executor.submit(new Task(getActivity()));

                try {
                    if(future.get(10, TimeUnit.SECONDS)) {
                        displayCompleteSending();
                    } else {
                        displayFailedSending();
                    }
                } catch (TimeoutException|InterruptedException|ExecutionException e) {
                    future.cancel(true);
                    displayFailedSending();
                }

                executor.shutdownNow();
            }
        });

        notMainUIThread.start();
    }


    private void displayStartSending() {
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                TextView statusText = creditsFragmentLayout.findViewById(R.id.count_text);
                statusText.setText(R.string.frag_21_text);

                ProgressBar progressBar = creditsFragmentLayout.findViewById(R.id.send_activity_indicator);
                progressBar.setVisibility(View.VISIBLE);

                Button saveButton = (Button) creditsFragmentLayout.findViewById(R.id.save_button);
                saveButton.setEnabled(false);
                saveButton.setAlpha(.5f);
                saveButton.setClickable(false);

                Button sendButton = (Button) creditsFragmentLayout.findViewById(R.id.right_button);
                sendButton.setEnabled(false);
                sendButton.setAlpha(.5f);
                sendButton.setClickable(false);

                ImageButton restartButton = (ImageButton) creditsFragmentLayout.findViewById(R.id.end_button);
                restartButton.setEnabled(false);
                restartButton.setAlpha(.5f);
                restartButton.setClickable(false);
            }
        });
    }

    private void displayFailedSending() {
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                TextView statusText = creditsFragmentLayout.findViewById(R.id.count_text);
                statusText.setText(R.string.frag_21_text_failed);

                ProgressBar progressBar = creditsFragmentLayout.findViewById(R.id.send_activity_indicator);
                progressBar.setVisibility(View.GONE);

                Button saveButton = (Button) creditsFragmentLayout.findViewById(R.id.save_button);
                saveButton.setEnabled(true);
                saveButton.setAlpha(1f);
                saveButton.setClickable(true);

                Button sendButton = (Button) creditsFragmentLayout.findViewById(R.id.right_button);
                sendButton.setEnabled(true);
                sendButton.setAlpha(1f);
                sendButton.setClickable(true);

                ImageButton restartButton = (ImageButton) creditsFragmentLayout.findViewById(R.id.end_button);
                restartButton.setEnabled(true);
                restartButton.setAlpha(1f);
                restartButton.setClickable(true);
            }
        });
    }

    private void displayCompleteSending() {
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                TextView statusText = creditsFragmentLayout.findViewById(R.id.count_text);
                statusText.setText(R.string.frag_21_text_complete);

                ProgressBar progressBar = creditsFragmentLayout.findViewById(R.id.send_activity_indicator);
                progressBar.setVisibility(View.GONE);

                Button saveButton = (Button) creditsFragmentLayout.findViewById(R.id.save_button);
                saveButton.setEnabled(false);
                saveButton.setAlpha(.5f);
                saveButton.setClickable(false);

                Button sendButton = (Button) creditsFragmentLayout.findViewById(R.id.right_button);
                sendButton.setEnabled(false);
                sendButton.setAlpha(.5f);
                sendButton.setClickable(false);

                ImageButton restartButton = (ImageButton) creditsFragmentLayout.findViewById(R.id.end_button);
                restartButton.setEnabled(true);
                restartButton.setAlpha(1f);
                restartButton.setClickable(true);
            }
        });
    }

    private void displayCompleteSaving() {
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                TextView statusText = creditsFragmentLayout.findViewById(R.id.count_text);
                statusText.setText(R.string.frag_21_text_complete);

                ProgressBar progressBar = creditsFragmentLayout.findViewById(R.id.send_activity_indicator);
                progressBar.setVisibility(View.GONE);

                Button saveButton = (Button) creditsFragmentLayout.findViewById(R.id.save_button);
                saveButton.setEnabled(false);
                saveButton.setAlpha(.5f);
                saveButton.setClickable(false);

                Button sendButton = (Button) creditsFragmentLayout.findViewById(R.id.right_button);
                sendButton.setEnabled(false);
                sendButton.setAlpha(.5f);
                sendButton.setClickable(false);

                ImageButton restartButton = (ImageButton) creditsFragmentLayout.findViewById(R.id.end_button);
                restartButton.setEnabled(true);
                restartButton.setAlpha(1f);
                restartButton.setClickable(true);
            }
        });
    }

    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}


class Task implements Callable<Boolean> {
    private final Activity callingActivity;

    public Task (Activity theCallingActivity) {
        this.callingActivity = theCallingActivity;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            HashMap<String, String> postData = new HashMap<String, String>();
            postData.put("description", GoodnessState.getDescriptionStatementShort());
            postData.put("interviewee", IdeologyState.getYouIdeology().getShortString());
            postData.put("interviewer", IdeologyState.getMeIdeology().getShortString());

            SharedPreferences sharedPref = callingActivity.getPreferences(Context.MODE_PRIVATE);
            String storageString = sharedPref.getString(callingActivity.getString(R.string.storage_string_key), "");
            if (!"".equals(storageString)) {
                postData.put("stored", storageString);
            }

            try {
                URL url;
                String url_string = callingActivity.getString(R.string.api_url);
                String response = "";
                url = new URL(url_string);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(Fragment_21_Ending.getPostDataString(postData));

                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";
                }

                //Remove stored interview string
                if (!"".equals(storageString)) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(callingActivity.getString(R.string.storage_string_key), "");
                    editor.apply();
                }

                Log.e("SUCCESS", response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e("FAIL","unsupencex");
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("FAIL","ioexcep");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FAIL","Excep");
            return false;
        }
        return true;
    }


}
