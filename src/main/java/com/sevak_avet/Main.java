package com.sevak_avet;

import com.sevak_avet.domain.City;
import com.sevak_avet.domain.train.FreightTrain;
import com.sevak_avet.domain.train.Train;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

import static com.sevak_avet.Transitions.getPath;
import static com.sevak_avet.Transitions.getTrainRectangles;
import static com.sevak_avet.Transitions.getTrainTransitions;
import static com.sevak_avet.Parser.parse;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.copyOfRange;

public class Main extends Application {
    private static int width;
    private static int height;
    private static List<City> cities;
    private static List<Train> passengersTrains;

    public static void main(String[] args) throws IOException {
        initialize(args[0]);
        launch(args);
    }

    private static void initialize(String file) {
        try {
            parse(file);
            width = Parser.getWidth();
            height = Parser.getHeight();
            cities = Parser.getCities();
            passengersTrains = Parser.getPassengersTrains();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Drawer.setRoot(root);
        Transitions.setRoot(root);

        cities.forEach(Drawer::drawCity);
        cities.forEach(Drawer::drawRailRoad);
        passengersTrains.forEach(x -> getPath(x).play());

        Timelines.checkCollisions.play();
        Timelines.createFreightTrains.play();
    }
}
