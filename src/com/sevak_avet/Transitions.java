package com.sevak_avet;

import com.sevak_avet.domain.City;
import com.sevak_avet.domain.train.FreightTrain;
import com.sevak_avet.domain.train.PassengerTrain;
import com.sevak_avet.domain.train.Train;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sevak_avet.Parser.getCities;
import static java.lang.Math.sqrt;

/**
 * Created by Sevak Avetisyan
 * Date: 5/11/15
 * Time: 3:00 PM
 */
public class Transitions {
    private static Map<Train, Rectangle> trainRectangles = new HashMap<>();
    private static Map<Train, Transition> trainTransitions = new HashMap<>();
    private static List<City> cities = getCities();
    private static Group root;

    public static void setRoot(Group root) {
        Transitions.root = root;
    }

    public static Transition getPath(Train train) {
        if (train instanceof PassengerTrain) {
            return getPassengerPath((PassengerTrain) train);
        }

        return createFreightPath((FreightTrain) train);
    }

    public static Transition getPassengerPath(PassengerTrain train) {
        if (trainTransitions.containsKey(train)) {
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

    private static double calculateTime(City from, City to, double v) {
        int dx = from.getX() - to.getX();
        int dy = from.getY() - to.getY();
        return sqrt(dx * dx + dy * dy) / v;
    }

    private static List<City> generateRandomRoute(int n) {
        List<City> route = new ArrayList<>();

        City cur = cities.get((int) (Math.random() * (cities.size() - 1)));
        route.add(cur);

        for (int i = 0; i < n - 1; i++) {
            cur = cur.getNeighbours().get((int) (Math.random() * (cur.getNeighbours().size() - 1)));
            route.add(cur);
        }

        return route;
    }

    private static Transition createFreightPath(FreightTrain train) {
        List<City> route = generateRandomRoute(1 + (int) (Math.random() * (cities.size() - 2)));
        Path path = new Path();

        path.getElements().add(new MoveTo(route.get(0).getX(), route.get(0).getY()));

        double time = 0;
        for (int i = 1; i < route.size(); ++i) {
            City from = route.get(i - 1);
            City to = route.get(i);
            time += calculateTime(from, to, train.getSpeed());

            path.getElements().add(new LineTo(to.getX(), to.getY()));
        }

        Rectangle rectangle = new Rectangle(8, 7, Color.BLACK);
        root.getChildren().add(rectangle);
        trainRectangles.put(train, rectangle);

        PathTransition transition = new PathTransition();
        transition.setDuration(Duration.seconds(time));
        transition.setPath(path);
        transition.setNode(rectangle);
        transition.setCycleCount(1);
        transition.setOnFinished(e -> root.getChildren().remove(rectangle));
        trainTransitions.put(train, transition);

        return transition;
    }

    public static Map<Train, Rectangle> getTrainRectangles() {
        return trainRectangles;
    }

    public static Map<Train, Transition> getTrainTransitions() {
        return trainTransitions;
    }
}
