package application;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.Instant;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class TransactionController {
    private TableView<Transaction> transactionTable;
    private TextField priceField;
    private TextField amountField;
    private TextField StratsCodeField;

    private Stage primaryStage;
    private Scene loginScene;
    private Scene mainScene;
    private Scene AddCCYScene;
    private Scene AddStratsCodeScene;
    private final DatabaseManager databaseManager;
    
    //Public Object Declaration
    ChoiceBox<String> CcyChoicebox = new ChoiceBox<>();
    ChoiceBox<String> StratscodeChoicebox = new ChoiceBox<>();
    LineChartAnalyse lca = new LineChartAnalyse();
    ScatterChartAnalyse sca = new ScatterChartAnalyse();
    SDChartAnalyse SDca = new SDChartAnalyse();
    LinearRegressionAnalyse lra = new LinearRegressionAnalyse();
    DatabaseManager dbManager = new DatabaseManager();
    List<Transaction> transactionData = dbManager.getTransactionData();
    
    public TransactionController() {
        this.databaseManager = new DatabaseManager();
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
       
        
        
        
        primaryStage.setTitle("Transaction Program");
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        VBox loginBox = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        loginBox.setPadding(new Insets(10));

        loginButton.setOnAction(event -> {
            if (validateLogin(usernameField.getText(), passwordField.getText())) {
                showMainScene();
            } else {
                showAlert("Invalid credentials", "Please enter valid username and password.");
            }
        });

        loginScene = new Scene(loginBox, 400, 200);
        
        loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        //initialize the item in CCY + Stratscode choice box
        CcyChoicebox.getItems().addAll(FileManager.readCryptoTypes());
        StratscodeChoicebox.getItems().addAll(FileManager.readStratscodeTypes());
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    
    
    //Preset passwords, for demonstration purposes, equals to empty
    private boolean validateLogin(String username, String password) {
        if (username.equals("") && password.equals("")){
        	return true;
        }else if (username.equals("admin") && password.equals("admin")) {
        	return true;
        }
		return false;
    }
   
    //alert method
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        
    }

    private void showMainScene() {
    	
        Label cryptocurrencyLabel = new Label("Cryptocurrency:");
        Button AddCcyButton = new Button("Add a New Type of CCY");
        Label Addccy = new Label("Add a new cryptocurrency type:");
        TextField Addccytextfield = new TextField();
        Button AddCcyButton2 = new Button("Add a New Type of CCY");
        Button AddStratscodeButton = new Button("Add a New Type of Stratscode");
        Label AddStratscode = new Label("Add a new Stratscode: ");
        TextField AddStratscodefield = new TextField();
        Button AddStratscode2 = new Button("Add a New Type of Stratscode");
        
        //Choice box for ccy
        VBox AddCCYBox = new VBox(10, Addccy,Addccytextfield,AddCcyButton2);
        AddCCYBox.setPadding(new Insets(10));
        AddCCYScene = new Scene(AddCCYBox, 350, 150);
        AddCCYScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        AddCcyButton.setOnAction(event ->  primaryStage.setScene(AddCCYScene));
        primaryStage.show();
        AddCcyButton2.setOnAction(event ->  {
        	addCCYtype(Addccytextfield.getText());
        	Addccytextfield.clear();
        });
        
        //Choice box for stratscode
        VBox AddStratscodeBox = new VBox(10, AddStratscode,AddStratscodefield,AddStratscode2);
        AddStratscodeBox.setPadding(new Insets(10));
        AddStratsCodeScene = new Scene(AddStratscodeBox, 350, 150);
        AddStratsCodeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        AddStratscodeButton.setOnAction(event ->  primaryStage.setScene(AddStratsCodeScene));
        primaryStage.show();
        AddStratscode2.setOnAction(event ->  {
        	addStratscodetype(AddStratscodefield.getText());
        	AddStratscodefield.clear();
        });
        
        //GUI
        Label priceLabel = new Label("Price:");
        priceField = new TextField();
        Label amountLabel = new Label("Amount:");
        amountField = new TextField();
        Label StratsCodeLabel = new Label("Strats Code:");
        StratsCodeField = new TextField();
        Button addButton = new Button("Add Transaction");
        Button importButton = new Button("Import");
        VBox importBox = new VBox(10, importButton);
        importBox.setPadding(new Insets(10));

        //GUI
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(cryptocurrencyLabel, 0, 0);
        grid.add(CcyChoicebox, 1, 0);
        grid.add(AddCcyButton, 2, 0);
        grid.add(priceLabel, 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(amountLabel, 0, 2);
        grid.add(amountField, 1, 2);
        grid.add(StratsCodeLabel, 0, 3);
        grid.add(StratscodeChoicebox, 1, 3);
        grid.add(AddStratscodeButton, 2, 3);
        grid.add(addButton, 1, 4);
        HBox ImportButton = new HBox(10);
        ImportButton.setAlignment(Pos.BOTTOM_LEFT);
        ImportButton.getChildren().add(importButton);
        grid.add(ImportButton, 1, 5);
        
        //Import and Add transaction button
        importButton.setOnAction(event -> importTransactionsFromCsv());
        addButton.setOnAction(event -> addTransaction(CcyChoicebox));
        
        //Columns to add to the Table view
        TableColumn<Transaction, String> cryptocurrencyColumn = new TableColumn<>("Cryptocurrency");
        cryptocurrencyColumn.setPrefWidth(150);
        TableColumn<Transaction, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setPrefWidth(150);
        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setPrefWidth(150);
        TableColumn<Transaction, Double> stratsCodeColumn = new TableColumn<>("Strats Code");
        amountColumn.setPrefWidth(150);
        TableColumn<Transaction, Timestamp> timestampColumn = new TableColumn<>("Time");
        timestampColumn.setPrefWidth(150);

        //Create the Table view for the transaction
        transactionTable = new TableView<>();
        transactionTable.getColumns().addAll(cryptocurrencyColumn, priceColumn, amountColumn,stratsCodeColumn,timestampColumn);
        VBox transactionTableBox = new VBox(20, transactionTable);
        transactionTableBox.setPadding(new Insets(10));

        // Data entry panel for editing and deleting previous transactions
        Button editButton = new Button("Edit Transaction");
        Button deleteButton = new Button("Delete Transaction");
        Button searchButton = new Button("Search");
        Button AnalyseButton = new Button("Analyse");
        Button refresh = new Button("Refresh Table");
        DatePicker startDatePicker = new DatePicker();
        Label toLabel = new Label("To");
        DatePicker endDatePicker = new DatePicker();
        Label CCYLabel = new Label("Trend Analysis Type");
        Label nameOrStratsLabel = new Label("Selecte the type");
        ChoiceBox<String> nameOrStrats = new ChoiceBox<String>();
        ChoiceBox<String> nameOrStratsField = new ChoiceBox<String>();
        
        //GUI
        BorderPane borderPane = new BorderPane();
        HBox topButtons = new HBox(10, editButton, deleteButton, refresh);
        topButtons.setAlignment(Pos.CENTER_LEFT);
        topButtons.setPadding(new Insets(10));
        HBox searchArea = new HBox(10, nameOrStratsLabel, nameOrStrats, nameOrStratsField, searchButton);
        searchArea.setAlignment(Pos.CENTER_LEFT);
        searchArea.setPadding(new Insets(10));
        
        
        //Analysis I can do with data
        ChoiceBox<String> AnalyseChoicebox = new ChoiceBox<>();
        AnalyseChoicebox.getItems().add("LineChartAnalyse");
        AnalyseChoicebox.getItems().add("ScatterChartAnalyse");
        AnalyseChoicebox.getItems().add("StandardDeviationChartAnalyse");
        AnalyseChoicebox.getItems().add("LinearRegressionAnalyse");
        
        //GUI
        HBox bottomAnalysis = new HBox(10, CCYLabel, AnalyseChoicebox, AnalyseButton);
        bottomAnalysis.setAlignment(Pos.CENTER_LEFT);
        bottomAnalysis.setPadding(new Insets(10));
        HBox datePickers = new HBox(10, startDatePicker, toLabel, endDatePicker);
        datePickers.setAlignment(Pos.CENTER_LEFT);
        datePickers.setPadding(new Insets(10));     
        VBox topSection = new VBox(5, topButtons);
        VBox bottomSection = new VBox(5, datePickers,searchArea, bottomAnalysis);
        borderPane.setTop(topSection);
        borderPane.setCenter(transactionTableBox);
        borderPane.setBottom(bottomSection);
     
        //the choicebox types when user choose CCY or Stratscode
        nameOrStrats.getItems().add("Cryptocurrency");
        nameOrStrats.getItems().add("Stratscode");
        
        //Action listener for types
        nameOrStrats.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            nameOrStratsField.getItems().clear();
            if ("Cryptocurrency".equals(newValue)) {
                nameOrStratsField.getItems().addAll(FileManager.readCryptoTypes());
            } else if ("Stratscode".equals(newValue)) {
                nameOrStratsField.getItems().addAll(FileManager.readStratscodeTypes());
            }
        });
        
        //Chart Analysis + Action listener for all kinds of Analysis
        AnalyseChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("LineChartAnalyse")) {
                    AnalyseButton.setOnAction(event -> lca.displayLineChart(transactionData));
                } else if (newValue.equals("ScatterChartAnalyse")) {
                    AnalyseButton.setOnAction(event -> sca.displayScatterChart(transactionData));
                } else if (newValue.equals("StandardDeviationChartAnalyse")) {
                	AnalyseButton.setOnAction(event -> SDca.displayStandardDeviationChart(dbManager));
                }else if (newValue.equals("LinearRegressionAnalyse")) {
                	AnalyseButton.setOnAction(event -> lra.displayLinearRegressionChart(dbManager));
                }
            }
        });
   
        //Button for edit and delete
        editButton.setOnAction(event -> editTransaction());
        deleteButton.setOnAction(event -> deleteTransaction());
        

        //Active listener that can know when the data field got a new input, it will reevaluate the method being implemented to searchButton
        ChangeListener<Object> ListenerForSearch = (observable, oldValue, newValue) -> {
            if (nameOrStratsField.getValue() != null && !nameOrStratsField.getValue().isEmpty() 
                && startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
                System.out.println("Searching with both name and date");
                searchButton.setOnAction(event -> searchwithBoth(startDatePicker.getValue(), endDatePicker.getValue(), nameOrStratsField.getValue()));
            } else if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
                System.out.println("Searching with date only");
                searchButton.setOnAction(event -> searchTransactions(startDatePicker.getValue(), endDatePicker.getValue()));
            } else if (nameOrStratsField.getValue() != null && !nameOrStratsField.getValue().isEmpty()) {
                System.out.println("Searching with name or stratscode only");
                searchButton.setOnAction(event -> searchTransactions(nameOrStratsField.getValue()));
            }
        };

        nameOrStratsField.getSelectionModel().selectedItemProperty().addListener(ListenerForSearch);
        startDatePicker.valueProperty().addListener(ListenerForSearch);
        endDatePicker.valueProperty().addListener(ListenerForSearch);


      

        //refresh the loading page with refresh button
        refresh.setOnAction(event -> loadTransactions());

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab addTransactionTab = new Tab("Add Transaction");
        addTransactionTab.setContent(grid);
       
        Tab viewTransactionsTab = new Tab("View Transactions");
        viewTransactionsTab.setContent(borderPane);

        viewTransactionsTab.setOnSelectionChanged(event -> {
            if (viewTransactionsTab.isSelected()) {
                loadTransactions();
            }
        });

       
        tabPane.getTabs().addAll(addTransactionTab, viewTransactionsTab);

        VBox mainLayout = new VBox(tabPane);

        mainScene = new Scene(mainLayout, 700, 600);
        primaryStage.setScene(mainScene);
        mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
       
        databaseManager.createDatabase();
        databaseManager.createTransactionTable();
        loadTransactions();
    }
    
    //Add CCY type or Strats code type into CSV files
    private void addCCYtype(String ccy) {
    	CcyChoicebox.getItems().add(ccy);
    	primaryStage.setScene(mainScene);
    	FileManager.writeCryptoTypes(CcyChoicebox.getItems());
    }
    private void addStratscodetype(String stratscode) {
    	StratscodeChoicebox.getItems().add(stratscode);
    	primaryStage.setScene(mainScene);
    	FileManager.writeStratscodeTypes(StratscodeChoicebox.getItems());
    }
    
    
    private void addTransaction(ChoiceBox<String> CcyChoicebox) {
    	//Data validity checker
    	boolean flag = false;
    	while(!flag) {
        	try {
                double amount = Double.parseDouble(amountField.getText());
                double price = Double.parseDouble(priceField.getText());
                flag = true;
            } catch (NumberFormatException e) {
                showAlert("Invalid input.", "Please enter a valid double value.");
                break;
            }
    	}
    	
    	String cryptocurrency = CcyChoicebox.getValue();
    	double amount = Double.parseDouble(amountField.getText());
    	double price = Double.parseDouble(priceField.getText());
    	String stratsCode = StratscodeChoicebox.getValue();
    	Timestamp timestamp = Timestamp.from(Instant.now()); //instant.now() is the time right now
 
  
    	if(flag==true) {
    	//adding transaction to the database
    	databaseManager.addTransaction(cryptocurrency, price, amount,stratsCode, timestamp);
    	showAlert("Success", "Transaction added successfully");
    	
    	priceField.clear();
    	amountField.clear();
    	StratsCodeField.clear();
    	}
    }
    	
    
    //Method that load the transaction Table
    private void loadTransactions() {
    	transactionTable.getItems().clear();
    	
    	List<Transaction> transactions = databaseManager.getTransactions();
    	
    	for (Transaction transaction : transactions) {
    		transactionTable.getItems().add(transaction);
    	}
    	
    	TableColumn<Transaction, String> cryptocurrencyColumn = (TableColumn<Transaction, String>) transactionTable.getColumns().get(0);
    	TableColumn<Transaction, Double> priceColumn = (TableColumn<Transaction, Double>) transactionTable.getColumns().get(1);
    	TableColumn<Transaction, Double> amountColumn = (TableColumn<Transaction, Double>) transactionTable.getColumns().get(2);
    	TableColumn<Transaction, String> stratsCodeColumn = (TableColumn<Transaction, String>) transactionTable.getColumns().get(3);
    	TableColumn<Transaction, String> timestampColumn = (TableColumn<Transaction, String>) transactionTable.getColumns().get(4);
    	
    	cryptocurrencyColumn.setCellValueFactory(new PropertyValueFactory<>("cryptocurrency"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    	amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    	stratsCodeColumn.setCellValueFactory(new PropertyValueFactory<>("stratscode"));
    	
    	timestampColumn.setCellValueFactory(cellData -> {
    		Timestamp timestamp = cellData.getValue().getTimestamp();
   
    		if(timestamp == null) {
    			return new ReadOnlyStringWrapper("Unknown");
    		}else {
    		String formattedTimestamp = timestamp.toString();
    		return new ReadOnlyStringWrapper(formattedTimestamp);
    		}
    
    	});
    } 

    private void editTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            Stage editStage = new Stage();
            editStage.setTitle("Edit Transaction");

            //GUI for update values
            Label cryptocurrencyLabel = new Label("Cryptocurrency:");
            Label priceLabel = new Label("Price:");
            TextField priceField = new TextField(String.valueOf(selectedTransaction.getPrice()));
            Label amountLabel = new Label("Amount:");
            TextField amountField = new TextField(String.valueOf(selectedTransaction.getAmount()));
            Label StratsCodeLabel = new Label("StratsCode:");
            Button saveButton = new Button("Save");
            Button cancelButton = new Button("Cancel");

            saveButton.setOnAction(event -> {
                String updatedCryptocurrency = CcyChoicebox.getValue();
                double updatedPrice = Double.parseDouble(priceField.getText());
                double updatedamount = Double.parseDouble(amountField.getText());
                String updatedStratsCode= StratscodeChoicebox.getValue();
                
                
                // Update the selected transaction with new Inputs
                selectedTransaction.setCryptocurrency(updatedCryptocurrency);
                selectedTransaction.setPrice(updatedPrice);
                selectedTransaction.setAmount(updatedamount);
                selectedTransaction.setStratscode(updatedStratsCode);

                // Refresh the table view
                transactionTable.refresh();

                // Update the transaction in the database
                databaseManager.updateTransaction(selectedTransaction);
                //update the data for analyse
                transactionData = dbManager.getTransactionData();
                editStage.close();

                showAlert("Transaction Updated", "The selected transaction has been updated successfully.");
            });

            cancelButton.setOnAction(event -> editStage.close());

            HBox buttonBox = new HBox(10, saveButton, cancelButton);
            buttonBox.setPadding(new Insets(10));

            VBox editLayout = new VBox(10, cryptocurrencyLabel, CcyChoicebox, priceLabel, priceField, amountLabel, amountField,StratsCodeLabel,StratscodeChoicebox, buttonBox);
            editLayout.setPadding(new Insets(10));

            Scene editScene = new Scene(editLayout);

            editStage.setScene(editScene);
            editStage.show();
        } else {
            showAlert("No Transaction Selected", "Please select a transaction to edit.");
        }
    }
    
    
    /* Override
     * Can search with Date + CCY/Stratscode
     * Can search with Date Only
     * Can search with CCY/Stratscode only
     */
    private void searchTransactions(LocalDate startDate, LocalDate endDate) {
        ObservableList<Transaction> filteredTransactions = FXCollections.observableArrayList();

        for (Transaction transaction : transactionTable.getItems()) {
            LocalDate transactionDate = transaction.getTimestamp().toLocalDateTime().toLocalDate();
            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {
                filteredTransactions.add(transaction);
            }
        }

        transactionTable.setItems(filteredTransactions);
    }
    private void searchTransactions(String ccyOrStrat) {
    	ObservableList<Transaction> filteredTransactions = FXCollections.observableArrayList();

        for (Transaction transaction : transactionTable.getItems()) {
            if (transaction.getCryptocurrency().equalsIgnoreCase(ccyOrStrat) ||transaction.getStratscode().equalsIgnoreCase(ccyOrStrat)) {
                filteredTransactions.add(transaction);
            }
        }
        transactionTable.setItems(filteredTransactions);
        
    }
 
 
    private void searchwithBoth(LocalDate startDate, LocalDate endDate,String cryptocurrency) {
        ObservableList<Transaction> filteredTransactions = FXCollections.observableArrayList();

        for (Transaction transaction : transactionTable.getItems()) {
        	LocalDate transactionDate = transaction.getTimestamp().toLocalDateTime().toLocalDate();
            if (transaction.getCryptocurrency().equalsIgnoreCase(cryptocurrency)) {
            	if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {
                    filteredTransactions.add(transaction);
                }
            }
        }

        transactionTable.setItems(filteredTransactions);
    }
 
    
    private void deleteTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            databaseManager.deleteTransaction(selectedTransaction);

            showAlert("Transaction Deleted", "The selected transaction has been deleted successfully.");

            transactionTable.getItems().remove(selectedTransaction);
        } else {
            showAlert("No Transaction Selected", "Please select a transaction to delete.");
        }
    }

    //CSV file import
    private void importTransactionsFromCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            List<Transaction> transactions = readTransactionsFromCsv(selectedFile);
            if (!transactions.isEmpty()) {
                databaseManager.insertTransactions(transactions);
                showAlert("Import Successful", "Transactions imported successfully from CSV.");
                loadTransactions();
            } else {
                showAlert("Import Failed", "No valid transactions found in the CSV file.");
            }
        }
    }
    
    //CSV Reader
    private List<Transaction> readTransactionsFromCsv(File file) {
        List<Transaction> transactions = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(0).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length == 5) {
                    String cryptocurrency = line[0];
                    double price = Double.parseDouble(line[1]);
                    double amount = Double.parseDouble(line[2]);
                    String stratsCode = line[3];
                    String time = line[4];
                    Timestamp timestamp = null;

                    if (time != null && !time.isEmpty()) {
                    	timestamp = Timestamp.valueOf(time);
                    }
                 
                    Transaction transaction = new Transaction(0, cryptocurrency, price, amount, stratsCode, timestamp);
                    transactions.add(transaction);
                } 
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
