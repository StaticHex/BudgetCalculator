package budget;
import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BudgetCalculator extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	// Set up primary window
        String contentStyle = String.format("-fx-background: rgb(%d, %d, %d);"+
                							"-fx-background-color: -fx-background;", 255,255,255);
        primaryStage.setTitle("Budget Calculator v1.0");
        final Random rng = new Random();
        final VBox content = new VBox(5);
        		   content.setPadding(new Insets(10,10,10,10));
        ScrollPane scroller = new ScrollPane(content);
        		   scroller.setStyle(contentStyle);
        scroller.setFitToWidth(true);
        
        // Set up calendar button
        Button dateButton = new Button ("YYYY/MM/DD-YYYY/MM/DD");
        	   dateButton.setMaxWidth(Double.MAX_VALUE);
        Label balanceText = new Label("Total:");
        Label balanceAmount = new Label("$0.00");
        final BorderPane balancePane = new BorderPane(null,null,balanceAmount,null,balanceText);
        final BorderPane buttonPane = new BorderPane(dateButton, null,null,null,null);
        final BorderPane contentPane = new BorderPane(scroller,buttonPane,null,balancePane,null);
        				 contentPane.setPadding(new Insets(10,10,10,10));
        
        // Set up option Buttons to add a transaction      		   
        final AnchorPane optionPane = new AnchorPane();
		  				 optionPane.setMaxWidth(Double.MAX_VALUE);
        Button addButton = new Button("+");
        	   addButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Button saveButton = new Button("💾");
        	   saveButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Button loadButton = new Button("📂");
               loadButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Button newButton = new Button("🗋");
        	   newButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Button settingsButton = new Button("🛠");
        	   settingsButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        AnchorPane.setLeftAnchor(addButton, 10.0);
        AnchorPane.setBottomAnchor(addButton, 10.0);
        AnchorPane.setTopAnchor(addButton, 10.0);
        AnchorPane.setRightAnchor(settingsButton, 10.0);
        AnchorPane.setBottomAnchor(settingsButton, 10.0);
        AnchorPane.setTopAnchor(settingsButton, 10.0);
        AnchorPane.setRightAnchor(saveButton, 65.0);
        AnchorPane.setBottomAnchor(saveButton, 10.0);
        AnchorPane.setTopAnchor(saveButton, 10.0);
        AnchorPane.setRightAnchor(loadButton, 120.0);
        AnchorPane.setBottomAnchor(loadButton, 10.0);
        AnchorPane.setTopAnchor(loadButton, 10.0);
        AnchorPane.setRightAnchor(newButton, 175.0);
        AnchorPane.setBottomAnchor(newButton, 10.0);
        AnchorPane.setTopAnchor(newButton, 10.0);
        optionPane.getChildren().addAll(addButton, settingsButton, saveButton, loadButton, newButton); 
        
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
	            	   editButton.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
	            editButton.setOnAction(new EventHandler<ActionEvent>() {
	            	@Override
	                public void handle(ActionEvent event) {
	    				Alert alert = new Alert(AlertType.INFORMATION);
	    				alert.setTitle("Trying to edit an entry?");
	    				alert.setHeaderText("Sorry, there's nothing here yet");
	    				alert.setContentText("In the future you can edit stuff with this though.");
	    				alert.showAndWait();
	            	}
	            });
	            Button removeButton = new Button("✕");
	            	   removeButton.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
	            removeButton.setOnAction(new EventHandler<ActionEvent>() {
	            	@Override
	                public void handle(ActionEvent event) {
	            		content.getChildren().remove(anchorPane);
	            	}
	            });
	            AnchorPane.setRightAnchor(editButton, 45.0);
	            AnchorPane.setTopAnchor(editButton, 5.0);
	            AnchorPane.setBottomAnchor(editButton, 5.0);
	            AnchorPane.setRightAnchor(removeButton, 5.0);
	            AnchorPane.setTopAnchor(removeButton, 5.0);
	            AnchorPane.setBottomAnchor(removeButton, 5.0);
	            anchorPane.getChildren().addAll(label, editButton, removeButton);
	            content.getChildren().add(anchorPane);
        	}
        });

        // Set up our scene and then show
        Scene scene = new Scene(new BorderPane(contentPane, null, null, optionPane, null), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
