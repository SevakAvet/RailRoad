package com.sevak_avet.domain.train;

/**
 * Created by Avetisyan Sevak
 * Date: 03.05.2015
 * Time: 17:25
 */
public abstract class Train {
    private double speed;

    protected Train(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public abstract String toString();
}
