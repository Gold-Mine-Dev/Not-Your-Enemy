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
public class Fragment_18_Punishment extends Fragment {

    private FrameLayout punishmentFragmentLayout;       //Linear Layout for introFragmentLayout animation
    private TextView punishmentSubtitle;

    public Fragment_18_Punishment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        punishmentFragmentLayout =  (FrameLayout) inflater.inflate(R.layout.fragment_18_punishment, container, false);
        LinearLayout leftPanel = (LinearLayout) punishmentFragmentLayout.findViewById(R.id.punishment_background_left);
        LinearLayout rightPanel = (LinearLayout) punishmentFragmentLayout.findViewById(R.id.punishment_background_right);
        TextView titleText = (TextView) punishmentFragmentLayout.findViewById(R.id.punishment_title_left);
        TextView punishmentTextYou = (TextView) punishmentFragmentLayout.findViewById(R.id.punishment_text_you);
        TextView punishmentTextMe = (TextView) punishmentFragmentLayout.findViewById(R.id.punishment_text_me);
        TextView punishmentSubtitle = (TextView) punishmentFragmentLayout.findViewById(R.id.punishmentSubtitle);

        //Set panel colours
        leftPanel.setBackgroundColor(IdeologyState.youColor(getContext()));
        rightPanel.setBackgroundColor(IdeologyState.meColor(getContext()));

        //Set title typeface
        Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "fonts/nimbus_sans_l_bold.otf");
        titleText.setTypeface(face);
        punishmentSubtitle.setTypeface(face);

        //Set judgment text
        punishmentTextYou.setText(Html.fromHtml(ApologiaDataInterface.getPunishment(getContext(), IdeologyState.getYouIdeology())));
        punishmentTextMe.setText(Html.fromHtml(ApologiaDataInterface.getPunishment(getContext(), IdeologyState.getMeIdeology())));
        resizeText();

        //Set next button listener
        ImageButton nextButton = (ImageButton) punishmentFragmentLayout.findViewById(R.id.close_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });

        return punishmentFragmentLayout;
    }

    private void nextButton() {
        Fragment newFragment = new Fragment_19_GoodnessQ4();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void resizeText() {
        TextView lawTextYou = (TextView) punishmentFragmentLayout.findViewById(R.id.punishment_text_you);
        TextView lawTextMe = (TextView) punishmentFragmentLayout.findViewById(R.id.punishment_text_me);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) ((displayMetrics.heightPixels / displayMetrics.scaledDensity) / 25);
        lawTextYou.setTextSize(height);
        lawTextMe.setTextSize(height);
    }

}
