package com.sevak_avet;

import com.sevak_avet.domain.City;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created by Sevak Avetisyan
 * Date: 5/11/15
 * Time: 3:01 PM
 */
public class Drawer {
    private static double R = 20;
    private static Group root;

    public static void setRoot(Group root) {
        Drawer.root = root;
    }

    public static void drawCity(City city) {
        Label cityName = new Label(city.getName());
        cityName.setLayoutX(city.getX() - R);
        cityName.setLayoutY(city.getY() - 2 * R);

        Circle cityCircle = new Circle(city.getX(), city.getY(), R);
        cityCircle.setFill(Color.GREENYELLOW);
        root.getChildren().addAll(cityName, cityCircle);
    }

    public static void drawRailRoad(City city) {
        city.getNeighbours().forEach(x -> {
            Line line = new Line(city.getX(), city.getY(), x.getX(), x.getY());
            line.setStrokeWidth(2);
            root.getChildren().add(line);
        });
    }
}
