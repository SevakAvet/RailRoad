package com.sevak_avet;

import com.sevak_avet.domain.City;
import com.sevak_avet.domain.train.FreightTrain;
import com.sevak_avet.domain.train.PassengerTrain;
import com.sevak_avet.domain.train.Train;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.copyOfRange;

/**
 * Created by Sevak Avetisyan
 * Date: 5/11/15
 * Time: 2:51 PM
 */
public class Parser {
    private static int width;
    private static int height;
    private static List<City> cities = new ArrayList<>();
    private static Map<String, City> citiesMap = new HashMap<>();
    private static List<Train> passengersTrains = new ArrayList<>();
    private static List<Train> freightTrains = new ArrayList<>();

    public static void parse(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines().forEach(x -> {
                String[] split = x.split(" ");
                if (split[0].toLowerCase().equals("size")) {
                    width = parseInt(split[1]);
                    height = parseInt(split[2]);
                } else if (split[0].toLowerCase().equals("city")) {
                    City city = new City(split[1], parseInt(split[3]), parseInt(split[2]));
                    citiesMap.put(city.getName(), city);
                } else if (split[0].toLowerCase().equals("neighbours")) {
                    City city = citiesMap.get(split[1]);
                    city.setNeighbours(Stream.of(copyOfRange(split, 2, split.length))
                            .map(citiesMap::get)
                            .collect(Collectors.toList()));
                    cities.add(city);
                } else if (split[0].toLowerCase().equals("train")) {
                    if (split[1].toLowerCase().startsWith("passenger")) {
                        List<City> cities = Stream.of(copyOfRange(split, 4, split.length))
                                .map(citiesMap::get)
                                .collect(Collectors.toList());
                        Train train = new PassengerTrain(parseInt(split[3]), split[2], cities);
                        passengersTrains.add(train);
                    } else if (split[1].toLowerCase().equals("freight")) {
                        Train train = new FreightTrain(parseInt(split[2]));
                        freightTrains.add(train);
                    } else {
                        try {
                            throw new Exception("Unknown train type: " + split[1] + ". Must be 'passenger' or 'freight'");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static List<City> getCities() {
        return cities;
    }

    public static List<Train> getPassengersTrains() {
        return passengersTrains;
    }

    public static List<Train> getFreightTrains() {
        return freightTrains;
    }
}
