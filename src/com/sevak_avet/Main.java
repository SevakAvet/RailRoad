package com.sevak_avet;

import com.sevak_avet.domain.City;
import com.sevak_avet.domain.train.FreightTrain;
import com.sevak_avet.domain.train.PassengerTrain;
import com.sevak_avet.domain.train.Train;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

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
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.util.Arrays.copyOfRange;

public class Main extends Application {
    private static double R = 20.0;

    private static int width;
    private static int height;
    private static List<City> cities = new ArrayList<>();
    private static Map<String, City> citiesMap = new HashMap<>();
    private static List<Train> passengersTrains = new ArrayList<>();
    private static List<Train> freightTrains = new ArrayList<>();
    private static Map<Train, Rectangle> trainRectangles = new HashMap<>();
    private static Map<Train, Transition> trainTransitions = new HashMap<>();

    private Group root;

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/input.txt"))) {
            reader.lines().forEach(x ->
            {
                String[] split = x.split(" ");
                if (split[0].toLowerCase().equals("size")) {
                    width = parseInt(split[1]);
                    height = parseInt(split[2]);
                } else if (split[0].toLowerCase().equals("city")) {
                    System.out.println("CITY!!!");
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

        System.out.println(cities);

        launch(args);
    }

    private void drawCity(City city) {
        Label cityName = new Label(city.getName());
        cityName.setLayoutX(city.getX() - R);
        cityName.setLayoutY(city.getY() - 2 * R);

        Circle cityCircle = new Circle(city.getX(), city.getY(), R);
        cityCircle.setFill(Color.GREENYELLOW);
        root.getChildren().addAll(cityName, cityCircle);
    }

    private void drawRailRoad(City city) {
        city.getNeighbours().forEach(x -> {
            Line line = new Line(city.getX(), city.getY(), x.getX(), x.getY());
            line.setStrokeWidth(2);
            root.getChildren().add(line);
        });
    }

    private Transition getPath(Train train) {
        if (train instanceof PassengerTrain) {
            return getPassengerPath((PassengerTrain) train);
        }

        return createFreightPath((FreightTrain) train);
    }

    private Transition getPassengerPath(PassengerTrain train) {
        if(trainTransitions.containsKey(train)) {
            return trainTransitions.get(train);
        }

        List<City> route = train.getRoute();

        Path path = new Path();
        path.getElements().add(new MoveTo(route.get(0).getX(), route.get(0).getY()));

        double time = 0;
        for (int i = 1; i < route.size(); ++i) {
            City from = route.get(i - 1);
            City to = route.get(i);
            time += calculateTime(from, to, train.getSpeed());

            path.getElements().add(new LineTo(to.getX(), to.getY()));
        }

        City from = route.get(route.size() - 1);
        City to = route.get(0);
        time += calculateTime(from, to, train.getSpeed());

        path.getElements().add(new LineTo(to.getX(), to.getY()));
        Rectangle rectangle = new Rectangle(5, 10, Color.BROWN);
        root.getChildren().add(rectangle);
        trainRectangles.put(train, rectangle);

        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(time));
        transition.setPath(path);
        transition.setNode(rectangle);
        transition.setCycleCount(Animation.INDEFINITE);
        trainTransitions.put(train, transition);

        return transition;
    }

    private double calculateTime(City from, City to, double v) {
        int dx = from.getX() - to.getX();
        int dy = from.getY() - to.getY();
        return sqrt(dx * dx + dy * dy) / v;
    }

    private Transition createFreightPath(FreightTrain train) {
        Path path = new Path();
        PathTransition transition = new PathTransition();
        return transition;
    }

    @Override
    public void start(Stage stage) throws Exception {
        root = new Group();
        stage.setScene(new Scene(root, width, height));
        stage.setResizable(false);
        stage.show();

        cities.forEach(this::drawCity);
        cities.forEach(this::drawRailRoad);
        passengersTrains.forEach(x -> getPath(x).play());

        Timeline checkCollisions = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            passengersTrains.stream().forEach(x -> {
                Rectangle rectangle = trainRectangles.get(x);

                passengersTrains.stream().forEach(y -> {
                    Rectangle cur = trainRectangles.get(y);

                    if(rectangle != cur) {
                        Shape intersect = Rectangle.intersect(rectangle, cur);
                        if(intersect.getBoundsInLocal().getWidth() != -1) {
                            rectangle.setFill(Color.RED);
                            cur.setFill(Color.RED);

                            trainTransitions.get(x).stop();
                            trainTransitions.get(y).stop();
                        }
                    }
                });
            });
        }));

        checkCollisions.setCycleCount(Timeline.INDEFINITE);
        checkCollisions.play();
    }
}
