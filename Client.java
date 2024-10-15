import java.util.*;
import java.io.*;

public class Client implements Serializable {
    private String id;
    private String name;
    private String address;
    private String phone;
    private Wishlist wishlist;
    private static final String CLIENT_STRING = "C";
    private static final long serialVersionUID = 1L;

    public Client(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        id = CLIENT_STRING + ClientIdServer.instance().getId();
        wishlist = new Wishlist();
    }

    public String getName(){ 
        return name;
    }
    public String getPhone(){
        return phone; 
    }
    public String getAddress(){ 
        return address; 
    }
    public String getId() {
        return id; 
    }
    public Wishlist getWishlist(){
        return wishlist; 
    }

    public boolean addProductToWishlist(Product product, int quantity) {
        return wishlist.addProduct(product, quantity);
    }

    public void displayWishlist() {
        wishlist.displayWishlist();
    }

    @Override
    public String toString() {
        return "Client Name: " + name + " Id: " + id + " Address: " + address + " Phone: " + phone;
    }
}