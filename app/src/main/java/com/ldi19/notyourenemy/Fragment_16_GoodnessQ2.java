package com.ldi19.notyourenemy;


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
import com.ldi19.notyourenemy.state.GoodnessState;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_16_GoodnessQ2 extends Fragment {

    private FrameLayout courtroomFragmentLayout;       //Linear Layout for introFragmentLayout animation
    private TextView courtText;


    public Fragment_16_GoodnessQ2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        courtroomFragmentLayout =  (FrameLayout) inflater.inflate(R.layout.fragment_16_goodness_q2, container, false);
        courtroomFragmentLayout.setBackgroundColor(IdeologyState.youColor(getContext()));
        courtText = (TextView) courtroomFragmentLayout.findViewById(R.id.court_text);

        //Set Case
        courtText.setText(Html.fromHtml(GoodnessState.getDescriptionStatement()));

        //Set Font Size
        resizeText(courtText);

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
        Fragment newFragment = new Fragment_17_GoodnessQ3();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
