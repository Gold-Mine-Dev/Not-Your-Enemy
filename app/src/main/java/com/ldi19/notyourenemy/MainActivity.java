package com.ldi19.notyourenemy;

import android.content.res.Configuration;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ldi19.notyourenemy.state.IdeologyState;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();    //Get action bar
        actionBar.hide();                               //Kill it

        //Set introduction fragment
        changeFragment(new Fragment_01_Intro());
    }


    //Ignore orientation/keyboard change - Remain in Landscape mode
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    /***********************************************************************************************
     *  NAVIGATION METHODS
     **********************************************************************************************/
    public void changeFragment(Fragment fragment){
        FragmentManager frgManager = getSupportFragmentManager();
        FragmentTransaction trans = frgManager.beginTransaction();
        trans.replace(R.id.maincontainer, fragment);
        trans.commit();
    }

    public void newSession(){
        IdeologyState.clearIdeologyState();
        Fragment newFragment = new Fragment_02_BeliefSelect();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.commit();
    }
}
