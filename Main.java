

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

public class Main extends Application {

    private Stage primaryStage;
    private ArrayList<Customer> customers = new ArrayList<>();

    private final String CUSTOMERS_FILE = "customers.txt";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // 1) Load customers from file
        loadCustomersFromFile();

        // Show the login scene
        showLoginScene();

        primaryStage.setTitle("Bookstore App");
        primaryStage.show();
    }

    @Override
    public void stop() {
        storeCustomersToFile();
    }

    // ------------------ LOAD CUSTOMERS ------------------
    private void loadCustomersFromFile() {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            System.out.println("No existing customers.txt file found. Starting fresh.");
            return; // No file yet, so skip loading
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            customers.clear(); // Clear any existing data
            while ((line = br.readLine()) != null) {
                // Each line: username:password:points
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    int points = Integer.parseInt(parts[2]);

                    Customer c = new Customer(username, password);
                    c.setPoints(points);
                    customers.add(c);
                }
            }
            System.out.println("Customers loaded successfully from " + CUSTOMERS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------ STORE CUSTOMERS ------------------
    private void storeCustomersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer c : customers) {
                // Write line as: username:password:points
                bw.write(c.getUsername() + ":" + c.getPassword() + ":" + c.getPoints());
                bw.newLine();
            }
            System.out.println("Customers saved successfully to " + CUSTOMERS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter for the customers list
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    // ------------------ LOGIN SCENE ------------------
    private void showLoginScene() {
        
        Image bookImage = new Image("file:/Users/artthiramesh/Downloads/imageedit_1_6624353365.png");
        ImageView seeImage = new ImageView(bookImage);
        seeImage.setFitWidth(100);
        seeImage.setPreserveRatio(true);
        
        
        
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));

        Label sceneTitle = new Label("Welcome to the BookStore App");
        sceneTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");


        Label userLabel = new Label("Username:");
        grid.add(userLabel, 0, 1);

        TextField userField = new TextField();
        grid.add(userField, 1, 1);

        Label passLabel = new Label("Password:");
        grid.add(passLabel, 0, 2);

        PasswordField passField = new PasswordField();
        grid.add(passField, 1, 2);

        Button loginBtn = new Button("Login");
        grid.add(loginBtn, 1, 3);

        Label messageLabel = new Label();
        grid.add(messageLabel, 1, 4);

        loginBtn.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            // Check if admin
            if (username.equals("admin") && password.equals("admin")) {
                showOwnerStartScene();
                return;
            }

            // Otherwise, check customers
            for (Customer c : customers) {
                if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                    showCustomerStartScene(c);
                    return;
                }
            }

            // No match
            messageLabel.setText("Invalid credentials. Try again!");
        });
        
        VBox layout = new VBox(20,sceneTitle, seeImage, grid);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        
        
        Scene loginScene = new Scene(layout, 500, 500);
        primaryStage.setScene(loginScene);
        loginScene.setFill(javafx.scene.paint.Color.valueOf("#EED9C4"));
        
        
    }

    // ------------------ OWNER SCENE ------------------
    private void showOwnerStartScene() {
        Label welcomeLabel = new Label("Welcome, Owner!");
        Button customersBtn = new Button("Customers");
        Button logoutBtn = new Button("Logout");
        Button booksButton = new Button("Books");
        
        customersBtn.setOnAction(e -> showManageCustomersScene());
        logoutBtn.setOnAction(e -> showLoginScene());
        booksButton.setOnAction(e -> showManageBooks());

        VBox layout = new VBox(10, welcomeLabel, customersBtn, logoutBtn, booksButton);
        layout.setPadding(new Insets(20));
        primaryStage.setScene(new Scene(layout, 400, 300));
    }

    // ------------------ MANAGE CUSTOMERS SCENE ------------------
    private void showManageCustomersScene() {
    TableView<Customer> table = new TableView<>();
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // Columns
    TableColumn<Customer, String> colUsername = new TableColumn<>("Username");
    colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

    TableColumn<Customer, String> colPassword = new TableColumn<>("Password");
    colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

    TableColumn<Customer, Integer> colPoints = new TableColumn<>("Points");
    colPoints.setCellValueFactory(new PropertyValueFactory<>("points"));

    table.getColumns().addAll(colUsername, colPassword, colPoints);

        ObservableList<Customer> data = FXCollections.observableArrayList(getCustomers());
    table.setItems(data);

    // Input fields
    TextField userField = new TextField();
    userField.setPromptText("Username");
    TextField passField = new TextField();
    passField.setPromptText("Password");

    Button addBtn = new Button("Add");
    addBtn.setOnAction(e -> {
        String u = userField.getText().trim();
        String p = passField.getText().trim();
        if (!u.isEmpty() && !p.isEmpty()) {
            // Create new customer
            Customer c = new Customer(u, p);
            getCustomers().add(c);
            data.add(c);

            userField.clear();
            passField.clear();
        }
    });

    Button deleteBtn = new Button("Delete");
    deleteBtn.setOnAction(e -> {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            getCustomers().remove(selected);
            data.remove(selected);
        }
    });

    Button backBtn = new Button("Back");
    backBtn.setOnAction(e -> showOwnerStartScene());

    // Layout
    VBox topBox = new VBox(table);
    topBox.setPadding(new Insets(10));

    HBox middleBox = new HBox(10, userField, passField, addBtn);
    middleBox.setAlignment(Pos.CENTER_LEFT);
    middleBox.setPadding(new Insets(10));

    HBox bottomBox = new HBox(10, deleteBtn, backBtn);
    bottomBox.setAlignment(Pos.CENTER_LEFT);
    bottomBox.setPadding(new Insets(10));

    VBox root = new VBox(10, topBox, middleBox, bottomBox);
    Scene scene = new Scene(root, 600, 400);
    primaryStage.setScene(scene);
}


    // ------------------ CUSTOMER SCENE ------------------
    private final ObservableList<Books> sharedBooksList = FXCollections.observableArrayList(); // so all the methods can access it 

    private void showCustomerStartScene(Customer c) {
        Label welcomeLabel = new Label("Welcome " + c.getUsername() + ". You have " 
                                       + c.getPoints() + " points.");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> showLoginScene());
        
        Button viewBooksButton = new Button("View Current Books");
            viewBooksButton.setOnAction(e -> avaliableCustomerBooks(c));

        VBox layout = new VBox(10, welcomeLabel, logoutBtn, viewBooksButton);
        layout.setPadding(new Insets(20));
        primaryStage.setScene(new Scene(layout,600 , 400));
    }
   
    private void avaliableCustomerBooks(Customer c){
        
        c.updateStatus(); // updating status
       
        //labels on the screen
        Label welcomeLabel = new Label("Welcome " + c.getUsername()+ "!");
            welcomeLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        
        Label pointsLabel = new Label("Points: " + c.getPoints()); 
            pointsLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        Label statusLabel = new Label("Status: " + c.getStatus()); 
            statusLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        //table to view the books from the owner added,  
        TableView<Books> table = new TableView <> ();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // changes the size if the window is resized 
        
        TableColumn <Books, String> titleColumn = new TableColumn <> ("Title");
            titleColumn.setCellValueFactory(new PropertyValueFactory <> ("title")); //binds it to the property of the book object
        
        //creating same but for author extra function      
        TableColumn <Books, String> authorColumn = new TableColumn <> ("Author");
            authorColumn.setCellValueFactory(new PropertyValueFactory <> ("author")); 
            
        //creating the same for book price
        TableColumn <Books, Double> priceColumn = new TableColumn <> ("Price (CAD)");
            priceColumn.setCellValueFactory(new PropertyValueFactory <> ("price")); 
        
        TableColumn <Books, Boolean> selectedColumn = new TableColumn <> ("Select");
            selectedColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
            
            selectedColumn.setCellFactory(col -> new TableCell<Books, Boolean>(){
                private final CheckBox checkBox = new CheckBox();
                { checkBox.setStyle("-fx-opacity: 1;");
                    checkBox.setOnAction (e -> { // adding a listener to update the books selected thich the check box is clicked/unclicked
                        Books book = getTableView().getItems().get(getIndex()); // books in the row 
                        book.setSelectedBooks(checkBox.isSelected()); //updating the sataus ob books
                        checkBox.setText(checkBox.isSelected()? "x": "");
                    });
                  
                }
            
            @Override 
            protected void updateItem(Boolean selectedBox, boolean empty){
                super.updateItem(selectedBox, empty); // making sure base is functioning as intended
                if (empty || getIndex() >= getTableView().getItems().size()){
                    setGraphic(null);
                }else{
                    Books book = getTableView().getItems().get(getIndex());

                    checkBox.setText(selectedBox ? "x" : "");
                    checkBox.setText(book.isSelectedBooks() ? "x" : "");
                    setGraphic(checkBox);
                }
            }
         });
          
        table.getColumns().addAll(titleColumn, authorColumn, priceColumn, selectedColumn);
            table.setItems(sharedBooksList); // the list that the owner created 

       
        Button logoutButton = new Button("Logout");
            logoutButton.setOnAction(e -> showLoginScene());
        
        Button checkoutButton = new Button("Checkout");
            
            checkoutButton.setOnAction(e ->{ 
               List<Books> selected = sharedBooksList.stream() 
                .filter(Books::isSelectedBooks) // filtering out books not selected 
               .collect(Collectors.toList()); // making a list of the selected books
               
                checkoutScreen(selected, c);
                    
                    
                    });
           
        HBox pointStatusBox = new HBox(10, pointsLabel, statusLabel);
        pointStatusBox.setAlignment(Pos.CENTER_RIGHT);
        
        VBox layout = new VBox(10, welcomeLabel, pointStatusBox, table, logoutButton, checkoutButton);
        layout.setPadding(new Insets(20));
        primaryStage.setScene(new Scene(layout, 800, 400));
    }
          //-//
    //------------------ CHECKOUT SCREEN ------------------
    private void checkoutScreen(List<Books> selectedBooks, Customer customer){
      TableView<Books> checkoutTable = new TableView <> ();
        checkoutTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // creating a column for title of books and linking the title to the property of the book class 
        TableColumn <Books, String> titleColumn = new TableColumn <> ("Title");
            titleColumn.setCellValueFactory(new PropertyValueFactory <> ("title")); 
        
        //creating same but for author extra function      
        TableColumn <Books, String> authorColumn = new TableColumn <> ("Author");
            authorColumn.setCellValueFactory(new PropertyValueFactory <> ("author")); 
            
        //creating the same for book price
        TableColumn <Books, Double> priceColumn = new TableColumn <> ("Price (CAD)");
            priceColumn.setCellValueFactory(new PropertyValueFactory <> ("price")); 
        
       Button backButton = new Button("Back");
        backButton.setOnAction(e -> avaliableCustomerBooks(customer));
        
        checkoutTable.getColumns().addAll(titleColumn, authorColumn, priceColumn);
        checkoutTable.setItems(FXCollections.observableArrayList(selectedBooks));
        
      customer.updateStatus();
      
      
       
        double total = selectedBooks.stream()// total price of the selected books
                                .mapToDouble(Books::getPrice)
                                .sum();
            
        
      
        Label totalLabel = new Label("Total: $ " + total);
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");     
        
        Button buyButton = new Button("Buy");
            buyButton.setOnAction(e -> {
                int earnedPoints = (int)(total * 100);
                customer.setPoints(customer.getPoints() + earnedPoints); // adding earned point to existing points
             
                //alerts with a sucuess note
            showAlert( "Transaction Sucessful","Total $" + total + "\nYou earned " + earnedPoints+ ". Your status is " + customer.getStatus());
            showCustomerStartScene(customer);
            });
        
        
    
        Button redeemButton = new Button("Redeem Points & Buy");
            redeemButton.setOnAction(e -> {
                int points = customer.getPoints(); //current points
                double discount = (points / 100.0); // every 100 points == $1
                double finalPrice = Math.max(0, total - discount); // final price cant be neg
                int usedPoints = (int)(Math.min(discount, total)); // cant use more points than required 
                
                if(usedPoints > points){ // no enoght points == error 
                showAlert("Error", "Not enough points, choose another form of payment");
                }else{
                    
                customer.setPoints(customer.getPoints() - usedPoints);
                showAlert( "Discount Applied!"," Total $" + finalPrice + "\nCurrent points after discount: " + (points - usedPoints) + ". Your status is " + customer.getStatus() );
                showCustomerStartScene(customer);
                }
           });
            
    HBox totalBox = new HBox();
    totalBox.setAlignment(Pos.BOTTOM_RIGHT);
    totalBox.getChildren().add(totalLabel);
    
    HBox backBox = new HBox(10, backButton);
    backBox.setAlignment(Pos.TOP_RIGHT);
        
       VBox layout = new VBox(10,backBox, checkoutTable,  buyButton, redeemButton, totalBox);
        layout.setPadding(new Insets(20));
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.show();
    }
 
   //------------------ OWNER - MANAGE BOOKS  ------------------
    public void showManageBooks(){
        TableView<Books> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // making a table and adjusting the size auto
        
         // creating a column for title of books and linking the title to the property of the book class 
        TableColumn <Books, String> titleColumn = new TableColumn <> ("Title");
            titleColumn.setCellValueFactory(new PropertyValueFactory <> ("title")); 
        
        //creating same but for author extra function      
        TableColumn <Books, String> authorColumn = new TableColumn <> ("Author");
            authorColumn.setCellValueFactory(new PropertyValueFactory <> ("author")); 
            
        //creating the same for book price
        TableColumn <Books, Double> priceColumn = new TableColumn <> ("Price (CAD)");
            priceColumn.setCellValueFactory(new PropertyValueFactory <> ("price")); 
        
       
        table.getColumns().addAll(titleColumn, authorColumn, priceColumn);
        table.setItems(sharedBooksList);  // contains the list of books 
        
        TextField titleSection = new TextField();// letting owner add title of book 
            titleSection.setPromptText("Title"); // sets the promt text to the title category
            
        TextField authorSection = new TextField(); // letting owner add author of books
            authorSection.setPromptText("Author");
            
        TextField priceSection = new TextField(); // letting owner add price of the book
            priceSection.setPromptText("Price");    
       
        Button addButton = new Button("Add");
        addButton.setOnAction (e -> addBooks(titleSection, authorSection, priceSection, sharedBooksList));
        
        
         Button deleteButton = new Button("Delete");
    deleteButton.setOnAction(e -> {
        Books selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            getCustomers().remove(selected);
            sharedBooksList.remove(selected);
        }
    });
    
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showOwnerStartScene());
        
        HBox inputBox = new HBox(10, titleSection, authorSection, priceSection, addButton);
            inputBox.setPadding(new Insets(10));
        
        VBox root = new VBox(10, table, inputBox,deleteButton, backButton);
            root.setPadding(new Insets(10));
        
        Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
         
    }
    
    private void showAlert(String title, String message){ // method to show an error message 
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        
    }
    
    public void addBooks(TextField titleSection,TextField authorSection, TextField priceSection, ObservableList<Books> books){
        String title = titleSection.getText().trim(); // adding the stuff removing the extra spaces
        String priceTe = priceSection.getText().trim();
        String author = authorSection.getText().trim();
        
        //making sure that the inputs are passable, if one is blank then error if all are filled adds 
        if (title.isBlank() || priceTe.isBlank() || author.isBlank() ){
            showAlert("Try again"," Please enter the Title, Author and Price");
            return;
            
        } try{
            double price = Double.parseDouble(priceTe);
            books.add(new Books(title, author, price)); //adding book to the list 
            priceSection.clear();
            authorSection.clear();
            titleSection.clear();
            
        }catch(NumberFormatException e){
            showAlert("Input is Invalid.", "Enter a valid price."); // if its not a number 
        }
        
    }
    
       public static void main(String[] args) {
        launch(args);
    
    }
    
    
    
}