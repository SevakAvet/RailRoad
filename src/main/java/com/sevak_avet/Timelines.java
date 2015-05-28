package com.sevak_avet;

import com.sevak_avet.domain.train.FreightTrain;
import com.sevak_avet.domain.train.Train;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.List;

import static com.sevak_avet.Parser.*;
import static com.sevak_avet.Transitions.getPath;
import static com.sevak_avet.Transitions.getTrainRectangles;
import static com.sevak_avet.Transitions.getTrainTransitions;

/**
 * Created by Sevak Avetisyan
 * Date: 5/11/15
 * Time: 3:14 PM
 */
public class Timelines {
    private static List<Train> passengersTrains = getPassengersTrains();
    private static List<Train> freightTrains = getFreightTrains();

    public static Timeline checkCollisions = new Timeline(new KeyFrame(Duration.millis(50), event -> {
        passengersTrains.stream().forEach(x -> {
            Rectangle rectangle = getTrainRectangles().get(x);

            passengersTrains.stream().forEach(y -> {
                Rectangle cur = getTrainRectangles().get(y);

                if (rectangle != cur) {
                    Shape intersect = Rectangle.intersect(rectangle, cur);
                    if (intersect.getBoundsInLocal().getWidth() != -1) {
                        rectangle.setFill(Color.RED);
                        cur.setFill(Color.RED);

                        getTrainTransitions().get(x).stop();
                        getTrainTransitions().get(y).stop();
                    }
                }
            });
        });
    }));

    public static Timeline createFreightTrains = new Timeline(new KeyFrame(Duration.millis(3000), event -> {
        if((int) (Math.random() * 50) % 2 == 0) {
            FreightTrain freightTrain = new FreightTrain(50 + Math.random() * 20);
            getPath(freightTrain).play();
        } else {
            getPath(freightTrains.get((int) (Math.random() * (freightTrains.size() - 1)))).play();
        }
    }));

    static {
        checkCollisions.setCycleCount(Animation.INDEFINITE);
        createFreightTrains.setCycleCount(Animation.INDEFINITE);
    }

}
