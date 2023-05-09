package com.ldi19.notyourenemy.app;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;

import com.ldi19.notyourenemy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class RichOpeningImageFragment extends Fragment {

    private ImageView theImageView;       // ImageView to contain Background
    private Bitmap bgBitmap;              // bgBitmap in the Bitmap format
    private BitmapDrawable bgDrawable;    // bgBitmap in the Drawable format


    /***********************************************************************************************
     *  BACKGROUND METHODS
     *  Methods used to start and stop the preloader & load and unload bgBitmap
     *  @see <a href="https://androidactivity.wordpress.com/2011/09/24/solution-for-outofmemoryerror-bitmap-size-exceeds-vm-budget/">Solution for OutOfMemoryError</a>
     **********************************************************************************************/

    public void loadBackground(ImageView theImageView) {
        this.theImageView = theImageView;
        bgBitmap = BitmapFactory.decodeStream(getResources().openRawResource(+ R.drawable.background_intro));
        bgDrawable = new BitmapDrawable(this.getResources(), bgBitmap);
        bgDrawable.setBounds(0, 0, bgDrawable.getIntrinsicWidth(), bgDrawable.getIntrinsicHeight());
        this.theImageView.setImageDrawable(bgDrawable);
    }

    public void unloadBackground() {
        //Delay for very slow exiting app/devices
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!(isAdded() && isVisible() && getUserVisibleHint())) {
                    if (theImageView != null)
                        theImageView.setImageDrawable(null);
                    if (bgDrawable != null) {
                        if (bgBitmap != null && !bgBitmap.isRecycled()) {
                            bgBitmap.recycle();
                            bgBitmap = null;
                        }
                    }
                    bgDrawable = null;
                }
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
