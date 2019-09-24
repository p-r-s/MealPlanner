/**
* Filename:   Main.java
* Project:    Food Query
* Authors:  Pooja Sivakumar, Anna Kim, Stella Kim, Weston Nelson
*  
* Semester:   Fall 2018
* Course:     CS400
* 
*/

package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
 * This class is for running a program that displays a GUI 
 * that shows the main view that the user will see for the 
 * Food Query and Meal Analysis program.
 * 
 * 
 * @author Pooja Sivakumar, Anna Kim, Stella Kim, Weston Nelson 
 */

public class Main extends Application {
	  Scene scene; //The main view scene, so that other scenes can access it to go back to the main view
	 
	 /*
	 * This is a method that creates main page of the program.  It initializes all the GUI
	 * features, including buttons, text fields, and display of relevant data.
	 * 
	 * @param primaryStage
	 * 
	 */
	@Override
	public void start(Stage primaryStage) {
		 
		try {
			
			//The layout of the main view will be a border pane layout
			BorderPane root = new BorderPane();
			root.setPadding(new Insets(0, 150, 20, 200)); 
			
			//Creates the header for the main view
			Text header = new Text("Food Query and Meal Analysis");
			header.setFont(new Font(20));
			HBox text = new HBox();
			text.getChildren().add(header);
			text.setAlignment(Pos.CENTER);
			
			// create a vbox for the food list and meal list, and add padding
			VBox flBox = new VBox();
			VBox mlBox = new VBox();
			flBox.setPadding(new Insets(0, 50, 0, 0));
			mlBox.setPadding(new Insets(0, 0, 0, 50));

			// create the labels and counters for food list and meal list, and add padding
			Label flLabel = new Label("Food List");
			flLabel.setPadding(new Insets(0, 30, 0, 0));
			Label fCounter = new Label();
			fCounter.setPadding(new Insets(5, 0, 0, 30));
			Label mCounter = new Label();
			mCounter.setPadding(new Insets(5, 0, 0, 30));
			Label mlLabel = new Label("Meal List");
			mlLabel.setPadding(new Insets(0, 30, 0, 0));
			flLabel.setFont(new Font(20));
			mlLabel.setFont(new Font(20));
			
			// Make h box for Title + counter
			HBox fTitleCounter = new HBox();
			fTitleCounter.getChildren().addAll(flLabel, fCounter);
			HBox mTitleCounter = new HBox();
			mTitleCounter.getChildren().addAll(mlLabel, mCounter);

			// create the list view of the food list and the meal list
			ListView<String> foodList = new ListView<String>();
			foodList.setPrefWidth(300);
			foodList.setPrefHeight(170);
			ListView<String> mealList = new ListView<String>();
			mealList.setPrefWidth(300);
			mealList.setPrefHeight(170);
		
			// put the labels and lists in vbox
			flBox.getChildren().addAll(fTitleCounter, foodList);
			mlBox.getChildren().addAll(mTitleCounter, mealList);

			// put these into an hbox
			HBox boxList = new HBox();
			boxList.getChildren().addAll(flBox, mlBox);
			boxList.setAlignment(Pos.CENTER);
			boxList.setPadding(new Insets(50, 0, 50, 0));
			root.setTop(text); //the top of the layout will contain the header
			
			FoodData foodListFile = new FoodData(); //Instance of food data for the food list.
			ArrayList<FoodItem> mealListFile = new ArrayList<FoodItem>(); //Instance of food data for the meal list
			
			//This Observable List of Strings is the list that the foodList and mealList (ListView) will display	
			ObservableList<String> fl = FXCollections.observableArrayList();
			ObservableList<String> ml = FXCollections.observableArrayList();
			
			//The heirarchy of the food list is:
			// foodList (ListView) displays the NAMES OF THE FOOD ITEMS stored in foodListFile (FoodData), using an observable array of
			// Strings called fl
			
			// Make the buttons for the Food list
			// Button to add selected food items to the meal list
			Button addMeal = new Button("+ To Meal List");
			addMeal.setOnAction(e -> {
				if (foodList.getSelectionModel().getSelectedItem() != null) {
					ml.add(foodList.getSelectionModel().getSelectedItem());
					List<FoodItem> itemsTemp = foodListFile.getAllFoodItems();
					for(FoodItem item : itemsTemp) {
						if (item.getName().equals(foodList.getSelectionModel().getSelectedItem())) {
							mealListFile.add(item);
							break;
						}
					}
					mealList.setItems(ml);
					mCounter.setText("COUNT: " + ml.size());
				}
			});
			
			//Creates the clear meal button and add the functionality to clear the list of visible foods in the current meal list
			Button clearMeal = new Button("Clear Meal");
			clearMeal.setOnAction(e -> {
				ml.clear();
				mealList.setItems(ml);
				mealListFile.clear();
				mCounter.setText("COUNT: " + ml.size());
			});
			
			//Button to add a new food item to the food list 
			Button addFood = new Button("+ New Food");
			
			//The GUI of the Add new food item scene
			VBox dialog = new VBox(); //Create VBox for the overall layout
			VBox headingBox = new VBox(); //Creat vbox to hold the heading
			Label headingAdd = new Label("Enter the details of the food to be added");
			headingAdd.setFont(new Font(20));
			headingBox.getChildren().add(headingAdd);
			headingAdd.setPadding(new Insets(20, 0, 20, 350));
			VBox labelsBox = new VBox(); //create vbox to hold all the nutrients' names
			Label idAdd = new Label ("ID: "); //id label
			idAdd.setFont(new Font(15));
			Label nameAdd = new Label ("Name: "); //name label
			nameAdd.setFont(new Font(15));
			Label calsAdd = new Label("Calories (KCal): "); //calories label
			calsAdd.setFont(new Font(15));
			Label fatAdd = new Label ("Fat (g): "); //fat label
			fatAdd.setFont(new Font(15));
			Label carbAdd = new Label ("Carbohydrates (g): "); //carbs label
			carbAdd.setFont(new Font(15));
			Label fiberAdd = new Label("Fiber (g): "); //fibre label
			fiberAdd.setFont(new Font(15));
			Label proteinAdd = new Label ("Protein (g): "); //protein label
			proteinAdd.setFont(new Font(15));
			labelsBox.getChildren().addAll(idAdd, nameAdd, calsAdd, fatAdd, carbAdd, fiberAdd, proteinAdd);
			labelsBox.setSpacing(35);
			labelsBox.setAlignment(Pos.CENTER_LEFT);
			VBox textFieldsBox = new VBox(); //create vbox to hold the input fields for the nutrients
			TextField idFieldAdd = new TextField(); //id input
			TextField nameFieldAdd = new TextField(); //name input
			TextField calFieldAdd = new TextField(); //calories input
			TextField fatFieldAdd = new TextField(); //fat input
			TextField carbFieldAdd = new TextField(); //carbs input
			TextField fiberFieldAdd = new TextField(); //fibre input
			TextField proteinFieldAdd = new TextField(); //protein input
			textFieldsBox.getChildren().addAll(idFieldAdd, nameFieldAdd, calFieldAdd, fatFieldAdd, carbFieldAdd, fiberFieldAdd, proteinFieldAdd);
			textFieldsBox.setSpacing(30);
			HBox inputs = new HBox(); //create an hbox to hold the nutrients' names and input boxes
			inputs.getChildren().addAll(labelsBox, textFieldsBox);
			inputs.setAlignment(Pos.CENTER);
			Button submitBtn = new Button("Submit"); //Create button to save new food to the food list
			submitBtn.setOnAction(event->{
				Boolean flag = true;
				//Retrieve all inputted values
				String idNew = idFieldAdd.getText();
				if (idNew == null || idFieldAdd.getText().trim().isEmpty() || idNew.contains(" ")) {
					flag = false;
					idFieldAdd.setText("Enter an ID!");
				}
				String nameNew = nameFieldAdd.getText();
				if (nameNew == null || nameFieldAdd.getText().trim().isEmpty() || nameNew.contains(" ")) {
					flag = false;
					nameFieldAdd.setText("Enter a name!");
				}
				if (flag == true) {
					Boolean flag2 = true;
				//Create new instance of FoodItem and initialize all values
				FoodItem itemNew = new FoodItem(idNew, nameNew);
				if (calFieldAdd.getText() == null || calFieldAdd.getText().trim().isEmpty()) {
					calFieldAdd.setText("Enter a valid input!");
					flag2 = false;
				}
				
				try {
					Double cals = Double.parseDouble(calFieldAdd.getText());
					if (cals < 0 )  {
						calFieldAdd.setText("Enter valid input!");
						flag2 = false;
					}
				}catch (Exception e) {
					calFieldAdd.setText("Enter valid input!");
					flag2 = false;
				}
				
				if (fatFieldAdd.getText() == null || fatFieldAdd.getText().trim().isEmpty()) {
					fatFieldAdd.setText("Enter a valid input!");
					flag2 = false;
				}
				
				try {
					Double fats = Double.parseDouble(fatFieldAdd.getText());
					if ( fats < 0 )  {
						fatFieldAdd.setText("Enter valid input!");
						flag2 = false;
					}
				}catch (Exception e) {
					fatFieldAdd.setText("Enter valid input!");
					flag2 = false;
				}
				
				if (carbFieldAdd.getText() == null || carbFieldAdd.getText().trim().isEmpty()) {
					carbFieldAdd.setText("Enter a valid input!");
					flag2 = false;
				}
				
				try {
					Double carbs = Double.parseDouble(carbFieldAdd.getText());
					if (carbs < 0 )  {
						carbFieldAdd.setText("Enter valid input!");
						flag2 = false;
					}
				}catch (Exception e) {
					carbFieldAdd.setText("Enter valid input!");
					flag2 = false;
				}
				
				if (fiberFieldAdd.getText() == null || fiberFieldAdd.getText().trim().isEmpty()) {
					fiberFieldAdd.setText("Enter a valid input!");
					flag2 = false;
				}
				
				try {
					Double fibers = Double.parseDouble(fiberFieldAdd.getText());
					if (fibers < 0 )  {
						fiberFieldAdd.setText("Enter valid input!");
						flag2 = false;
					}
				}catch (Exception e) {
					fiberFieldAdd.setText("Enter valid input!");
					flag2 = false;
				}
				
				if (proteinFieldAdd.getText() == null || proteinFieldAdd.getText().trim().isEmpty()) {
					proteinFieldAdd.setText("Enter a valid input!");
					flag2 = false;
				}
				
				try {
					Double proteins = Double.parseDouble(proteinFieldAdd.getText());
					if (proteins < 0 )  {
						proteinFieldAdd.setText("Enter valid input!");
						flag2 = false;
					}
				}catch (Exception e) {
					proteinFieldAdd.setText("Enter valid input!");
					flag2 = false;
				}
				if (flag2 == true) {
				itemNew.addNutrient("calories", Double.parseDouble(calFieldAdd.getText()));
				itemNew.addNutrient("fat", Double.parseDouble(fatFieldAdd.getText()));
				itemNew.addNutrient("carbohydrate", Double.parseDouble(carbFieldAdd.getText()));
				itemNew.addNutrient("fiber", Double.parseDouble(fiberFieldAdd.getText()));
				itemNew.addNutrient("protein", Double.parseDouble(proteinFieldAdd.getText()));
				//Add the new item to the copy of the food list with all details
				foodListFile.addFoodItem(itemNew);
				//add the name of the food item to the Observable Array
				fl.add(itemNew.getName());
				//display the updated list of food names in the ListView
				foodList.setItems(fl.sorted());
				idFieldAdd.clear();
				nameFieldAdd.clear();
				calFieldAdd.clear();
				fatFieldAdd.clear();
				carbFieldAdd.clear();
				fiberFieldAdd.clear();
				proteinFieldAdd.clear();
				//Go back to the main view
				fCounter.setText("COUNT: " + fl.size());
				primaryStage.setScene(scene);
				}
				} 
			});
			
			Button exitAnalAdd = new Button("Go Back"); //button to go back to home page
			exitAnalAdd.setOnAction(exit -> primaryStage.setScene(scene));
				
			HBox forSubmit = new HBox(); //create vbox to hold the submit button
			forSubmit.getChildren().addAll(submitBtn,exitAnalAdd);
			
			
			forSubmit.setPadding(new Insets(50, 0, 20, 500));
			//add all nodes to the overall layout
			dialog.getChildren().addAll(headingAdd, inputs, forSubmit);
			Scene scene3 = new Scene(dialog, 1000, 640);
			addFood.setOnAction(event -> primaryStage.setScene(scene3));
			
			//To load a new food list and display it in the list view
			Button addFoodFile = new Button("+ Load Food List");
			addFoodFile.setOnAction(event -> {
				FileChooser choose = new FileChooser(); //Create a file selection dialog
				choose.setTitle("Load new food list");
				File file = choose.showOpenDialog(primaryStage);
				if (file != null) {
					int index = file.getAbsolutePath().length();
					if (file.getAbsolutePath().substring(index - 4, index).equals(".csv")) {
				foodListFile.loadFoodItems(file.getAbsolutePath()); //Load the new food list into the food data object with all details of the foods
				//clear the observable array of food names and add the new names to it
				fl.clear();
				for(int i = 0; i < foodListFile.getAllFoodItems().size(); i++) {
					fl.add(foodListFile.getAllFoodItems().get(i).getName());
					}
				
				//display the new food items' names in the List View
				foodList.setItems(fl.sorted());
				fCounter.setText("COUNT: " + fl.size());
			}
			else
				{
					Alert error = new Alert(AlertType.ERROR, "Select a valid .csv file!", ButtonType.CLOSE);
					error.show();
				}
			} });
			
			//To save the food items in the current food list to a file on the user's computer
			Button saveFood = new Button("Save Food List");
			saveFood.setOnAction(event -> {
			TextInputDialog dialogSave = new TextInputDialog(); //Clicking on the save food list button will open a text input dialog
			dialogSave.setTitle("Save Food List");
			dialogSave.setHeaderText(null);
			dialogSave.setContentText("Enter the name of the file to save to: ");
			Optional<String> fileName = dialogSave.showAndWait();
			if (fileName.isPresent()) {
			String saveFile = fileName.get(); //get the user's input
				foodListFile.saveFoodItems(saveFile + ".csv"); //Save the food items as a .csv file
				//primaryStage.setScene(scene); //go back to the main view
			}
			});

			// Make the buttons for the Meal List
			//to remove a selected food item from the meal list
			Button removeFood = new Button("- Remove Food");
			removeFood.setOnAction(e -> {
				if (mealList.getSelectionModel().getSelectedItem() != null) {
					for (FoodItem item: mealListFile) {
						if (item.getName().equals(mealList.getSelectionModel().getSelectedItem())) {
							mealListFile.remove(item);
							break;
						}
					}
					ml.remove(mealList.getSelectionModel().getSelectedIndex());
					mealList.setItems(ml);
					mCounter.setText("COUNT: " + ml.size());
				}
			});
			
			//to analyze the meal list and come up with a summary of the meal
			Button analyze = new Button("Analyze");
			Alert noMeal = new Alert(AlertType.ERROR, "No food in current meal.  Add food to display meal analysis.", ButtonType.CLOSE);	//Alert message for attempt to analyze empty meal list

			analyze.setOnAction(
				handle -> {
					
					if (mealListFile.size() == 0) {
						noMeal.show();
						return;
					}
					
					//The GUI for when the meal summary is displayed
					Label header2 = new Label("Meal Summary");
					header2.setFont(new Font(20));
					Label subHead = new Label("Below is an analysis of the nutrients in your meal and the factor by which each nutrient comprises your meal");
					subHead.setFont(new Font(15));
					//create hbox for heading
					HBox heading = new HBox();
					heading.getChildren().add(header2);
					heading.setAlignment(Pos.CENTER);
					//create hbox for subheading
					HBox subheading = new HBox();
					subheading.getChildren().add(subHead);
					subheading.setAlignment(Pos.CENTER);
					//declare variables for the sum of carbs, calories, protein, fat, and fibre content of the meal
					double sumCarb = 0;
					double sumProtein = 0;
					double sumFiber = 0;
					double sumCalories = 0;
					double sumFat = 0;
					double total = 0; //this is the total amount of nutrients in the meal
					for(FoodItem item: mealListFile) {
						sumCarb+=item.getNutrientValue("carbohydrate");
						sumProtein+=item.getNutrientValue("protein");
						sumFiber+=item.getNutrientValue("fiber");
						sumCalories+=item.getNutrientValue("calories");
						sumFat+=item.getNutrientValue("fat");
					}
					total+=sumCarb + sumProtein + sumFiber + sumCalories + sumFat;

					//make some formatted strings to store the percents of the meal that each nutrient comprises
					String carbPerc = String.format("%.2f", sumCarb/total*100);
					String proPerc = String.format("%.2f", sumProtein/total*100);
					String fibPerc = String.format("%.2f", sumFiber/total*100);
					String ftPerc = String.format("%.2f", sumFat/total*100);
					//Create labels for each nutrient
					Label carbs = new Label("Carbohydrates: " + sumCarb + "g" + " -- " + carbPerc + "%");
					carbs.setFont(new Font(17));
					carbs.setPadding(new Insets(50, 0, 0, 0));
					Label cal = new Label("Calories: " + sumCalories + "KCal");
					cal.setFont(new Font(17));
					cal.setPadding(new Insets(50, 0, 0, 0));
					Label pro = new Label ("Proteins: " + sumProtein + "g"+ " -- " + proPerc + "%" );
					pro.setFont(new Font(17));
					pro.setPadding(new Insets(50, 0, 0, 0));
					Label fib = new Label("Fiber: " + sumFiber + "g"+ " -- " + fibPerc + "%");
					fib.setFont(new Font(17));
					fib.setPadding(new Insets(50, 0, 0, 0));
					Label ft = new Label("Fat: " + sumFat + "g"+ " -- " + ftPerc + "%");
					ft.setFont(new Font(17));
					ft.setPadding(new Insets(50, 0, 0, 0));
					//Create VBox to store the nutrients together
					VBox nutrAnal = new VBox();
					nutrAnal.setAlignment(Pos.CENTER);
					nutrAnal.setPadding(new Insets(20, 0, 50, 0));
					nutrAnal.getChildren().addAll(cal, ft,carbs, fib, pro);
					Button exitAnal = new Button("Go Back"); //button to go back to home page
					exitAnal.setOnAction(exit -> primaryStage.setScene(scene));
					//make box to hold button
					VBox exitBox = new VBox();
					exitBox.getChildren().add(exitAnal);
					//Create Vbox to store heading + subheading + nutrient summary
					VBox analyzeLayout = new VBox();
					analyzeLayout.getChildren().addAll(heading, subheading, nutrAnal, exitAnal);
					analyzeLayout.setAlignment(Pos.TOP_CENTER);
					Scene scene2 = new Scene(analyzeLayout, 1000, 640);	
				primaryStage.setScene(scene2);
				});
			
			// Make two HBoxs for the Food/Meal list buttons. Add them to the Food/Meal list
			// boxes
			HBox FButtons = new HBox();
			HBox MButtons = new HBox();
			
			// HBox for the food saving button and the loading food file button
			HBox foodSaves = new HBox(addFoodFile, saveFood);

			FButtons.getChildren().addAll(addFood, addMeal);
			FButtons.setSpacing(10);
			MButtons.getChildren().addAll(removeFood, analyze, clearMeal);
			MButtons.setSpacing(10);
			flBox.getChildren().addAll(FButtons, foodSaves);
			flBox.setSpacing(3);
			mlBox.getChildren().add(MButtons);
			mlBox.setSpacing(3);
			
			root.setCenter(boxList); //the center of the layout will contain the lists and buttons

			
			Button help = new Button("Help");
			help.setOnAction(event -> {
			Label headingHelp = new Label ("How to use this app");
			headingHelp.setFont(new Font(20));
			Text textHelp = new Text();
			textHelp.setFont(new Font(13));
			textHelp.setWrappingWidth(850);
			textHelp.setText("How to load a food file: \n"
					+ "Click the \"+ Load Food List\" button and select a valid .csv or .txt file.\n\nHow to add a new food item to Food List: \n"
					+ "Click the \"+ New Food \" button and enter:\n"
					+ "1. The ID of the food item\n"
					+ "2. The Name of the food item\n"
					+ "3. The various nutritional values of the food item\n"
					+ "And click Submit.\n\n"
					+ "How to add to the Meal List: \n"
					+ "Select the food item you want to add to the Meal List from the Food List, and click the \"+ To Meal List\" button.\n"
					+ "You can add the same food item to the meal list as many times as you want.\n\n"
					+ "How to save a food list: \n"
					+ "You can save the current food list as a .csv file onto your computer (including items that were filtered out) "
					+ "by clicking the \"Save Food List\" button\n\n"
					+ "How to filter foods in the Food List: \n"
					+ "You can filter the displayed items in the Food List by entering the phrase you want the food items "
					+ "to match and the nutritonal values of the food items you want to see.\n"
					+ "To display foods with a particular value of a nutrient (e.g, only items with 1000 calories), "
					+ "enter the the particular value into both bounds.\n You can clear the filters by clicking \"Reset Filters\".\n\n"
					+ "How to Remove Food from Meal List: \n"
					+ "You can remove an item from the meal list by selecting it and clicking the \"- Remove Food\" button.\n\n"
					+ "How to generate a Meal Summary\n"
					+ "You can analyze the meal you've made by clicking the \"Analyze\" button.\n\n");
			Button goBack = new Button ("Got it! Take me back!");
			goBack.setOnAction(action -> primaryStage.setScene(scene));
			VBox texts = new VBox();
			texts.getChildren().addAll(headingHelp, textHelp, goBack);
			texts.setAlignment(Pos.CENTER);
			Scene scene4 = new Scene(texts, 1000, 640);
			primaryStage.setScene(scene4);
			});
			
			root.setRight(help);

			// box including name text field
			VBox filterBoxUp = new VBox();
			// set the margin for the filter box up
			filterBoxUp.setPadding(new Insets(0, 50, 0, 0));

			// title
			Text fTitle = new Text("Filter Food List By:");
			fTitle.setFont(new Font(15));
			HBox text2 = new HBox();
			text2.setPadding(new Insets(10, 0, 15, 0));
			text2.getChildren().add(fTitle); // put fTitle to text2
			text2.setAlignment(Pos.CENTER); // put text2 to center

			// the name section of the filters
			Label name = new Label("Name:");
			TextField nameField = new TextField();
			nameField.setPrefWidth(250); // set the width of the TextField to 250

			HBox nameBox = new HBox();
			nameBox.getChildren().addAll(name, nameField); // put name and nameField into nameBox
			nameBox.setSpacing(10);
			nameBox.setAlignment(Pos.CENTER);

			filterBoxUp.getChildren().add(nameBox); // put nameBox into filterBoxUp

			// the nutrient section of the filters
			VBox filterBoxDw = new VBox();

			// for Calories, Carbohydrates, Fat, Protein, and Fiber,
			// set the label as a nutrient's name
			// create a text field for minimum and maximum value of a nutrient
			// so user can enter to filter food
			// and add all the elements into HBox for each nutrient

			// Calories
			Label calories = new Label("Calories:	 	");
			TextField minField1 = new TextField();
			Label to1 = new Label(" to ");
			TextField maxField1 = new TextField();
			minField1.setPrefWidth(40);
			maxField1.setPrefWidth(40);
			HBox caloriesBox = new HBox();
			caloriesBox.getChildren().addAll(calories, minField1, to1, maxField1);
			caloriesBox.setSpacing(10);

			// carbohydrates
			Label carbohydrates = new Label("Carbohydrates:");
			TextField minField2 = new TextField();
			Label to2 = new Label(" to ");
			TextField maxField2 = new TextField();
			minField2.setPrefWidth(40);
			maxField2.setPrefWidth(40);
			HBox carbohydratesBox = new HBox();
			carbohydratesBox.getChildren().addAll(carbohydrates, minField2, to2, maxField2);
			carbohydratesBox.setSpacing(10);

			// fat
			Label fat = new Label("Fat:			");
			TextField minField3 = new TextField();
			Label to3 = new Label(" to ");
			TextField maxField3 = new TextField();
			minField3.setPrefWidth(40);
			maxField3.setPrefWidth(40);
			HBox fatBox = new HBox();
			fatBox.getChildren().addAll(fat, minField3, to3, maxField3);
			fatBox.setSpacing(10);

			// protein
			Label protein = new Label("Protein:		");
			TextField minField4 = new TextField();
			Label to4 = new Label(" to ");
			TextField maxField4 = new TextField();
			minField4.setPrefWidth(40);
			maxField4.setPrefWidth(40);
			HBox proteinBox = new HBox();
			proteinBox.getChildren().addAll(protein, minField4, to4, maxField4);
			proteinBox.setSpacing(10);

			// fiber
			Label fiber = new Label("Fiber:		");
			TextField minField5 = new TextField();
			Label to5 = new Label(" to ");
			TextField maxField5 = new TextField();
			minField5.setPrefWidth(40);
			maxField5.setPrefWidth(40);
			HBox fiberBox = new HBox();
			fiberBox.getChildren().addAll(fiber, minField5, to5, maxField5);
			fiberBox.setSpacing(10);
			filterBoxDw.getChildren().addAll(caloriesBox, fatBox, carbohydratesBox, fiberBox, proteinBox);
			
			TextField[] filterFields = new TextField[]{minField1, maxField1, minField2, maxField2, minField3, maxField3, minField4, maxField4, minField5, maxField5};

			// nutrient label
			// Label nutrient = new Label("Nutrient:		");

			// combine nutrient label with the types of nutrient including
			// Calories, Carbohydrates, Fat, Protein, and Fiber
			HBox whole = new HBox();
			whole.getChildren().addAll(filterBoxDw);
			whole.setSpacing(10);
			whole.setAlignment(Pos.CENTER);
			

			// combine title, name box, and whole box vertically
			VBox filterBoxTotal = new VBox();
			filterBoxTotal.getChildren().addAll(text2, filterBoxUp, whole);

			// create Apply Filter button
			Button filterButton = new Button("Apply Filter");
			
			Alert alertNum = new Alert(AlertType.ERROR, "Invalid Entry: negative value[s]", ButtonType.CLOSE);	//Alert for negative number entry
			Alert alertLet = new Alert(AlertType.ERROR, "Invalid Entry: non-numeric value[s]", ButtonType.CLOSE);	//Alert for non-number entry
			
			// filter button functionality is implemented 
			filterButton.setOnAction(event -> {
				List<String> rules = new ArrayList<String>();
				
				//Checks if the user entered a non-numeric value
				for (TextField fields: filterFields) {
					try {
						if (!fields.getText().isEmpty()) {
							Double.parseDouble(fields.getText());
						}
					} catch (Exception e) {
						alertLet.show();
						return;
					}
				}
				
				//Checks if the user entered a negative value
				for (TextField fields: filterFields) {
					if (fields.getText().contains("-")) {
						alertNum.show();
						return;
					}
				}
				
				// Filter for calories (adds to list for later parsing)
				if (!minField1.getText().isEmpty()) {
				rules.add("calories" + " " + minField1.getText() + " " + ">=");
				}
				if (!maxField1.getText().isEmpty()) {
					
				rules.add("calories" + " " + maxField1.getText() + " " + "<=");
				}
				
				// Filter for carbohydrates (adds to list for later parsing)
				if (!minField2.getText().isEmpty()) {
				rules.add("carbohydrate" + " " + minField2.getText() + " " + ">=");
				}
				
				if (!maxField2.getText().isEmpty()) {
					
				rules.add("carbohydrate" + " " + maxField2.getText() + " " + "<=");
				}
				
				// Filter for fat (adds to list for later parsing)
				if (!minField3.getText().isEmpty()) {
					rules.add("fat" + " " + minField3.getText() + " " + ">=");
				}
				
				if (!maxField3.getText().isEmpty()) {
					
					rules.add("fat" + " " + maxField3.getText() + " " + "<=");
				}
				
				// Filter for protein (adds to list for later parsing)
				if (!minField4.getText().isEmpty()) {
					rules.add("protein" + " " + minField4.getText() + " " + ">=");
				}
				
				if (!maxField4.getText().isEmpty()) {
					rules.add("protein" + " " + maxField4.getText() + " " + "<="); 
				}
				
				// Filter for fiber (adds to list for later parsing)
				if (!minField5.getText().isEmpty()) {
					rules.add("fiber" + " " + minField5.getText() + " " + ">=");
				}
				if (!maxField5.getText().isEmpty()) {
					rules.add("fiber" + " " + maxField5.getText() + " " + "<=");
				}
					
				// Removes filters that are null (not requested by user)
				for(String ruleText : rules) {
					if (ruleText == null) {
						rules.remove(ruleText);
					}
				}
				
				if (rules.size() > 0) {
				// Applies the filter to the visible food list
				List<FoodItem>filteredList = new ArrayList<FoodItem>();
				filteredList = foodListFile.filterByNutrients(rules);
				fl.clear();
				for(int i = 0; i < filteredList.size(); i++) {
					fl.add(filteredList.get(i).getName());
				}
				String nameRule = nameField.getText();
				List<FoodItem>filteredListName = foodListFile.filterByName(nameRule);
				List<String>namesFiltered = new ArrayList<String>();
				
				for(FoodItem item : filteredListName) {
					namesFiltered.add(item.getName());
				}
				
				for(int i = 0; i < fl.size(); i++) {
					if (!namesFiltered.contains(fl.get(i))) {
					fl.remove(fl.get(i));
					i--;
					}
				}
				foodList.setItems(fl.sorted());
				fCounter.setText("COUNT: " + fl.size());
				}
			});
			
			// Resets the current filters and shows the entire food list
			Button reset = new Button("Remove Filters");
			reset.setOnAction(event -> {
				fl.clear();
				nameField.clear();
				
				minField1.clear();	//Clears all the filter text fields for the user
				maxField1.clear();
				minField2.clear();
				maxField2.clear();
				minField3.clear();
				maxField3.clear();
				minField4.clear();
				maxField4.clear();
				minField5.clear();
				maxField5.clear();
				
				List<FoodItem> foodListTemp = foodListFile.getAllFoodItems();
				for(FoodItem item : foodListTemp) {
					fl.add(item.getName());
				}
				foodList.setItems(fl.sorted());
				fCounter.setText("COUNT: " + fl.size());
			});
			
			// Places filter buttons in HBox to be added to the scene
			HBox filterButtonBox = new HBox();
			filterButtonBox.getChildren().addAll(filterButton, reset);
			filterButtonBox.setAlignment(Pos.CENTER); // position at the center

			// combine filter button and filter box total box
			VBox bottom = new VBox();
			bottom.getChildren().addAll(filterBoxTotal, filterButtonBox);
			

			// place at the bottom
			root.setBottom(bottom);
			
			scene = new Scene(root, 1000, 640);	// First scene is initialized
			 	
			// Format and sets the current scene on the primary stage
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is the main method for the Food Query and Meal Analysis program. It
	 * creates the window that contains the main page of the program.
	 *
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		launch(args);
	}

}