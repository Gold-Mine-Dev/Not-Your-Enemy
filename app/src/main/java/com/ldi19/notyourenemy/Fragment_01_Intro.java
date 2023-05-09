package com.ldi19.notyourenemy;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ldi19.notyourenemy.app.RichOpeningImageFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_01_Intro extends RichOpeningImageFragment {

    private FrameLayout introFragmentLayout;       //Linear Layout for introFragmentLayout animation
    private Runnable autoNextCountdown;
    private Runnable autoTutorialCountdown;

    public Fragment_01_Intro() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        introFragmentLayout = (FrameLayout) inflater.inflate(R.layout.fragment_01_intro, container, false);
        loadBackground((ImageView) introFragmentLayout.findViewById(R.id.intro_background));

        //Proceed to next fragment if no click within 5 seconds
        autoNextCountdown = new Runnable() {
            @Override
            public void run() {
                nextFragment();
            }
        };
        introFragmentLayout.postDelayed(autoNextCountdown, 5000);

        //Set onClickListener to proceed to next fragment
        introFragmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introFragmentLayout.removeCallbacks(autoNextCountdown);
                nextFragment();
            }
        });

        return introFragmentLayout;
    }

    /***********************************************************************************************
     *  LIFECYCLE METHODS
     **********************************************************************************************/
    @Override
    public void onStop() {
        super.onStop();
        Log.e("DELAY", "Stopped");
        introFragmentLayout.removeCallbacks(autoNextCountdown);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("DELAY", "Restarted");
        introFragmentLayout.postDelayed(autoNextCountdown, 5000);
    }

    /***********************************************************************************************
     *  PRIVATE METHODS
     **********************************************************************************************/

    private void nextFragment() {
        introFragmentLayout.removeCallbacks(autoNextCountdown);
        introFragmentLayout.removeCallbacks(autoTutorialCountdown);
        Fragment newFragment = new Fragment_02_BeliefSelect();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.commit();
    }


}
