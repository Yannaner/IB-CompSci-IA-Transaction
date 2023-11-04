package application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class TransactionController {
    private TableView<Transaction> transactionTable;
    private TextField cryptocurrencyField;
    private TextField priceField;
    private TextField marketWorthField;
    private TextField StratsCodeField;

    private Stage primaryStage;
    private Scene loginScene;
    private Scene mainScene;
    private Scene AddCCYScene;
    private Scene AddStratsCodeScene;
    private final DatabaseManager databaseManager;
    ChoiceBox<String> CcyChoicebox = new ChoiceBox<>();
    ChoiceBox<String> StratscodeChoicebox = new ChoiceBox<>();
    
    
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
        //initialize the item in ccy & stratscode choice box
        CcyChoicebox.getItems().addAll(FileManager.readCryptoTypes());
        StratscodeChoicebox.getItems().addAll(FileManager.readStratscodeTypes());
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private boolean validateLogin(String username, String password) {
        if (username.equals("") && password.equals("")){
        	return true;
        }else if (username.equals("admin") && password.equals("admin")) {
        	return true;
        }
		return false;
    }
   
//Alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMainScene() {
        // Data entry 
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
        AddCCYScene = new Scene(AddCCYBox, 400, 200);
        AddCcyButton.setOnAction(event ->  primaryStage.setScene(AddCCYScene));
        primaryStage.show();
        AddCcyButton2.setOnAction(event ->  {
        	addCCYtype(Addccytextfield.getText());
        	Addccytextfield.clear();
        });
        
        //Choice box for stratscode
        VBox AddStratscodeBox = new VBox(10, AddStratscode,AddStratscodefield,AddStratscode2);
        AddStratscodeBox.setPadding(new Insets(10));
        AddStratsCodeScene = new Scene(AddStratscodeBox, 400, 200);
        AddStratscodeButton.setOnAction(event ->  primaryStage.setScene(AddStratsCodeScene));
        primaryStage.show();
        AddStratscode2.setOnAction(event ->  {
        	addStratscodetype(AddStratscodefield.getText());
        	AddStratscodefield.clear();
    });
        
     
        
        
        Label priceLabel = new Label("Price:");
        priceField = new TextField();
        Label marketWorthLabel = new Label("Amount:");
        marketWorthField = new TextField();
        Label StratsCodeLabel = new Label("Strats Code:");
        StratsCodeField = new TextField();
        Button addButton = new Button("Add Transaction");
        Button importButton = new Button("Import");
        
      
        
        VBox importBox = new VBox(10, importButton);
        importBox.setPadding(new Insets(10));

        importButton.setOnAction(event -> importTransactionsFromCsv());
        
        
        VBox addTransactionBox = new VBox(10, cryptocurrencyLabel, CcyChoicebox, priceLabel, priceField,
                marketWorthLabel, marketWorthField,StratsCodeLabel,StratscodeChoicebox, addButton,importButton , AddCcyButton,AddStratscodeButton);
        addTransactionBox.setPadding(new Insets(10));

        addButton.setOnAction(event -> addTransaction(CcyChoicebox));

        
        
        // Page to view and sort all previous transactions by time
        TableColumn<Transaction, String> cryptocurrencyColumn = new TableColumn<>("Cryptocurrency");
        cryptocurrencyColumn.setPrefWidth(150);
        TableColumn<Transaction, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setPrefWidth(150);
        TableColumn<Transaction, Double> marketWorthColumn = new TableColumn<>("Amount");
        marketWorthColumn.setPrefWidth(150);
        TableColumn<Transaction, Double> stratsCodeColumn = new TableColumn<>("Strats Code");
        marketWorthColumn.setPrefWidth(150);
        TableColumn<Transaction, Timestamp> timestampColumn = new TableColumn<>("Time");
        timestampColumn.setPrefWidth(150);

        
        transactionTable = new TableView<>();
        transactionTable.getColumns().addAll(cryptocurrencyColumn, priceColumn, marketWorthColumn,stratsCodeColumn,timestampColumn);
        VBox transactionTableBox = new VBox(10, transactionTable);
        transactionTableBox.setPadding(new Insets(10));

        // Data entry panel for editing and deleting previous transactions
        Button editButton = new Button("Edit Transaction");
        Button deleteButton = new Button("Delete Transaction");
        Button searchButton = new Button("Search");
        Button AnalyseButton = new Button("Analyse");
        Button refresh = new Button("Refresh");
        DatePicker startDatePicker = new DatePicker();
        Label toLabel = new Label("to");
        DatePicker endDatePicker = new DatePicker();
        Label CCYLabel = new Label("CCY");
        TextField CCYname = new TextField();
        VBox searchBox = new VBox(10, startDatePicker, toLabel, endDatePicker,CCYLabel,CCYname, searchButton,refresh);
        searchBox.setPadding(new Insets(5));
        
        
        VBox editAndDeleteBox = new VBox(10);
        editAndDeleteBox.setPadding(new Insets(10));
        editAndDeleteBox.getChildren().addAll(editButton, deleteButton,searchBox,startDatePicker, toLabel, endDatePicker,searchButton);

        editButton.setOnAction(event -> editTransaction());
        deleteButton.setOnAction(event -> deleteTransaction());
        
        //BUG HERE, IT CANT SEARCH with time
        if(CCYname.getText()!=null && startDatePicker.getValue()!=null &&endDatePicker.getValue()!=null) {
        searchButton.setOnAction(event -> searchwithBoth(startDatePicker.getValue(), endDatePicker.getValue(),CCYname.getText()));
        CCYname.clear();
        }else if(CCYname.getText()==null){
        searchButton.setOnAction(event -> searchTransactions(startDatePicker.getValue(), endDatePicker.getValue()));    
        
        }else {
        searchButton.setOnAction(event -> searchTransactions(CCYname.getText()));
        }
        
        //refresh the loading page with refresh button
        refresh.setOnAction(event -> loadTransactions());

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab addTransactionTab = new Tab("Add Transaction");
        addTransactionTab.setContent(addTransactionBox);

        Tab viewTransactionsTab = new Tab("View Transactions");
        viewTransactionsTab.setContent(new VBox(transactionTableBox, editAndDeleteBox));

        viewTransactionsTab.setOnSelectionChanged(event -> {
            if (viewTransactionsTab.isSelected()) {
                loadTransactions();
            }
        });

       
        tabPane.getTabs().addAll(addTransactionTab, viewTransactionsTab);

        VBox mainLayout = new VBox(tabPane);

        mainScene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(mainScene);

       
        databaseManager.createDatabase();
        databaseManager.createTransactionTable();
        loadTransactions();
    }
    
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
    	String cryptocurrency = CcyChoicebox.getValue();
    	double marketWorth = Double.parseDouble(marketWorthField.getText());
    	double price = Double.parseDouble(priceField.getText());
    	String stratsCode = StratscodeChoicebox.getValue();
    	Timestamp timestamp = Timestamp.from(Instant.now());
    	//time rn
    	
    	//adding transaction to the database
    	databaseManager.addTransaction(cryptocurrency, price, marketWorth,stratsCode, timestamp);
    	
    	
    	showAlert("Success", "Transaction added successfully");
    	
    	//cryptocurrencyField.clear();
    	priceField.clear();
    	marketWorthField.clear();
    	StratsCodeField.clear();
    }
    
    private void loadTransactions() {
    	transactionTable.getItems().clear();
    	
    	List<Transaction> transactions = databaseManager.getTransactions();
    	
    	for (Transaction transaction : transactions) {
    		transactionTable.getItems().add(transaction);
    	}
    	
    	TableColumn<Transaction, String> cryptocurrencyColumn = (TableColumn<Transaction, String>) transactionTable.getColumns().get(0);
    	TableColumn<Transaction, Double> priceColumn = (TableColumn<Transaction, Double>) transactionTable.getColumns().get(1);
    	TableColumn<Transaction, Double> marketWorthColumn = (TableColumn<Transaction, Double>) transactionTable.getColumns().get(2);
    	TableColumn<Transaction, String> stratsCodeColumn = (TableColumn<Transaction, String>) transactionTable.getColumns().get(3);
    	TableColumn<Transaction, String> timestampColumn = (TableColumn<Transaction, String>) transactionTable.getColumns().get(4);
    	
    	cryptocurrencyColumn.setCellValueFactory(new PropertyValueFactory<>("cryptocurrency"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    	marketWorthColumn.setCellValueFactory(new PropertyValueFactory<>("marketWorth"));
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

            // Create the data input fields for updated values
            Label cryptocurrencyLabel = new Label("Cryptocurrency:");
            TextField cryptocurrencyField = new TextField(selectedTransaction.getCryptocurrency());
            Label priceLabel = new Label("Price:");
            TextField priceField = new TextField(String.valueOf(selectedTransaction.getPrice()));
            Label marketWorthLabel = new Label("Amount:");
            TextField marketWorthField = new TextField(String.valueOf(selectedTransaction.getMarketWorth()));
            Label StratsCodeLabel = new Label("StratsCode:");
            TextField StratsCodeField = new TextField(String.valueOf(selectedTransaction.getStratscode()));
            Button saveButton = new Button("Save");
            Button cancelButton = new Button("Cancel");

            saveButton.setOnAction(event -> {
                String updatedCryptocurrency = cryptocurrencyField.getText();
                double updatedPrice = Double.parseDouble(priceField.getText());
                double updatedMarketWorth = Double.parseDouble(marketWorthField.getText());
                String updatedStratsCode= StratsCodeField.getText();
                // Update the selected transaction
                selectedTransaction.setCryptocurrency(updatedCryptocurrency);
                selectedTransaction.setPrice(updatedPrice);
                selectedTransaction.setMarketWorth(updatedMarketWorth);
                selectedTransaction.setStratscode(updatedStratsCode);

                // Refresh the table view
                transactionTable.refresh();

                // Update the transaction in the database
                databaseManager.updateTransaction(selectedTransaction);

                editStage.close();

                showAlert("Transaction Updated", "The selected transaction has been updated successfully.");
            });

            cancelButton.setOnAction(event -> editStage.close());

            HBox buttonBox = new HBox(10, saveButton, cancelButton);
            buttonBox.setPadding(new Insets(10));

            VBox editLayout = new VBox(10, cryptocurrencyLabel, cryptocurrencyField, priceLabel, priceField, marketWorthLabel, marketWorthField,StratsCodeLabel,StratsCodeField, buttonBox);
            editLayout.setPadding(new Insets(10));

            Scene editScene = new Scene(editLayout);

            editStage.setScene(editScene);
            editStage.show();
        } else {
            showAlert("No Transaction Selected", "Please select a transaction to edit.");
        }
    }

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
    private void searchTransactions(String cryptocurrency) {
    	ObservableList<Transaction> filteredTransactions = FXCollections.observableArrayList();

        for (Transaction transaction : transactionTable.getItems()) {
            if (transaction.getCryptocurrency().equalsIgnoreCase(cryptocurrency)) {
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

    private List<Transaction> readTransactionsFromCsv(File file) {
        List<Transaction> transactions = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(0).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length == 3) {
                    String cryptocurrency = line[0];
                    double price = Double.parseDouble(line[1]);
                    double marketWorth = Double.parseDouble(line[2]);
                    String stratsCode = line[3];
                    String time = line[4];
                    Timestamp Currenttime = Timestamp.valueOf(time);
                    if(time==null) {
                    	Currenttime = new Timestamp(System.currentTimeMillis());

                    }
                    
                    Transaction transaction = new Transaction(0, cryptocurrency, price, marketWorth, stratsCode, Currenttime);
                    transactions.add(transaction);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return transactions;
    }

}
