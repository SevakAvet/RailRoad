package com.sevak_avet.domain.train;

/**
 * Created by Avetisyan Sevak
 * Date: 03.05.2015
 * Time: 17:26
 */
public class FreightTrain extends Train {
    public FreightTrain(double speed) {
        super(speed);
    }

    @Override
    public String toString() {
        return "FreightTrain {speed = " + getSpeed() + "}";
    }
}
