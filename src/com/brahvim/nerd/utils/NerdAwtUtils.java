package com.brahvim.nerd.utils;

import java.awt.AWTException;
import java.awt.Robot;

public class NerdAwtUtils {

    private NerdAwtUtils() {
        NerdReflectionUtils.rejectStaticClassInstantiationFor(this);
    }

    public static Robot createAwtRobot() {
        Robot temporaryRobot = null;

        try {
            temporaryRobot = new Robot();
        } catch (final AWTException e) {
            e.printStackTrace();
        }
        return temporaryRobot;
    }

}
