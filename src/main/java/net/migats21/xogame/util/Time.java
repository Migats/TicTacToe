/*
 * Copyright (c) 2023 Migats21
 * net.migats21.xogame.util.Time (Obfuscated variables)
 */
package net.migats21.xogame.util;

public class Time {
    private static final float STARTING_TIME = System.nanoTime();
    private static float lastTime = getTime();

    public static float getTime() {
        return (System.nanoTime() - STARTING_TIME) * 1E-9f;
    }

    public static float getDeltaTime() {
        float currentTime = getTime();
        float deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        return deltaTime;
    }
}
