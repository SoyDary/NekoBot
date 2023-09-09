package com.github.SoyDary.NekoBot.Util;

import java.awt.Color;

public class Utils {

	public static String hexColor(Color color) {
        String R = Integer.toHexString(color.getRed());
        String G = Integer.toHexString(color.getGreen());
        String B = Integer.toHexString(color.getBlue());
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        return R+G+B;
	}
}
