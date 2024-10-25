import java.util.*;
import java.io.*;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int stock;
    private String id;
    private double price;
    private Waitlist waitlist;  // Use the Waitlist class

    public Product(String name, int stock, String id, double price) {
        this.name = name;
        this.stock = stock;
        this.id = id;
        this.price = price;
        this.waitlist = new Waitlist();
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public void reduceStock(int quantity) {
        if (quantity <= stock) {
            stock -= quantity;
        }
    }

    public void increaseStock(int quantity) {
        stock += quantity;
    }

    // Add a client and quantity to the waitlist
    public void addToWaitlist(Client client, int quantity) {
        waitlist.add(client, quantity);
    }

    // Get the waitlist for this product
    public Waitlist getWaitlist() {
        return waitlist;
    }

    @Override
    public String toString() {
        return "Name: " + name + " | Stock: " + stock + " | ID: " + id + " | Price: $" + price;
    }
}