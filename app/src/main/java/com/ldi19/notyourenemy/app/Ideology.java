package com.ldi19.notyourenemy.app;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.ldi19.notyourenemy.R;

public enum Ideology {
    ISLAM,
    SECULAR,
    HINDU,
    CHRISTIAN;

    public int getColor(Context theContext) {
        switch (this) {
            case ISLAM:
                return ContextCompat.getColor(theContext, R.color.colorIslam);
            case SECULAR:
                return ContextCompat.getColor(theContext, R.color.colorAtheist);
            case HINDU:
                return ContextCompat.getColor(theContext, R.color.colorHindu);
            case CHRISTIAN:
                return ContextCompat.getColor(theContext, R.color.colorChristian);
            default:
                throw new AssertionError("Unknown ideology " + this);
        }
    }

    public int getColorLight(Context theContext) {
        switch (this) {
            case ISLAM:
                return ContextCompat.getColor(theContext, R.color.colorIslamLight);
            case SECULAR:
                return ContextCompat.getColor(theContext, R.color.colorAtheistLight);
            case HINDU:
                return ContextCompat.getColor(theContext, R.color.colorHinduLight);
            case CHRISTIAN:
                return ContextCompat.getColor(theContext, R.color.colorChristianLight);
            default:
                throw new AssertionError("Unknown ideology " + this);
        }
    }

    public String getString() {
        switch (this) {
            case ISLAM:
                return "Islam";
            case SECULAR:
                return "Secularism";
            case HINDU:
                return "Hindu";
            case CHRISTIAN:
                return "Christian";
            default:
                throw new AssertionError("Unknown ideology " + this);
        }
    }

    public String getShortString() {
        switch (this) {
            case ISLAM:
                return "Islam";
            case SECULAR:
                return "Secular";
            case HINDU:
                return "Hindu";
            case CHRISTIAN:
                return "Christian";
            default:
                throw new AssertionError("Unknown ideology " + this);
        }
    }
}
