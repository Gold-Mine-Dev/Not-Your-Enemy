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
public class Fragment_11_Problem extends Fragment {

    private FrameLayout judgmentFragmentLayout;       //Linear Layout for introFragmentLayout animation

    public Fragment_11_Problem() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        judgmentFragmentLayout =  (FrameLayout) inflater.inflate(R.layout.fragment_11_problem, container, false);
        LinearLayout leftPanel = (LinearLayout) judgmentFragmentLayout.findViewById(R.id.judgment_background_left);
        LinearLayout rightPanel = (LinearLayout) judgmentFragmentLayout.findViewById(R.id.judgment_background_right);
        TextView titleText = (TextView) judgmentFragmentLayout.findViewById(R.id.judgment_title_left);
        TextView lawTextYou = (TextView) judgmentFragmentLayout.findViewById(R.id.judgment_text_you);
        TextView lawTextMe = (TextView) judgmentFragmentLayout.findViewById(R.id.judgment_text_me);
        TextView lawSubtitle = (TextView) judgmentFragmentLayout.findViewById(R.id.judgmentSubtitle);

        //Set panel colours
        leftPanel.setBackgroundColor(IdeologyState.youColor(getContext()));
        rightPanel.setBackgroundColor(IdeologyState.meColor(getContext()));

        //Set title typeface
        Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "fonts/nimbus_sans_l_bold.otf");
        titleText.setTypeface(face);
        lawSubtitle.setTypeface(face);
        resizeText();

        //Set judgment text
        lawTextYou.setText(Html.fromHtml(ApologiaDataInterface.getProblem(getContext(), IdeologyState.getYouIdeology())));
        lawTextMe.setText(Html.fromHtml(ApologiaDataInterface.getProblem(getContext(), IdeologyState.getMeIdeology())));

        //Set next button listener
        ImageButton nextButton = (ImageButton) judgmentFragmentLayout.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });

        return judgmentFragmentLayout;
    }

    private void nextButton() {
        Fragment newFragment = new Fragment_12_Law();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void resizeText() {
        TextView lawTextYou = (TextView) judgmentFragmentLayout.findViewById(R.id.judgment_text_you);
        TextView lawTextMe = (TextView) judgmentFragmentLayout.findViewById(R.id.judgment_text_me);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) ((displayMetrics.heightPixels / displayMetrics.scaledDensity) / 10);
        lawTextYou.setTextSize(height);
        lawTextMe.setTextSize(height);
    }

}
