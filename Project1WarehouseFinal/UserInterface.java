import java.util.*;
import java.text.*;
import java.io.*;
public class UserInterface {
  private static UserInterface userInterface;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int DISPLAY_CLIENTS = 2;
  private static final int ADD_PRODUCT = 3;
  private static final int DISPLAY_PRODUCTS = 4;
  private static final int ADD_PRODUCTS_TO_CLIENTS_WISHLIST = 5;
  private static final int DISPLAY_CLIENTS_WISHLIST = 6;
  private static final int PROCESS_CLIENT_WISHLIST = 7;
  private static final int RECEIVE_SHIPMENT = 8;
  private static final int DISPLAY_INVOICES = 9;
  private static final int DISPLAY_PRODUCT_WAITLIST = 10;
  private static final int SAVE = 11;
  private static final int RETRIEVE = 12;
  private static final int HELP = 13;
  private UserInterface() {
    if (yesOrNo("Look for saved data and  use it?")) {
      retrieve();
    } else {
      warehouse = Warehouse.instance();
    }
  }
  public static UserInterface instance() {
    if (userInterface == null) {
      return userInterface = new UserInterface();
    } else {
      return userInterface;
    }
  }
  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }
  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  public Calendar getDate(String prompt) {
    do {
      try {
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe) {
        System.out.println("Please input a date as mm/dd/yy");
      }
    } while (true);
  }
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 13 as explained below:");
    System.out.println(EXIT + " to Exit\n"); //0
    System.out.println(ADD_CLIENT + " to add a client"); //1
    System.out.println(DISPLAY_CLIENTS + " to display clients"); //2
    System.out.println(ADD_PRODUCT + " to add a product"); //3
    System.out.println(DISPLAY_PRODUCTS + " to display products"); //4 
    System.out.println(ADD_PRODUCTS_TO_CLIENTS_WISHLIST + " to add products to a clients wishlist"); //5
    System.out.println(DISPLAY_CLIENTS_WISHLIST + " to display a clients wishlist"); //6
    System.out.println(PROCESS_CLIENT_WISHLIST + " to process a clients wishlist"); //7
    System.out.println(RECEIVE_SHIPMENT + " to recieve a shipment"); //8
    System.out.println(DISPLAY_INVOICES + " to display client's invoices"); //9
    System.out.println(DISPLAY_PRODUCT_WAITLIST + " to display a products waitlist"); //10
    System.out.println(SAVE + " to save"); //11
    System.out.println(RETRIEVE + " to retrieve"); //12
    System.out.println(HELP + " for help"); //13
  }

  public void addClient() {
    String name = getToken("Enter client name");
    String address = getToken("Enter address");
    String phone = getToken("Enter phone");
    Client result = warehouse.addClient(name, address, phone); // Use the warehouse instance instead of Warehouse
    if (result == null) {
        System.out.println("Could not add client");
    } else {
        System.out.println(result);
    }
}

public void showClients() {
    Iterator<Client> allClients = warehouse.getClients(); 
    while (allClients.hasNext()) {
        Client client = allClients.next();
        System.out.println(client.toString());
    }
}

public void addProduct() {
    boolean moreProducts = true;
    while (moreProducts) {
        String name = getToken("Enter product name");
        int stock = Integer.parseInt(getToken("Enter stock amount"));
        double price = Double.parseDouble(getToken("Enter price"));
        String id = getToken("Enter product ID");

        Product result = warehouse.addProduct(name, stock, id, price);
        if (result == null) {
            System.out.println("Could not add product");
        } else {
            System.out.println("Product added: " + result);
        }

        // Ask the user if they want to add another product
        String more = getToken("Do you want to add another product? (Y|y for yes, any other key for no): ");
        if (!more.equalsIgnoreCase("y")) {
            moreProducts = false;  // Exit the loop if the user doesn't want to add more
        }
    }
}

public void showProducts() {
    Iterator<Product> allProducts = warehouse.getProducts(); 
    while (allProducts.hasNext()) {
        Product product = allProducts.next();
        System.out.println(product.toString());
    }
}



public void addProductToClientsWishlist() {
    String clientId = getToken("Enter client ID");
    Client client = warehouse.searchClientId(clientId);

    if (!isValidClient(client)) return;

    boolean moreProducts = true;
    while (moreProducts) {
        String productName = getToken("Enter product name");
        Product product = warehouse.searchProductName(productName);

        if (product != null) {
            int quantity = Integer.parseInt(getToken("Enter quantity"));
            boolean added = warehouse.addToWishlist(client, product, quantity);

            if (added) {
                System.out.println("Product added to wishlist: " + product.getName() + " (Quantity: " + quantity + ")");
            } else {
                System.out.println("Failed to add product to wishlist");
            }
        } else {
            System.out.println("Product does not exist");
        }

        String more = getToken("Do you want to add another product to the client's wishlist? (Y|y for yes, any other key for no): ");
        if (!more.equalsIgnoreCase("y")) {
            moreProducts = false;
        }
    }
}

public void displayClientsWishlist() {
    String clientId = getToken("Enter client ID");
    Client client = warehouse.searchClientId(clientId);

    if (!isValidClient(client)) return;

    Wishlist wishlist = client.getWishlist();
    if (isWishlistEmpty(wishlist)) return;

    client.displayWishlist();
}


public void processClientsWishlist() {
    String clientId = getToken("Enter client ID");
    Client client = warehouse.searchClientId(clientId);

    if (!isValidClient(client)) return;

    Wishlist wishlist = client.getWishlist();
    if (isWishlistEmpty(wishlist)) return;

    Map<Product, Integer> orderItems = new HashMap<>();

    Map<Product, Integer> items = wishlist.getWishlistItems();
    for (Map.Entry<Product, Integer> entry : items.entrySet()) {
        Product product = entry.getKey();
        int requestedQuantity = entry.getValue();
        int availableStock = product.getStock();

        System.out.println("Product: " + product.getName() + " | Requested: " + requestedQuantity + " | Available: " + availableStock);
        String userChoice = getToken("Options: (1)Change quantity, (2)Remove item, (3)Leave as is");

        // Handle user choice
        if (userChoice.equalsIgnoreCase("1")) {
            // Update quantity
            int newQuantity = Integer.parseInt(getToken("Enter new quantity: "));
            if (newQuantity <= 0) {
                wishlist.removeProduct(product);
                System.out.println("Product removed from wishlist.");
                continue; // Skip further processing for this product
            } else {
                wishlist.updateProductQuantity(product, newQuantity); // Update the quantity in the wishlist
                requestedQuantity = newQuantity; // Update requestedQuantity to reflect the new value
            }
        } else if (userChoice.equalsIgnoreCase("2")) {
            wishlist.removeProduct(product);
            System.out.println("Product removed from wishlist.");
            continue; // Skip further processing for this product
        }

        // Check if the requested quantity exceeds available stock
        if (requestedQuantity > availableStock) {
            int fulfilledQuantity = availableStock;
            int waitlistedQuantity = requestedQuantity - availableStock;

            // Update the stock and fulfill only what is available
            product.reduceStock(fulfilledQuantity);
            orderItems.put(product, fulfilledQuantity);

            // Add the remaining quantity to the waitlist
            product.addToWaitlist(client, waitlistedQuantity);
            System.out.println("Only " + fulfilledQuantity + " of " + requestedQuantity + " could be fulfilled. Remaining " +
                                waitlistedQuantity + " has been added to the waitlist.");
        } else {
            // Fulfill the entire order if stock is sufficient
            product.reduceStock(requestedQuantity);
            orderItems.put(product, requestedQuantity);
        }
    }

    if (!orderItems.isEmpty()) {
        createInvoiceForClient(client, orderItems);
        wishlist.getWishlistItems().clear();
        System.out.println("Wishlist has been cleared. Order has been placed.");
    } else {
        System.out.println("No items left in the wishlist. No order created.");
    }
}


private boolean isValidClient(Client client) {
    if (client == null) {
        System.out.println("Client not found.");
        return false;
    }
    return true;
}

private boolean isWishlistEmpty(Wishlist wishlist) {
    if (wishlist.isEmpty()) {
        System.out.println("The wishlist is empty.");
        return true;
    }
    return false;
}

private Map<Product, Integer> processWishlistItems(Wishlist wishlist) {
    Map<Product, Integer> orderItems = new HashMap<>();
    Map<Product, Integer> items = wishlist.getWishlistItems();

    for (Map.Entry<Product, Integer> entry : items.entrySet()) {
        Product product = entry.getKey();
        int currentQuantity = entry.getValue();

        System.out.println("Product: " + product.getName() + " | Quantity: " + currentQuantity);
        String userChoice = getToken("Options: (1)Change quantity, (2)Remove item, (3)Leave as is");

        handleUserChoice(wishlist, orderItems, product, currentQuantity, userChoice);
    }

    System.out.println("Wishlist processing complete");
    return orderItems;
}

private void handleUserChoice(Wishlist wishlist, Map<Product, Integer> orderItems, 
                              Product product, int currentQuantity, String userChoice) {
    switch (userChoice) {
        case "1": // Change quantity
            int newQuantity = Integer.parseInt(getToken("Enter new quantity: "));
            if (newQuantity <= 0) {
                wishlist.removeProduct(product);
                System.out.println("Product removed from wishlist.");
            } else {
                wishlist.updateProductQuantity(product, newQuantity);
                orderItems.put(product, newQuantity);
                System.out.println("Product quantity updated.");
            }
            break;
        case "2": // Remove item
            wishlist.removeProduct(product);
            System.out.println("Product removed from wishlist.");
            break;
        default: // Leave as is
            orderItems.put(product, currentQuantity);
            System.out.println("Leaving product as is.");
            break;
    }
}

private void createInvoiceForClient(Client client, Map<Product, Integer> orderItems) {
    Invoice invoice = new Invoice(client, orderItems);
    client.addInvoice(invoice);
    System.out.println("Generated Invoice:");
    System.out.println(invoice.toString());
}

public void displayProductWaitlist() {
    String productName = getToken("Enter product name");
    Product product = warehouse.searchProductName(productName);

    if (product == null) {
        System.out.println("Product not found.");
        return;
    }

    Waitlist waitlist = product.getWaitlist();
    if (waitlist.isEmpty()) {
        System.out.println("No waitlist entries for this product.");
        return;
    }

    System.out.println("Waitlist for product: " + product.getName());
    System.out.println(waitlist.toString());
}



public void receiveShipment(){
  //DUMMY TEST
  System.out.println("RECEIVE SHIPMENT");
}

public void displayInvoices() {
    String clientId = getToken("Enter client ID");
    Client client = warehouse.searchClientId(clientId);

    if (!isValidClient(client)) return;

    Iterator<Invoice> invoiceIterator = client.getInvoiceIterator();

    if (!invoiceIterator.hasNext()) {
        System.out.println("No invoices found for this client.");
        return;
    }

    System.out.println("Invoices for client: " + client.getName());

    while (invoiceIterator.hasNext()) {
        Invoice invoice = invoiceIterator.next();
        System.out.println(invoice.toString());
    }
}
   
  
  private void save() {
    if (warehouse.save()) {
      System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
    } else {
      System.out.println(" There has been an error in saving \n" );
    }
  }
  private void retrieve() {
    try {
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("File doesnt exist; creating new Warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }
  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT:        addClient();
                                break;
        case DISPLAY_CLIENTS:   showClients();
                                break;
        case ADD_PRODUCT:        addProduct();
                                break;
        case DISPLAY_PRODUCTS:   showProducts();
                                break;
        case ADD_PRODUCTS_TO_CLIENTS_WISHLIST:  addProductToClientsWishlist();
                                break;
        case DISPLAY_CLIENTS_WISHLIST:   displayClientsWishlist();
                                break;
        case PROCESS_CLIENT_WISHLIST: processClientsWishlist();
                                break;
        case RECEIVE_SHIPMENT: receiveShipment();
                                break;
        case DISPLAY_INVOICES: displayInvoices();
                                break;
        case DISPLAY_PRODUCT_WAITLIST: displayProductWaitlist();
                                break;
        case SAVE:              save();
                                break;
        case RETRIEVE:          retrieve();
                                break;       
        case HELP:              help();
                                break;
      }
    }
  }
  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}