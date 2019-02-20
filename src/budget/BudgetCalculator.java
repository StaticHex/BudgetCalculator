package budget;
import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

public class BudgetCalculator extends Application {
	private final TransactionManager _manager 	= new TransactionManager();
	private final Formatting		 _formatter = new Formatting();
	
    public static void main(String[] args) {
        launch(args);
    }
    
	public String getStyleString(Color fg, Color bg) {
		return String.format("-fx-background: rgb(%d, %d, %d);"+
							 "-fx-background-color: -fx-background;"+
							 "-fx-fill: rgb(%d, %d, %d);", 
							 bg.getRed(), bg.getGreen(), bg.getBlue(),
							 fg.getRed(),fg.getGreen(),fg.getBlue());
	}
	
	public String getButtonStyle(Color fg, Color bg) {
		return String.format("-fx-background: rgb(%d, %d, %d);"+
							 "-fx-background-color: -fx-background;"+
							 "-fx-text-fill: rgb(%d, %d, %d);"+
							 "-fx-background-radius: 0, 0;", 
							 bg.getRed(), bg.getGreen(), bg.getBlue(),
							 fg.getRed(),fg.getGreen(),fg.getBlue());
	}
    @Override
    public void start(Stage primaryStage) throws IOException, ParseException {
    	// Set up our manager object first
    	this._manager.load();
    	
    	// Set title of primary stage
        primaryStage.setTitle("Budget Calculator v1.0");
        final VBox 		content = new VBox(5);
        		   		content.setPadding(new Insets(10,10,10,10));
        ScrollPane 		scroller = new ScrollPane(content);
        		   		scroller.setStyle(this.getStyleString(this._formatter.getSystemColor(SystemColor.BLACK), 
        		   											  this._formatter.getSystemColor(SystemColor.WHITE)));
        		   		scroller.setFitToWidth(true);
        
        // Set up table header
        Label dateLabel = new Label (this._manager.getArchivePeriodHeader());
        	  dateLabel.setMaxWidth(Double.MAX_VALUE);
        	  dateLabel.setStyle(this.getStyleString(this._manager.getButtonTextColor(), 
        			  								 this._manager.getButtonBackgroundColor()));
        	  dateLabel.setFont(Font.font("san-serif", FontWeight.BOLD, 20));
        	  dateLabel.setAlignment(Pos.CENTER);
        	  
        Label balanceText = new Label("Total:");
        	  balanceText.setFont(Font.font("san-serif", FontWeight.BOLD, 20));
        Label balanceAmount = new Label("$0.00");
        	  balanceAmount.setFont(Font.font("san-serif", FontWeight.BOLD, 20));
        final BorderPane balancePane = new BorderPane(null,null,balanceAmount,null,balanceText);
        final BorderPane buttonPane = new BorderPane(dateLabel, null,null,null,null);
        final BorderPane contentPane = new BorderPane(scroller,buttonPane,null,balancePane,null);
        				 contentPane.setPadding(new Insets(10,10,10,10));
        			     contentPane.setStyle(this.getStyleString(this._formatter.getSystemColor(SystemColor.BLACK), 
				 					 							  this._manager.getStageBackgroundColor()));
        
        // Set up actions and formatting for add button
        Button addButton = new Button("+");
        	   addButton.setFont(Font.font("san-serif", FontWeight.BOLD, 20));
        	   addButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
					  	  		  					  this._manager.getButtonBackgroundColor()));
        	   addButton.setOnMouseEntered(e -> {
        		   addButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
        			  								  	  this._formatter.lightenColor(
        			  								  			this._manager.getButtonBackgroundColor(), 1
        			  								  	  )));
        	   });
        	   addButton.setOnMouseExited(e -> {
        		   addButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
        				   								  this._manager.getButtonBackgroundColor()));
        	   });
        	   addButton.setOnMouseReleased(e -> {
        		   addButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
						  	  this._formatter.lightenColor(
						  			this._manager.getButtonBackgroundColor(), 1
						  	  )));        		   
        	   });
        	   addButton.setOnMousePressed(e-> {
        		   addButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
							  this._manager.getButtonBackgroundColor()));
        	   });
               addButton.setOnAction(e -> {
                   Dialog<Pair<String, String>> addTransactionDialog = new Dialog<>();
                   addTransactionDialog.setTitle("New Transaction");
                   addTransactionDialog.setHeaderText("Enter the information for your transaction below.");
                   Image dialogGraphic = new Image("file:./assets/add.png");
                   ImageView dialogView = new ImageView(dialogGraphic);
                   			 dialogView.setFitHeight(64);
                   			 dialogView.setFitWidth(64);
                   addTransactionDialog.setGraphic(dialogView);

                    ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
                    addTransactionDialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

                    addTransactionDialog.showAndWait();
            	    /*
       	            final AnchorPane anchorPane = new AnchorPane();
       	            anchorPane.setStyle(this.getStyleString(this._formatter.getSystemColor(SystemColor.BLACK),
       	            										this._manager.getDefaultTransactionColor()
       	            										));
       	            Label label = new Label("Pane "+(content.getChildren().size()+1));
       	            AnchorPane.setLeftAnchor(label, 5.0);
       	            AnchorPane.setTopAnchor(label, 5.0);
       	            Button editButton = new Button("✎");
       	            	   editButton.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
       	            	   editButton.setStyle(this.getButtonStyle(
              	            		this._formatter.detectTextColor(
                   	            			this._manager.getDefaultTransactionColorUndiluted()), 
                   	            			this._manager.getDefaultTransactionColorUndiluted()
                   	            	));
       	            editButton.setOnMouseEntered(ev -> {
       	            	editButton.setStyle(this.getButtonStyle(
       	            		this._formatter.detectTextColor(
       	            			this._manager.getDefaultTransactionColorUndiluted()), 
       	            			this._formatter.lightenColor(
       	            				this._manager.getDefaultTransactionColorUndiluted(), 2)
       	            	));
       	            });
       	            editButton.setOnMouseExited(ev -> {
       	            	editButton.setStyle(this.getButtonStyle(
           	            		this._formatter.detectTextColor(
           	            			this._manager.getDefaultTransactionColorUndiluted()), 
           	            			this._manager.getDefaultTransactionColorUndiluted()));
       	            });
       	            editButton.setOnAction(ev -> {
       	    				Alert alert = new Alert(AlertType.INFORMATION);
       	    				alert.setTitle("Trying to edit an entry?");
       	    				alert.setHeaderText("Sorry, there's nothing here yet");
       	    				alert.setContentText("In the future you can edit stuff with this though.");
       	    				alert.showAndWait();

       	            });
       	            Button removeButton = new Button("✕");
       	            	   removeButton.setFont(Font.font("san-serif", FontWeight.NORMAL, 14));
       	            	   removeButton.setStyle(this.getButtonStyle(
             	            		this._formatter.detectTextColor(
                  	            			this._manager.getDefaultTransactionColorUndiluted()), 
                  	            			this._manager.getDefaultTransactionColorUndiluted()
                  	            	));
       	            	   removeButton.setOnMouseEntered(ev -> {
       	            		   removeButton.setStyle(this.getButtonStyle(
       	            				   this._formatter.detectTextColor(
       	            						   this._manager.getDefaultTransactionColorUndiluted()), 
       	            				   this._formatter.lightenColor(
       	            						   this._manager.getDefaultTransactionColorUndiluted(), 2)
       	            				   ));
       	            	   });
       	            	   removeButton.setOnMouseExited(ev -> {
       	            		   removeButton.setStyle(this.getButtonStyle(
       	            				   this._formatter.detectTextColor(
       	            						   this._manager.getDefaultTransactionColorUndiluted()), 
       	            				   this._manager.getDefaultTransactionColorUndiluted()));
       	            	   });
       	            	   removeButton.setOnAction(ev -> {
       	            		   content.getChildren().remove(anchorPane);
       	            	   });
       	            	   AnchorPane.setRightAnchor(editButton, 45.0);
       	            	   AnchorPane.setTopAnchor(editButton, 5.0);
       	            	   AnchorPane.setBottomAnchor(editButton, 5.0);
       	            	   AnchorPane.setRightAnchor(removeButton, 5.0);
       	            	   AnchorPane.setTopAnchor(removeButton, 5.0);
       	            	   AnchorPane.setBottomAnchor(removeButton, 5.0);
       	            	   anchorPane.getChildren().addAll(label, editButton, removeButton);
       	            	   content.getChildren().add(anchorPane);
       	            	   */
                });
        
        // Format Save button and add event listeners
        Button saveButton = new Button("💾");
        	   saveButton.setFont(Font.font("san-serif", FontWeight.BOLD, 20));
        	   saveButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
		  					  this._manager.getButtonBackgroundColor()));
        	   saveButton.setOnMouseEntered(e -> {
        		   saveButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
        				   this._formatter.lightenColor(
        						   this._manager.getButtonBackgroundColor(), 1)
        			));
        	   });
        	   saveButton.setOnMouseExited(e -> {
        		   saveButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
        				   this._manager.getButtonBackgroundColor())
        		   );
        	   });
        	   saveButton.setOnMousePressed(e-> {
        		   saveButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
        				   this._manager.getButtonBackgroundColor())
        		   );   		   
        	   });
        	   saveButton.setOnMouseReleased(e-> {
        		   saveButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
        				   this._formatter.lightenColor(
        						   this._manager.getButtonBackgroundColor(), 1)
        			));
        	   });
        
        // Format load button and add event listeners
        Button loadButton = new Button("📂");
               loadButton.setFont(Font.font("san-serif", FontWeight.BOLD, 20));
               loadButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
		  					  this._manager.getButtonBackgroundColor()));
               loadButton.setOnMouseEntered(e -> {
            	   loadButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
						  	  this._formatter.lightenColor(
						  			this._manager.getButtonBackgroundColor(), 1
						  	  )));
               });
               loadButton.setOnMouseExited(e -> {
            	   loadButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
							  this._manager.getButtonBackgroundColor()));
               });
               loadButton.setOnMousePressed(e->{
            	   loadButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
							  this._manager.getButtonBackgroundColor()));            	   
               });
               loadButton.setOnMouseReleased(e->{
            	   loadButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
						  	  this._formatter.lightenColor(
						  			this._manager.getButtonBackgroundColor(), 1
						  	  )));            	   
               });
               
        // Format New button and add event listeners
        Button newButton = new Button("🗋");
        	   newButton.setFont(Font.font("san-serif", FontWeight.BOLD, 20));
        	   newButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
		  					  this._manager.getButtonBackgroundColor()));
        	   newButton.setOnMouseEntered(e -> {
        		   newButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
						  	  this._formatter.lightenColor(
						  			this._manager.getButtonBackgroundColor(), 1
						  	  )));
        	   });
        	   newButton.setOnMouseExited(e -> {
        		   newButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
							  this._manager.getButtonBackgroundColor()));
        	   });
        	   newButton.setOnMousePressed(e-> {
        		   newButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
							  this._manager.getButtonBackgroundColor()));        		   
        	   });
        	   newButton.setOnMouseReleased(e-> {
        		   newButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
						  	  this._formatter.lightenColor(
						  			this._manager.getButtonBackgroundColor(), 1
						  	  )));        		   
        	   });
        	   
        // Format Settings button and add event listeners
        Button settingsButton = new Button("🛠");
        	   settingsButton.setFont(Font.font("san-serif", FontWeight.BOLD, 20));
        	   settingsButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
		  					  this._manager.getButtonBackgroundColor()));
        	   settingsButton.setOnMouseEntered(e -> {
        		   settingsButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
						  	  this._formatter.lightenColor(
						  			this._manager.getButtonBackgroundColor(), 1
						  	  )));
        	   });
        	   settingsButton.setOnMouseExited(e -> {
        		   settingsButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
							  this._manager.getButtonBackgroundColor()));
        	   });
        	   settingsButton.setOnMousePressed(e->{
        		   settingsButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
							  this._manager.getButtonBackgroundColor()));        		   
        	   });
        	   settingsButton.setOnMouseReleased(e->{
        		   settingsButton.setStyle(this.getButtonStyle(this._manager.getButtonTextColor(), 
						  	  this._formatter.lightenColor(
						  			this._manager.getButtonBackgroundColor(), 1
						  	  )));        		   
        	   });
        	   
        // Set up menu pane for option buttons     		   
        final AnchorPane optionPane = new AnchorPane();
       		  			 optionPane.setMaxWidth(Double.MAX_VALUE);
       		             optionPane.setStyle(this.getStyleString(this._formatter.getSystemColor(SystemColor.BLACK), 
       		            		 			 this._manager.getStageBackgroundColor()));	   
        // Position menu components in AnchorPane
        AnchorPane.setLeftAnchor	(addButton, 		 10.0);
        AnchorPane.setBottomAnchor	(addButton,	 		 10.0);
        AnchorPane.setTopAnchor		(addButton, 		 10.0);
        AnchorPane.setRightAnchor	(settingsButton, 	 10.0);
        AnchorPane.setBottomAnchor	(settingsButton, 	 10.0);
        AnchorPane.setTopAnchor		(settingsButton, 	 10.0);
        AnchorPane.setRightAnchor	(saveButton, 		 65.0);
        AnchorPane.setBottomAnchor	(saveButton, 		 10.0);
        AnchorPane.setTopAnchor		(saveButton, 		 10.0);
        AnchorPane.setRightAnchor	(loadButton, 		120.0);
        AnchorPane.setBottomAnchor	(loadButton, 		 10.0);
        AnchorPane.setTopAnchor		(loadButton, 		 10.0);
        AnchorPane.setRightAnchor	(newButton, 		175.0);
        AnchorPane.setBottomAnchor	(newButton, 		 10.0);
        AnchorPane.setTopAnchor		(newButton, 		 10.0);
        
        // Add menu components to Anchor pane
        optionPane.getChildren().addAll(addButton, settingsButton, saveButton, loadButton, newButton); 
        
        // Set up our scene and then show
        Scene scene = new Scene(new BorderPane(contentPane, null, null, optionPane, null), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
