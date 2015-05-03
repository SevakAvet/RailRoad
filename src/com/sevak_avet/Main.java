package com.sevak_avet;

import com.sevak_avet.domain.City;
import com.sevak_avet.domain.train.FreightTrain;
import com.sevak_avet.domain.train.PassengerTrain;
import com.sevak_avet.domain.train.Train;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

public class Main extends Application {
    private static int width;
    private static int height;
    private static List<City> cities = new ArrayList<>();
    private static Map<String, City> citiesMap = new HashMap<>();
    private static List<Train> passengersTrains = new ArrayList<>();
    private static List<Train> freightTrains = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/input.txt"))) {
            reader.lines().forEach(x ->
            {
                String[] split = x.split(" ");
                if (x.toLowerCase().startsWith("size")) {
                    width = parseInt(split[1]);
                    height = parseInt(split[2]);
                } else if (x.toLowerCase().startsWith("city")) {
                    City city = new City(split[1], parseInt(split[3]), parseInt(split[2]));
                    citiesMap.put(city.getName(), city);
                } else if (x.toLowerCase().startsWith("neighbours")) {
                    City city = citiesMap.get(split[1]);
                    city.setNeighbours(Stream.of(copyOfRange(split, 2, split.length))
                            .map(citiesMap::get)
                            .collect(Collectors.toList()));
                    cities.add(city);
                } else if (x.toLowerCase().startsWith("train")) {
                    if (split[1].toLowerCase().startsWith("passenger")) {
                        List<City> cities = Stream.of(copyOfRange(split, 4, split.length))
                                .map(citiesMap::get)
                                .collect(Collectors.toList());
                        Train train = new PassengerTrain(parseInt(split[3]), split[2], cities);
                        passengersTrains.add(train);
                    } else if (split[1].toLowerCase().startsWith("freight")) {
                        Train train = new FreightTrain(parseInt(split[2]));
                        freightTrains.add(train);
                    } else {
                        throw new IllegalArgumentException("Unknown train type: " + split[1] + ". Must be 'passenger' or 'freight'");
                    }
                }
            });
        }


        System.out.println(cities);
        System.out.println(passengersTrains);
        System.out.println(freightTrains);

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        stage.setScene(new Scene(root, width, height));
        stage.show();
    }
}
