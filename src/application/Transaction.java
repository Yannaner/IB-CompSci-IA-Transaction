package application;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private String cryptocurrency;
    private double price;
    private double amount;
    private Timestamp transaction_time;
    private String stratscode;
    
    
    public Transaction(int id, String cryptocurrency, double price, double amount,String stratscode, Timestamp currentTimestamp) {
        this.id = id;
        this.cryptocurrency = cryptocurrency;
        this.price = price;
        this.amount=amount;
        this.transaction_time = currentTimestamp;
        this.stratscode= stratscode;
    }
    public Transaction(double price, double amount) {
    	this.price = price;
    	this.amount = amount;
    }

    public String getStratscode() {
		return stratscode;
	}

	public void setStratscode(String stratscode) {
		this.stratscode = stratscode;
	}

	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCryptocurrency() {
        return cryptocurrency;
    }

    public void setCryptocurrency(String cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public Timestamp getTimestamp() {
        return transaction_time;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.transaction_time = timestamp;
    }
}

