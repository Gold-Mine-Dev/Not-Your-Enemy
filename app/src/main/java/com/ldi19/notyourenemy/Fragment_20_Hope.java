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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldi19.notyourenemy.state.IdeologyState;
import com.ldi19.notyourenemy.state.ApologiaDataInterface;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_20_Hope extends Fragment {

    private FrameLayout plausibilityFragmentLayout;       //Linear Layout for introFragmentLayout animation

    public Fragment_20_Hope() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        plausibilityFragmentLayout =  (FrameLayout) inflater.inflate(R.layout.fragment_20_hope, container, false);
        LinearLayout leftPanel = (LinearLayout) plausibilityFragmentLayout.findViewById(R.id.plausibility_background_left);
        LinearLayout rightPanel = (LinearLayout) plausibilityFragmentLayout.findViewById(R.id.plausibility_background_right);
        TextView titleText = (TextView) plausibilityFragmentLayout.findViewById(R.id.plausibility_title_left);
        TextView plausibilityTextYou = (TextView) plausibilityFragmentLayout.findViewById(R.id.plausibility_text_you);
        TextView plausibilityTextMe = (TextView) plausibilityFragmentLayout.findViewById(R.id.plausibility_text_me);
        TextView plausibilitySubtitle = (TextView) plausibilityFragmentLayout.findViewById(R.id.plausibilitySubtitle);

        //Set panel colours
        leftPanel.setBackgroundColor(IdeologyState.youColor(getContext()));
        rightPanel.setBackgroundColor(IdeologyState.meColor(getContext()));

        //Set title typeface
        Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "fonts/nimbus_sans_l_bold.otf");
        titleText.setTypeface(face);
        plausibilitySubtitle.setTypeface(face);

        //Set plausibility text
        plausibilityTextYou.setText(Html.fromHtml(ApologiaDataInterface.getHope(getContext(), IdeologyState.getYouIdeology())));
        plausibilityTextMe.setText(Html.fromHtml(ApologiaDataInterface.getHope(getContext(), IdeologyState.getMeIdeology())));
        resizeText();

        //Set next button listener
        ImageButton nextButton = (ImageButton) plausibilityFragmentLayout.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });

        return plausibilityFragmentLayout;
    }

    private void nextButton() {
        Fragment newFragment = new Fragment_21_Ending();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void resizeText() {
        TextView lawTextYou = (TextView) plausibilityFragmentLayout.findViewById(R.id.plausibility_text_you);
        TextView lawTextMe = (TextView) plausibilityFragmentLayout.findViewById(R.id.plausibility_text_me);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) ((displayMetrics.heightPixels / displayMetrics.scaledDensity) / 25);
        lawTextYou.setTextSize(height);
        lawTextMe.setTextSize(height);
    }

}
