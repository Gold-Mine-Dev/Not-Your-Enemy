package com.ldi19.notyourenemy;


import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ldi19.notyourenemy.state.IdeologyState;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_03_FunQuestion extends Fragment {

    private FrameLayout courtroomFragmentLayout;       //Linear Layout for introFragmentLayout animation
    private TextView courtText;


    public Fragment_03_FunQuestion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        courtroomFragmentLayout =  (FrameLayout) inflater.inflate(R.layout.fragment_03_fun_question, container, false);
        courtroomFragmentLayout.setBackgroundColor(IdeologyState.youColor(getContext()));
        courtText = (TextView) courtroomFragmentLayout.findViewById(R.id.court_text);

        //Set Case
        String funQuestion = "";
        try {
            ArrayList<String> funQuestionList = jsonStringToArray(getString(R.string.frag_3_text_array));
            funQuestion = funQuestionList.get(new Random().nextInt(funQuestionList.size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        courtText.setText(Html.fromHtml(funQuestion));

        //Set Font Size
        resizeText(courtText);
        courtText.setTypeface(courtText.getTypeface(), Typeface.BOLD);

        //Set Next Button
        ImageButton nextButton = (ImageButton) courtroomFragmentLayout.findViewById(R.id.forward_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });

        return courtroomFragmentLayout;
    }

    private void nextButton() {
        Fragment newFragment = new Fragment_04_Apologia_Transition();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }

    private void resizeText(TextView theTextView) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float fontSize = 0;
        float totalTextHeight = 0;
        do {
            fontSize++;
            totalTextHeight = (fontSize * theTextView.length() / (displayMetrics.widthPixels / displayMetrics.scaledDensity)) * fontSize;
        } while (totalTextHeight < (displayMetrics.heightPixels / displayMetrics.scaledDensity) * 0.9);
        theTextView.setTextSize(fontSize);
    }
}
