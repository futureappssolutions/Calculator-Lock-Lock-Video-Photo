package com.calculator.vaultlocker.panicswitch;


public interface AccelerometerListener {
    void onAccelerationChanged(float f, float f2, float f3);

    void onShake(float f);
}
