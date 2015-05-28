package com.sevak_avet.domain;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Avetisyan Sevak
 Date: 03.05.2015
 Time: 17:25
 */
public class City {
    private int x;
    private int y;
    private String name;
    private List<City> neighbours;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public List<City> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<City> neighbours) {
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return "City{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                ", neighbours=" + neighbours.stream().map(City::getName).collect(Collectors.toList()) +
                '}';
    }
}
