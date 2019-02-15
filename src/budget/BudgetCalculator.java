package budget;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class BudgetCalculator extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Budget Calculator v1.0");
        final Random rng = new Random();
        final VBox content = new VBox(5);
        ScrollPane scroller = new ScrollPane(content);
        scroller.setFitToWidth(true);

        Button addButton = new Button("+");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
	            final AnchorPane anchorPane = new AnchorPane();
	            String style = String.format("-fx-background: rgb(%d, %d, %d);"+
	                    "-fx-background-color: -fx-background;",
	                    rng.nextInt(256),
	                    rng.nextInt(256),
	                    rng.nextInt(256));
	            anchorPane.setStyle(style);
	            Label label = new Label("Pane "+(content.getChildren().size()+1));
	            AnchorPane.setLeftAnchor(label, 5.0);
	            AnchorPane.setTopAnchor(label, 5.0);
	            Button editButton = new Button("✎");
	            editButton.setOnAction(new EventHandler<ActionEvent>() {
	            	@Override
	                public void handle(ActionEvent event) {
	            		content.getChildren().remove(anchorPane);
	            	}
	            });
	            Button removeButton = new Button("X");
	            removeButton.setOnAction(new EventHandler<ActionEvent>() {
	            	@Override
	                public void handle(ActionEvent event) {
	            		content.getChildren().remove(anchorPane);
	            	}
	            });
	            AnchorPane.setRightAnchor(editButton, 35.0);
	            AnchorPane.setTopAnchor(editButton, 5.0);
	            AnchorPane.setBottomAnchor(editButton, 5.0);
	            AnchorPane.setRightAnchor(removeButton, 5.0);
	            AnchorPane.setTopAnchor(removeButton, 5.0);
	            AnchorPane.setBottomAnchor(removeButton, 5.0);
	            anchorPane.getChildren().addAll(label, editButton, removeButton);
	            content.getChildren().add(anchorPane);
        	}
        });

        Scene scene = new Scene(new BorderPane(scroller, null, null, addButton, null), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
