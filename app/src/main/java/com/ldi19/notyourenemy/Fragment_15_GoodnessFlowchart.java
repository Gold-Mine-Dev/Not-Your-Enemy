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

import com.ldi19.notyourenemy.state.IdeologyState;
import com.ldi19.notyourenemy.state.GoodnessState;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_15_GoodnessFlowchart extends Fragment {

    private FrameLayout flowchartFragmentLayout;       //Linear Layout for introFragmentLayout animation
    private TextView flowchartText;


    public Fragment_15_GoodnessFlowchart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        flowchartFragmentLayout =  (FrameLayout) inflater.inflate(R.layout.fragment_15_goodness_flowchart, container, false);
        flowchartFragmentLayout.setBackgroundColor(IdeologyState.youColor(getContext()));
        flowchartText = (TextView) flowchartFragmentLayout.findViewById(R.id.court_text);

        //Set Case
        if (GoodnessState.canReverse()) {
            GoodnessState.proceedWithBack();
        } else {
            GoodnessState.initalizeGoodnessState(flowchartFragmentLayout.getContext());
        }

        String presentationSlide = GoodnessState.getSlideText();
        flowchartText.setText(Html.fromHtml(presentationSlide));

        //Set Font Size
        resizeText();

        //Set Yes Button
        ImageButton yesButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesButtonPress();
            }
        });

        //Set No Button
        ImageButton noButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noButtonPress();
            }
        });

        //Set 1+ Button
        ImageButton yesNumericButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.one_button);
        yesNumericButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesButtonPress();
            }
        });

        //Set 0 Button
        ImageButton noNumericButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.zero_button);
        noNumericButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noButtonPress();
            }
        });

        //Set Next Button
        ImageButton nextButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.forward_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonPress();
            }
        });


        redrawMovementBar();

        return flowchartFragmentLayout;
    }



    private void redrawMovementBar() {
        LinearLayout normalSlideLayout = (LinearLayout) flowchartFragmentLayout.findViewById(R.id.panel_right_side_normal);
        LinearLayout numericSlideLayout = (LinearLayout )  flowchartFragmentLayout.findViewById(R.id.panel_right_side_numeric);
        LinearLayout finalSlideLayout = (LinearLayout) flowchartFragmentLayout.findViewById(R.id.panel_right_side_final);
        LinearLayout progressView = (LinearLayout) flowchartFragmentLayout.findViewById(R.id.progress_view);
        LinearLayout.LayoutParams progressViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageButton yesButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.yes_button);
        ImageButton noButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.no_button);
        ImageButton oneButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.one_button);
        ImageButton zeroButton = (ImageButton) flowchartFragmentLayout.findViewById(R.id.zero_button);

        progressViewParams.weight = GoodnessState.currentPercentOfArgumentProgress();
        progressView.setLayoutParams(progressViewParams);

        if(GoodnessState.anyButtonDisplay()) {
            if(!GoodnessState.numericButtonDisplay() && !GoodnessState.forceArrowButtonDisplay()) {
                //display normal panel
                normalSlideLayout.setVisibility(LinearLayout.VISIBLE);
                numericSlideLayout.setVisibility(LinearLayout.GONE);
                finalSlideLayout.setVisibility(LinearLayout.GONE);

                if(GoodnessState.yesButtonDisplay() || GoodnessState.yesForcesNextClause()) {
                    yesButton.setEnabled(true); yesButton.setAlpha(255);
                } else {
                    yesButton.setEnabled(false); yesButton.setAlpha(51);
                }

                if(GoodnessState.noButtonDisplay() || GoodnessState.noForcesNextClause()) {
                    noButton.setEnabled(true); noButton.setAlpha(255);
                } else {
                    noButton.setEnabled(false); noButton.setAlpha(51);
                }
            } else if (GoodnessState.forceArrowButtonDisplay()) {
                normalSlideLayout.setVisibility(LinearLayout.GONE);
                numericSlideLayout.setVisibility(LinearLayout.GONE);
                finalSlideLayout.setVisibility(LinearLayout.VISIBLE);
            } else {
                //display numeric panel
                numericSlideLayout.setVisibility(LinearLayout.VISIBLE);
                normalSlideLayout.setVisibility(LinearLayout.GONE);
                finalSlideLayout.setVisibility(LinearLayout.GONE);

                if(GoodnessState.yesButtonDisplay() || GoodnessState.yesForcesNextClause()) {
                    oneButton.setEnabled(true); oneButton.setAlpha(255);
                } else {
                    oneButton.setEnabled(false); oneButton.setAlpha(51);
                }

                if(GoodnessState.noButtonDisplay() || GoodnessState.noForcesNextClause()) {
                    zeroButton.setEnabled(true); zeroButton.setAlpha(255);
                } else {
                    zeroButton.setEnabled(false); zeroButton.setAlpha(51);
                }
            }
        } else {
            normalSlideLayout.setVisibility(LinearLayout.GONE);
            numericSlideLayout.setVisibility(LinearLayout.GONE);
            finalSlideLayout.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void nextButtonPress() {
        if(GoodnessState.forceArrowButtonDisplay()) {
            yesButtonPress();
        } else {
            boolean flowchartFinished = true;
            if(GoodnessState.proceedWithForward()) {
                if (!GoodnessState.threeDescriptorsReached()) {
                    flowchartText = (TextView) flowchartFragmentLayout.findViewById(R.id.court_text);
                    flowchartText.setText(Html.fromHtml(GoodnessState.getSlideText()));
                    resizeText();
                    redrawMovementBar();
                    flowchartFinished = false;
                }
            }

            if (flowchartFinished) {
                moveToNextFragment();
            }
        }
    }

    private void yesButtonPress() {
        if (GoodnessState.proceedWithYes()) {
            flowchartText = (TextView) flowchartFragmentLayout.findViewById(R.id.court_text);
            flowchartText.setText(Html.fromHtml(GoodnessState.getSlideText()));
            resizeText();
            redrawMovementBar();
        } else {
            moveToNextFragment();
        }

    }

    private void noButtonPress() {
        if (GoodnessState.proceedWithNo()) {
            flowchartText = (TextView) flowchartFragmentLayout.findViewById(R.id.court_text);
            flowchartText.setText(Html.fromHtml(GoodnessState.getSlideText()));
            resizeText();
            redrawMovementBar();
        } else {
            moveToNextFragment();
        }
    }


    /****************************************************************************************
     ** MARK:: Helper Methods
     ****************************************************************************************/
    //Proceed to next fragment (upon clause exhaustion or 3 params met)
    private void moveToNextFragment() {
        Fragment newFragment = new Fragment_16_GoodnessQ2();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void resizeText() {
        //DisplayMetrics displayMetrics = new DisplayMetrics();
        //getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = (int) (displayMetrics.scaledDensity * 20);
        //flowchartText.setTextSize(height);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float fontSize = 0;
        float totalTextHeight = 0;
        do {
            fontSize++;
            totalTextHeight = (fontSize * flowchartText.length() / (displayMetrics.widthPixels / displayMetrics.scaledDensity)) * fontSize;
        } while (totalTextHeight < (displayMetrics.heightPixels / displayMetrics.scaledDensity) * 0.9);
        flowchartText.setTextSize((float) (fontSize * 0.9));
    }


    /****************************************************************************************
     ** MARK:: Lifecycle Methods
     ****************************************************************************************/
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
                    if(GoodnessState.canReverse()) {
                        GoodnessState.proceedWithBack();
                        flowchartText.setText(Html.fromHtml(GoodnessState.getSlideText()));
                        redrawMovementBar();
                        return true;
                    }
                }
                return false;
            }
        });

    }
}
