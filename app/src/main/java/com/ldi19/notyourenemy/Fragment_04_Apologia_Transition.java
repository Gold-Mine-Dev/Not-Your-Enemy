package com.ldi19.notyourenemy;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ldi19.notyourenemy.state.ApologiaState;
import com.ldi19.notyourenemy.state.GoodnessState;
import com.ldi19.notyourenemy.state.IdeologyState;
import com.ldi19.notyourenemy.app.RichTransitionFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_04_Apologia_Transition extends RichTransitionFragment {

    private FrameLayout creditsFragmentLayout;       //Linear Layout for introFragmentLayout animation

    public Fragment_04_Apologia_Transition() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        creditsFragmentLayout = (FrameLayout) inflater.inflate(R.layout.fragment_04_apologia_transition, container, false);
        loadBackground((ImageView) creditsFragmentLayout.findViewById(R.id.intro_background), BackgroundTypes.APOLOGIA);

        //Set button listener
        Button skipButton = (Button) creditsFragmentLayout.findViewById(R.id.left_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new Fragment_08_Legal_Transition();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.maincontainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button nextButton = (Button) creditsFragmentLayout.findViewById(R.id.right_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new Fragment_05_ApologiaQ1();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.maincontainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

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



        return creditsFragmentLayout;
    }
}
