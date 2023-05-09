package com.ldi19.notyourenemy;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogFragment_02_Belief_Select extends DialogFragment {

    private View v;

    public static DialogFragment_02_Belief_Select newInstance() {
        DialogFragment_02_Belief_Select diag_frag = new DialogFragment_02_Belief_Select();
        diag_frag.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Dialog);
        return diag_frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Remove the default background
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Inflate the new view with margins and background
        v = inflater.inflate(R.layout.dialog_fragment_02_belief_select, container, false);

        // Set HTML String text
        TextView dialogText = (TextView) v.findViewById(R.id.dialog_text);
        dialogText.setText(Html.fromHtml(getString(R.string.diag_frag_2_text)));

        // Set dialog close button
        ImageButton closeButton = (ImageButton) v.findViewById(R.id.dialog_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ImageView image = v.findViewById(R.id.dialog_image);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chart);
                image.getLayoutParams().height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
                final double imageRatio = (double) image.getHeight() / (double) bitmap.getHeight();
                image.getLayoutParams().width = (int) (bitmap.getWidth() * imageRatio);

                int dialogWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                int dialogHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
                getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        resizeText();
    }

    private void resizeText() {
        TextView dialogText = (TextView) v.findViewById(R.id.dialog_text);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) ((displayMetrics.heightPixels / displayMetrics.scaledDensity) / 28);
        dialogText.setTextSize(height);
    }
}