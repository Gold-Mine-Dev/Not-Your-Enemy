package com.ldi19.notyourenemy;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldi19.notyourenemy.state.ApologiaState;
import com.ldi19.notyourenemy.state.IdeologyState;
import com.ldi19.notyourenemy.state.ApologiaDataInterface;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_07_ApologiaFlowchart extends Fragment {

    private FrameLayout flowchartFragmentLayout;       //Linear Layout for introFragmentLayout animation
    private TextView flowchartText;


    public Fragment_07_ApologiaFlowchart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        flowchartFragmentLayout =  (FrameLayout) inflater.inflate(R.layout.fragment_07_apologia_flowchart, container, false);
        flowchartFragmentLayout.setBackgroundColor(IdeologyState.youColor(getContext()));
        flowchartText = (TextView) flowchartFragmentLayout.findViewById(R.id.court_text);

        //Set Case
        if (ApologiaState.areSlidesEmpty() || ApologiaState.getFlowchartBackwardMovement()) {
            // new flowchart opened
            ApologiaState.setFlowchartBackwardMovement(false);
            String initialSlide = ApologiaDataInterface.getSlideContent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), 0);
            flowchartText.setText(Html.fromHtml(initialSlide));
        } else if (!ApologiaState.areSlidesEmpty()) {
            // set slide from last argument
            ApologiaState.setCurrentArgument(ApologiaState.peekLastSlide().getArgument());
            ApologiaState.setCurrentSlideNumber(ApologiaState.popLastSlide().getSlideNumber());
            String initialSlide = ApologiaDataInterface.getSlideContent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
            flowchartText.setText(Html.fromHtml(initialSlide));
        }

        //Set FontSize based on Screen Size
        resizeText();

        //Set Yes Button
        ImageButton yesButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesButton();
            }
        });

        //Set No Button
        ImageButton noButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noButton();
            }
        });

        //Set Next Button
        ImageButton nextButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.forward_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });

        //Set Reset Button
        ImageButton reset_button = (ImageButton) flowchartFragmentLayout.findViewById(R.id.reset_button);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButton();
            }
        });

        redrawMovementBar();

        return flowchartFragmentLayout;
    }

    private void yesButton() {
        if (ApologiaDataInterface.yesNextPresent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber())) {
            ApologiaState.addPreviousSlidePair(ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
            ApologiaState.setCurrentSlideNumber(ApologiaDataInterface.getNextSlideNumber(getContext(), true, IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber()));
            flowchartText = (TextView) flowchartFragmentLayout.findViewById(R.id.court_text);
            String nextYesSlideContent = ApologiaDataInterface.getSlideContent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
            flowchartText.setText(Html.fromHtml(nextYesSlideContent));

            //redraw function bar
            redrawMovementBar();
            resizeText();
        }
    }

    private void noButton() {
        if (ApologiaDataInterface.noNextPresent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber())) {
            ApologiaState.addPreviousSlidePair(ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
            ApologiaState.setCurrentSlideNumber(ApologiaDataInterface.getNextSlideNumber(getContext(), false, IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber()));
            flowchartText = (TextView) flowchartFragmentLayout.findViewById(R.id.court_text);
            String nextNoSlideContent = ApologiaDataInterface.getSlideContent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
            flowchartText.setText(Html.fromHtml(nextNoSlideContent));

            //redraw function bar
            redrawMovementBar();
            resizeText();
        }
    }

    private void redrawMovementBar() {
        LinearLayout normalSlideLayout = (LinearLayout) flowchartFragmentLayout.findViewById(R.id.panel_right_side_normal);
        LinearLayout finalSlideLayout = (LinearLayout) flowchartFragmentLayout.findViewById(R.id.panel_right_side_final);
        LinearLayout progressView = (LinearLayout) flowchartFragmentLayout.findViewById(R.id.progress_view);
        LinearLayout.LayoutParams progressViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageButton yesButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.yes_button);
        ImageButton noButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.no_button);
        ImageButton endButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.flowchart_end_imagebutton);



        if(ApologiaDataInterface.anyNextPresent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber())) {
            //display normal slide
            normalSlideLayout.setVisibility(LinearLayout.VISIBLE);
            finalSlideLayout.setVisibility(LinearLayout.GONE);
            endButton.setVisibility(LinearLayout.GONE);

            progressViewParams.weight = ApologiaState.percentOfSlideProgress(getContext(), ApologiaState.getCurrentArgument());
            progressView.setLayoutParams(progressViewParams);

            if(ApologiaDataInterface.yesNextPresent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber())) {
                //activate yes button
                yesButton.setEnabled(true);
                yesButton.setAlpha(255);
            } else {
                //deactivate yes button
                yesButton.setEnabled(false);
                yesButton.setAlpha(51);
            }

            if(ApologiaDataInterface.noNextPresent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber())) {
                //activate no button
                noButton.setEnabled(true);
                noButton.setAlpha(255);
            } else {
                //deactivate no button
                noButton.setEnabled(false);
                noButton.setAlpha(51);
            }
        } else {
            //display final slide
            progressViewParams.weight = 100;
            progressView.setLayoutParams(progressViewParams);

            normalSlideLayout.setVisibility(LinearLayout.GONE);
            finalSlideLayout.setVisibility(LinearLayout.VISIBLE);
            endButton.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void nextButton() {
        ApologiaState.addPreviousSlidePair(ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
        ApologiaState.clearCurrentSlide();
        ApologiaState.setFlowchartBackwardMovement(false);

        ApologiaState.addPreviousSlidePair(ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
        Fragment newFragment = new Fragment_08_Legal_Transition();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void resetButton() {
        ApologiaState.addPreviousSlidePair(ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
        ApologiaState.clearCurrentSlide();
        ApologiaState.setFlowchartBackwardMovement(true);

        Fragment newFragment = new Fragment_06_ApologiaSelect();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.commit();
    }

    private void resizeText() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float fontSize = 0;
        float totalTextHeight = 0;
        do {
            fontSize++;
            totalTextHeight = (fontSize * flowchartText.length() / (displayMetrics.widthPixels / displayMetrics.scaledDensity)) * fontSize;
        } while (totalTextHeight < (displayMetrics.heightPixels / displayMetrics.scaledDensity) * 0.90);
        flowchartText.setTextSize(fontSize);
    }




    //Intercept back pressed for ModeSelectFragment button reinstatement
    //https://stackoverflow.com/questions/18755550/fragment-pressing-back-button
    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if(ApologiaState.areSlidesEmpty() || ApologiaState.willBackProduceArgumentChange()) {
                        return false;
                    } else {
                        ApologiaState.setCurrentArgument(ApologiaState.peekLastSlide().getArgument());
                        ApologiaState.setCurrentSlideNumber(ApologiaState.popLastSlide().getSlideNumber());
                        String previousSlide = ApologiaDataInterface.getSlideContent(getContext(), IdeologyState.getYouIdeology(), ApologiaState.getCurrentArgument(), ApologiaState.getCurrentSlideNumber());
                        flowchartText.setText(Html.fromHtml(previousSlide));
                        redrawMovementBar();
                        return true;
                    }
                }
                return false;
            }
        });

    }
}
