package com.ldi19.notyourenemy.app;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;

import com.ldi19.notyourenemy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class RichTransitionFragment extends Fragment {

    // Image Delete Handler
    private Handler recycle_delay_handler;

    //Background
    private ImageView theImageView;       // ImageView to contain Background
    private Bitmap bgBitmap;              // bgBitmap in the Bitmap format
    private BitmapDrawable bgDrawable;    // bgBitmap in the Drawable format

    //Background Types
    public enum BackgroundTypes {
        INTRO,
        APOLOGIA,
        LEGAL,
        GOODNESS
    }


    /***********************************************************************************************
     *  BACKGROUND METHODS
     *  Methods used to start and stop the preloader & load and unload bgBitmap
     *  @see <a href="https://androidactivity.wordpress.com/2011/09/24/solution-for-outofmemoryerror-bitmap-size-exceeds-vm-budget/">Solution for OutOfMemoryError</a>
     **********************************************************************************************/

    //Background
    public void loadBackground(ImageView theImageView) {
        this.theImageView = theImageView;

        if (recycle_delay_handler != null) {
            recycle_delay_handler.removeCallbacksAndMessages(null);
        }

        if (bgBitmap == null) {
            bgBitmap = BitmapFactory.decodeStream(getResources().openRawResource(+ R.drawable.background_intro_dark));
        }

        bgDrawable = new BitmapDrawable(this.getResources(), bgBitmap);
        bgDrawable.setBounds(0, 0, bgDrawable.getIntrinsicWidth(), bgDrawable.getIntrinsicHeight());
        this.theImageView.setImageDrawable(bgDrawable);
    }


    public void loadBackground(ImageView theImageView, BackgroundTypes theBackgroundType) {
        this.theImageView = theImageView;

        if (recycle_delay_handler != null) {
            recycle_delay_handler.removeCallbacksAndMessages(null);
        }

        switch (theBackgroundType) {
            case INTRO:
                bgBitmap = BitmapFactory.decodeStream(getResources().openRawResource(+ R.drawable.background_intro_dark));
                break;
            case APOLOGIA:
                bgBitmap = BitmapFactory.decodeStream(getResources().openRawResource(+ R.drawable.background_apologia_dark));
                break;
            case LEGAL:
                bgBitmap = BitmapFactory.decodeStream(getResources().openRawResource(+ R.drawable.background_legal_dark));
                break;
            case GOODNESS:
                bgBitmap = BitmapFactory.decodeStream(getResources().openRawResource(+ R.drawable.background_goodness_dark));
                break;
        }

        bgDrawable = new BitmapDrawable(this.getResources(), bgBitmap);
        bgDrawable.setBounds(0, 0, bgDrawable.getIntrinsicWidth(), bgDrawable.getIntrinsicHeight());
        this.theImageView.setImageDrawable(bgDrawable);
    }

    public void unloadBackground() {
        //Delay for very slow exiting app/devices
        recycle_delay_handler = new Handler();
        recycle_delay_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (theImageView != null)
                    theImageView.setImageDrawable(null);
                if (bgDrawable != null) {
                    if (bgBitmap != null && !bgBitmap.isRecycled()) {
                        bgBitmap.recycle();
                        bgBitmap = null;
                        Log.e("IMAGE", "RECYCLED");
                    }
                }
                bgDrawable = null;
            }
        }, 2000);
    }


    /***********************************************************************************************
     *  LIFECYCLE METHODS
     *  Methods used to process references to large image objects on fragment transactions
     **********************************************************************************************/

    @Override
    public void onDestroy() {
        unloadBackground();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        unloadBackground();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (theImageView != null) {
            loadBackground(theImageView);
        }
    }


    /***********************************************************************************************
     *  HELPER METHODS
     *  Methods used to add features or avoid deprecation
     **********************************************************************************************/

    /**
     * Returns Spannable stylized text from html string (functions correctly across API versions)
     * @param html String representing stylized text
     * @return Spanned object representing styled text
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

}
