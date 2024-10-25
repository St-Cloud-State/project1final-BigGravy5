import java.util.*;
import java.io.*;

public class Waitlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Client, Integer> waitlistEntries;

    public Waitlist() {
        this.waitlistEntries = new HashMap<>();
    }

    // Add a client and requested quantity to the waitlist
    public void add(Client client, int quantity) {
        waitlistEntries.put(client, waitlistEntries.getOrDefault(client, 0) + quantity);
    }

    // Remove a client from the waitlist
    public boolean remove(Client client) {
        return waitlistEntries.remove(client) != null;
    }

    // Check if the waitlist is empty
    public boolean isEmpty() {
        return waitlistEntries.isEmpty();
    }

    // Get the current waitlist entries
    public Map<Client, Integer> getEntries() {
        return waitlistEntries;
    }

    // Display the waitlist
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Waitlist:\n");
        for (Map.Entry<Client, Integer> entry : waitlistEntries.entrySet()) {
            Client client = entry.getKey();
            int quantity = entry.getValue();
            sb.append("Client: ").append(client.getName()).append(" | Requested Quantity: ").append(quantity).append("\n");
        }
        return sb.toString();
    }
}