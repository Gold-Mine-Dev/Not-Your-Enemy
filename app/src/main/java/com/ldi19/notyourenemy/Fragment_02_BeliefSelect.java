package com.ldi19.notyourenemy;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldi19.notyourenemy.app.RichTransitionFragment;
import com.ldi19.notyourenemy.state.IdeologyState;
import com.ldi19.notyourenemy.app.Ideology;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_02_BeliefSelect extends RichTransitionFragment {

    private FrameLayout beliefSelectFragmentLayout;       //Linear Layout for introFragmentLayout animation
    boolean doubleBackToExitPressedOnce = false;


    public Fragment_02_BeliefSelect() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        beliefSelectFragmentLayout = (FrameLayout) inflater.inflate(R.layout.fragment_02_belief_select, container, false);
        loadBackground((ImageView) beliefSelectFragmentLayout.findViewById(R.id.intro_background));
        setFontAttributes();
        setFontSizes();
        initializeQuestionButton();
        setLeftButtonListeners();
        setRightButtonListeners();
        IdeologyState.clearIdeologyState();

        return beliefSelectFragmentLayout;
    }

    //Intercept back pressed for Fragment_06_ApologiaSelect button reinstatement
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if (doubleBackToExitPressedOnce) {
                        getActivity().moveTaskToBack(true);
                        getActivity().finish();
                        return true;
                    }

                    doubleBackToExitPressedOnce = true;

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce=false;
                        }
                    }, 2000);
                    return true;
                }
                return false;
            }
        });
    }

    /***********************************************************************************************
     *  PRIVATE METHODS
     **********************************************************************************************/

    private void initializeQuestionButton() {
        ImageButton questionButton = (ImageButton) beliefSelectFragmentLayout.findViewById(R.id.belief_select_question_button);

        //Repostion
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int titleBarHeight = (int) (displayMetrics.heightPixels * 0.02);

        TextView beliefSelectTitleLeft = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_left);
        beliefSelectTitleLeft.measure(0, 0);
        float left_width = beliefSelectTitleLeft.getMeasuredWidth();

        TextView beliefSelectTitleRight = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_right);
        beliefSelectTitleRight.measure(0, 0);
        float right_width = beliefSelectTitleRight.getMeasuredWidth();

        int margin_right = (int) Math.ceil((left_width - right_width) / 2);
        int padding = (int) Math.floor(titleBarHeight * 1.5);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1 );
        params.setMargins(0, 0, margin_right  + titleBarHeight, 0);
        questionButton.setLayoutParams(params);
        questionButton.setPadding(padding,padding,padding,padding);

        //Listener
        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment_02_Belief_Select.newInstance().show(getFragmentManager(), null);
            }
        });
    }

    private void setLeftButtonListeners(){
        TextView buttonYouIslam = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_islam);
        TextView buttonYouAtheist = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_atheist);
        TextView buttonYouHindu = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_hindu);
        TextView buttonYouChristian = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_christian);

        buttonYouIslam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleTextLeft = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_left);
                titleTextLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.colorIslam));
                IdeologyState.setYouIdeology(Ideology.ISLAM);
                hideAllSuggested();
                if(IdeologyState.areIdeologiesSet()) {
                    nextFragment();
                } else {
                    ImageView suggested = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_me_christian);
                    suggested.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonYouAtheist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleTextLeft = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_left);
                titleTextLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAtheist));
                IdeologyState.setYouIdeology(Ideology.SECULAR);
                hideAllSuggested();
                if(IdeologyState.areIdeologiesSet()) {
                    nextFragment();
                } else {
                    ImageView suggested = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_me_hindu);
                    suggested.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonYouHindu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleTextLeft = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_left);
                titleTextLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.colorHindu));
                IdeologyState.setYouIdeology(Ideology.HINDU);
                hideAllSuggested();
                if(IdeologyState.areIdeologiesSet()) {
                    nextFragment();
                } else {
                    ImageView suggested = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_me_atheist);
                    suggested.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonYouChristian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleTextLeft = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_left);
                titleTextLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.colorChristian));
                IdeologyState.setYouIdeology(Ideology.CHRISTIAN);
                hideAllSuggested();
                if(IdeologyState.areIdeologiesSet()) {
                    nextFragment();
                } else {
                    ImageView suggested = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_me_islam);
                    suggested.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setRightButtonListeners(){
        TextView buttonMeIslam = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_islam);
        TextView buttonMeAtheist = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_atheist);
        TextView buttonMeHindu = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_hindu);
        TextView buttonMeChristian = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_christian);

        buttonMeIslam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleTextRight = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_right);
                titleTextRight.setTextColor(ContextCompat.getColor(getContext(), R.color.colorIslam));
                IdeologyState.setMeIdeology(Ideology.ISLAM);
                hideAllSuggested();
                if(IdeologyState.areIdeologiesSet()) {
                    nextFragment();
                } else {
                    ImageView suggested = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_you_christian);
                    suggested.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonMeAtheist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleTextRight = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_right);
                titleTextRight.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAtheist));
                IdeologyState.setMeIdeology(Ideology.SECULAR);
                hideAllSuggested();
                if(IdeologyState.areIdeologiesSet()) {
                    nextFragment();
                } else {
                    ImageView suggested = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_you_hindu);
                    suggested.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonMeHindu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleTextRight = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_right);
                titleTextRight.setTextColor(ContextCompat.getColor(getContext(), R.color.colorHindu));
                IdeologyState.setMeIdeology(Ideology.HINDU);
                hideAllSuggested();
                if(IdeologyState.areIdeologiesSet()) {
                    nextFragment();
                } else {
                    ImageView suggested = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_you_atheist);
                    suggested.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonMeChristian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView titleTextRight = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_right);
                titleTextRight.setTextColor(ContextCompat.getColor(getContext(), R.color.colorChristian));
                IdeologyState.setMeIdeology(Ideology.CHRISTIAN);
                hideAllSuggested();
                if(IdeologyState.areIdeologiesSet()) {
                    nextFragment();
                } else {
                    ImageView suggested = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_you_islam);
                    suggested.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setFontAttributes() {
        TextView titleTextLeft = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_left);
        TextView titleTextRight = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_title_right);
        TextView subtitleTextLeft = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_subtitle_left);
        TextView subtitleTextRight = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_subtitle_right);
        TextView buttonYouIslam = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_islam);
        TextView buttonYouAtheist = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_atheist);
        TextView buttonYouHindu = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_hindu);
        TextView buttonYouChristian = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_christian);
        TextView buttonMeIslam = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_islam);
        TextView buttonMeAtheist = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_atheist);
        TextView buttonMeHindu = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_hindu);
        TextView buttonMeChristian = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_christian);

        Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "fonts/nimbus_sans_l_bold.otf"); //fonts/gobold.ttf
        Typeface italicFace=Typeface.createFromAsset(getActivity().getAssets(), "fonts/nimbus_sans_l_bold_italic.otf"); //fonts/gobold_italic.ttf
        titleTextLeft.setTypeface(italicFace);
        titleTextRight.setTypeface(italicFace);
        subtitleTextLeft.setTypeface(italicFace);
        subtitleTextRight.setTypeface(italicFace);
        buttonYouIslam.setTypeface(face);
        buttonYouAtheist.setTypeface(face);
        buttonYouHindu.setTypeface(face);
        buttonYouChristian.setTypeface(face);
        buttonMeIslam.setTypeface(face);
        buttonMeAtheist.setTypeface(face);
        buttonMeHindu.setTypeface(face);
        buttonMeChristian.setTypeface(face);
    }

    private void setFontSizes() {
        TextView buttonYouIslam = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_islam);
        TextView buttonYouAtheist = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_atheist);
        TextView buttonYouHindu = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_hindu);
        TextView buttonYouChristian = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_you_christian);
        TextView buttonMeIslam = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_islam);
        TextView buttonMeAtheist = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_atheist);
        TextView buttonMeHindu = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_hindu);
        TextView buttonMeChristian = (TextView) beliefSelectFragmentLayout.findViewById(R.id.belief_select_me_christian);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) ((displayMetrics.heightPixels / displayMetrics.scaledDensity) / 17);

        buttonYouIslam.setTextSize(height);
        buttonYouAtheist.setTextSize(height);
        buttonYouHindu.setTextSize(height);
        buttonYouChristian.setTextSize(height);
        buttonMeIslam.setTextSize(height);
        buttonMeAtheist.setTextSize(height);
        buttonMeHindu.setTextSize(height);
        buttonMeChristian.setTextSize(height);

        enlargeFirstLetter(buttonYouIslam);
        enlargeFirstLetter(buttonYouAtheist);
        enlargeFirstLetter(buttonYouHindu);
        enlargeFirstLetter(buttonYouChristian);
        enlargeFirstLetter(buttonMeIslam);
        enlargeFirstLetter(buttonMeAtheist);
        enlargeFirstLetter(buttonMeHindu);
        enlargeFirstLetter(buttonMeChristian);
    }

    private void hideAllSuggested() {
        ImageView suggestedYouIslam = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_you_islam);
        ImageView suggestedYouAtheist = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_you_atheist);
        ImageView suggestedYouHindu = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_you_hindu);
        ImageView suggestedYouChristian = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_you_christian);
        ImageView suggestedMeIslam = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_me_islam);
        ImageView suggestedMeAtheist = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_me_atheist);
        ImageView suggestedMeHindu = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_me_hindu);
        ImageView suggestedMeChristian = (ImageView) beliefSelectFragmentLayout.findViewById(R.id.suggested_me_christian);

        suggestedYouIslam.setVisibility(View.GONE);
        suggestedYouAtheist.setVisibility(View.GONE);
        suggestedYouHindu.setVisibility(View.GONE);
        suggestedYouChristian.setVisibility(View.GONE);
        suggestedMeIslam.setVisibility(View.GONE);
        suggestedMeAtheist.setVisibility(View.GONE);
        suggestedMeHindu.setVisibility(View.GONE);
        suggestedMeChristian.setVisibility(View.GONE);
    }

    private void enlargeFirstLetter(TextView buttonText ) {
        String tempStr = buttonText.getText().toString().substring(0, 1).toUpperCase() + buttonText.getText().toString().substring(1);
        SpannableString spannableString = new SpannableString(tempStr);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        buttonText.setText(spannableString);
    }

    private void nextFragment() {
        Fragment newFragment = new Fragment_03_FunQuestion();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}


