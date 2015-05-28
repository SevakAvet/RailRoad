package com.sevak_avet.domain.train;

import com.sevak_avet.domain.City;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Avetisyan Sevak
 * Date: 03.05.2015
 * Time: 17:26
 */
public class PassengerTrain extends Train {
    private String name;
    private List<City> route;

    private PassengerTrain(double speed) {
        super(speed);
    }

    public PassengerTrain(double speed, String name, List<City> route) {
        super(speed);
        this.name = name;
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public List<City> getRoute() {
        return route;
    }

    @Override
    public String toString() {
        return "PassengersTrain: {name = " + name + ", route = " + route.stream().map(City::getName).collect(Collectors.toList())+ "}";
    }
}
