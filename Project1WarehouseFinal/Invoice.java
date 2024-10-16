import java.io.*;
import java.util.*;

public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;
    private Client client;
    private Map<Product, Integer> orderedItems;
    private double totalCost;
    private Date date;

    public Invoice(Client client, Map<Product, Integer> orderedItems) {
        this.client = client;
        this.orderedItems = new HashMap<>(orderedItems);
        this.totalCost = calculateTotalCost();
        this.date = new Date();
    }

    private double calculateTotalCost() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : orderedItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            total += product.getPrice() * quantity;
        }
        return total;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Date getDate() {
        return date;
    }

    public Client getClient() {
        return client;
    }

    public Map<Product, Integer> getOrderedItems() {
        return orderedItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice for ").append(client.getName()).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Ordered items:\n");

        for (Map.Entry<Product, Integer> entry : orderedItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            sb.append(product.getName())
                .append(" | Quantity: ").append(quantity)
                .append(" | Price: $").append(product.getPrice())
                .append(" | Subtotal: $").append(product.getPrice() * quantity)
                .append("\n");
        }

        sb.append("Total Cost: $").append(totalCost).append("\n");
        return sb.toString();
    }
}