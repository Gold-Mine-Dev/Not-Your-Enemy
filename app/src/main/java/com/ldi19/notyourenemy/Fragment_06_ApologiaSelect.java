package com.ldi19.notyourenemy;


import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ldi19.notyourenemy.state.ApologiaState;
import com.ldi19.notyourenemy.state.IdeologyState;
import com.ldi19.notyourenemy.state.ApologiaDataInterface;
import com.ldi19.notyourenemy.app.Ideology;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * ListFragment implementation - https://www.tutorialspoint.com/android/android_list_fragment.htm
 */
public class Fragment_06_ApologiaSelect extends ListFragment implements OnItemClickListener {

    private FrameLayout modeSelectFragmentLayout;       //Linear Layout for introFragmentLayout animation
    private List<String> arguments;

    public Fragment_06_ApologiaSelect() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        modeSelectFragmentLayout = (FrameLayout) inflater.inflate(R.layout.fragment_06_apologia_select, container, false);
        modeSelectFragmentLayout.setBackgroundColor(IdeologyState.youColor(getContext()));
        setTextAttributes();
        return modeSelectFragmentLayout;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {  }

    /***********************************************************************************************
     *  INSTANTIATION METHODS
     **********************************************************************************************/

    public static Fragment_06_ApologiaSelect newListModeInstance() {
        Fragment_06_ApologiaSelect myFragment = new Fragment_06_ApologiaSelect();
        Bundle args = new Bundle();
        args.putBoolean("listMode", true);
        myFragment.setArguments(args);
        return myFragment;
    }

    /***********************************************************************************************
     *  PRIVATE METHODS
     **********************************************************************************************/

    private void setTextAttributes() {
        //Set Primary Attributes
        TextView mainSlideText = (TextView) modeSelectFragmentLayout.findViewById(R.id.discuss_main_text);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        mainSlideText.setTextSize(width / 40);

        //Set Primary Text
        mainSlideText.setText(Html.fromHtml("Discuss these errors <b>with " + toCamelCase(IdeologyState.getYouIdeology().getString()) + "</b>"));
    }



    private static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }


    /***********************************************************************************************
     *  LIFECYCLE METHODS
     *  Font change - http://www.android--tutorials.com/2016/10/android-change-listview-text-color-font.html
     **********************************************************************************************/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (IdeologyState.getYouIdeology() == Ideology.ISLAM) {
            arguments = ApologiaDataInterface.listOfIslamicArguments(getContext());
        } else if (IdeologyState.getYouIdeology() == Ideology.SECULAR) {
            arguments = ApologiaDataInterface.listOfAtheistArguments(getContext());
        } else if (IdeologyState.getYouIdeology() == Ideology.HINDU) {
            arguments = ApologiaDataInterface.listOfHinduArguments(getContext());
        } else if (IdeologyState.getYouIdeology() == Ideology.CHRISTIAN) {
            arguments = ApologiaDataInterface.listOfChristianArguments(getContext());
        }

        //Customize adapter to utilize specified font, size and color
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, arguments){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the typeface/font for the current item
                item.setTextColor(Color.parseColor("#99000000"));
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);

                // return the view
                return item;
            }
        };
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ApologiaState.setCurrentArgument(arguments.get(i));
        ApologiaState.setCurrentSlideNumber(0);
        nextFragment();
    }

    private void nextFragment() {
        Fragment newFragment = new Fragment_07_ApologiaFlowchart();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        transaction.addToBackStack(null); // Do add modeSelectFragment to backstack
        transaction.commit();
    }

    private void nextFragmentNoBackstack() {
        Fragment newFragment = new Fragment_07_ApologiaFlowchart();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.maincontainer, newFragment);
        //transaction.addToBackStack(null); // Do add modeSelectFragment to backstack
        transaction.commit();
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
                    if(ApologiaState.areSlidesEmpty()) {
                        return false;
                    } else {
                        nextFragmentNoBackstack();
                        return true;
                    }

                }
                return false;
            }
        });

    }
}
