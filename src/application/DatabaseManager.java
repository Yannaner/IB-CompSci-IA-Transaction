package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class DatabaseManager {
    private final String DB_URL = "jdbc:mysql://localhost:3306/transaction_db";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "@S08jj123lol";

    public void createDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", DB_USERNAME, DB_PASSWORD)) {
            String query = "CREATE DATABASE IF NOT EXISTS transaction_db";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTransactionTable() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Create the database if it doesn't exist
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS transaction_db";
            PreparedStatement createDatabaseStatement = connection.prepareStatement(createDatabaseQuery);
            createDatabaseStatement.executeUpdate();

            // Switch to the transaction_db database
            String useDatabaseQuery = "USE transaction_db";
            PreparedStatement useDatabaseStatement = connection.prepareStatement(useDatabaseQuery);
            useDatabaseStatement.executeUpdate();

            // Create the transactions table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS transactions (id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "cryptocurrency VARCHAR(255), price DOUBLE, amount DOUBLE, stratscode VARCHAR(255), transaction_time TIMESTAMP)";
            PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery);
            createTableStatement.executeUpdate();

            // Check if the transaction_time column exists and add it if it doesn't
            String checkColumnQuery = "SHOW COLUMNS FROM transactions LIKE 'transaction_time'";
            PreparedStatement checkColumnStatement = connection.prepareStatement(checkColumnQuery);
            ResultSet resultSet = checkColumnStatement.executeQuery();

            // This if method can be since the time is added, modify for later use
            if (!resultSet.next()) {
                String alterTableQuery = "ALTER TABLE transactions ADD COLUMN transaction_time TIMESTAMP";
                PreparedStatement alterTableStatement = connection.prepareStatement(alterTableQuery);
                alterTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(String cryptocurrency, double price, double amount, String stratsCode, Timestamp timestamp) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO transactions (cryptocurrency, price, amount, stratscode, transaction_time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cryptocurrency);
            statement.setDouble(2, price);
            statement.setDouble(3, amount);
            statement.setString(4, stratsCode);
            statement.setTimestamp(5, timestamp); // Set timestamp

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM transactions";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String cryptocurrency = resultSet.getString("cryptocurrency");
                double price = resultSet.getDouble("price");
                double amount = resultSet.getDouble("amount");
                String stratscode = resultSet.getString("stratscode");
                Timestamp timestamp = resultSet.getTimestamp("transaction_time");

                Transaction transaction = new Transaction(id, cryptocurrency, price, amount, stratscode, timestamp);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public void updateTransaction(Transaction transaction) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "UPDATE transactions SET cryptocurrency=?, price=?, amount=?, stratscode=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, transaction.getCryptocurrency());
            statement.setDouble(2, transaction.getPrice());
            statement.setDouble(3, transaction.getMarketWorth());
            statement.setString(4, transaction.getStratscode());
            statement.setInt(5, transaction.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(Transaction transaction) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "DELETE FROM transactions WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, transaction.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertTransactions(List<Transaction> transactions) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO transactions (cryptocurrency, price, amount, stratscode) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (Transaction transaction : transactions) {
                statement.setString(1, transaction.getCryptocurrency());
                statement.setDouble(2, transaction.getPrice());
                statement.setDouble(3, transaction.getMarketWorth());
                statement.setString(4, transaction.getStratscode());
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    transaction.setId(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
