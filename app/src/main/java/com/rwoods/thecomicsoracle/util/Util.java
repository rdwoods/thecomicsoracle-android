package com.rwoods.thecomicsoracle.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by rahmanwoods on 4/1/16.
 */
public class Util {
    private static DecimalFormat df;

    public static DecimalFormat getDf() {

        if (df == null) {
            df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
        }

        return df;
    }
}
