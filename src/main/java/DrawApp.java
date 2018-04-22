
import bus.Redix;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Point;


public class DrawApp extends Application {



    @Override
    public void start(Stage stage) throws Exception {

        StackPane sp = new StackPane();
        Pane pane = new Pane();
        Scene scene = new Scene(sp, 600, 600, Color.ALICEBLUE);

        Circle c = new Circle();
        c.setRadius(10);
        c.setFill(Color.RED);
        sp.getChildren().add(pane);
        pane.getChildren().addAll(c);

        stage.setScene(scene);


        pane.setOnMouseMoved(evt -> {

            double x = evt.getX();
            double y = evt.getY();

            Point p = new Point();
            p.setX(x);
            p.setY(y);

            Redix.publish(JsonObject.mapFrom(p));
        });

        Redix.subscribe(json -> {

            String message = json.getString("message");
            Point point = Json.decodeValue(message, Point.class);

            Platform.runLater(() -> {
                c.setCenterX(point.getX());
                c.setCenterY(point.getY());
            });
        });


        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.err.println("Exit...");
        Redix.stop();
        super.stop();
    }

    public static void main(String[] args) {
        System.err.println("Main Thread = " + Thread.currentThread().getName());
        Application.launch(args);
    }
}