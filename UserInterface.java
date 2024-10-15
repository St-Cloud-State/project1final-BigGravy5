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
  private static final int SAVE = 7;
  private static final int RETRIEVE = 8;
  private static final int HELP = 9;
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
    System.out.println("Enter a number between 0 and 9 as explained below:");
    System.out.println(EXIT + " to Exit\n"); //0
    System.out.println(ADD_CLIENT + " to add a client"); //1
    System.out.println(DISPLAY_CLIENTS + " to display clients"); //2
    System.out.println(ADD_PRODUCT + " to add a product"); //3
    System.out.println(DISPLAY_PRODUCTS + " to display products"); //4 
    System.out.println(ADD_PRODUCTS_TO_CLIENTS_WISHLIST + " to add products to a clients wishlist"); //5
    System.out.println(DISPLAY_CLIENTS_WISHLIST + " to display a clients wishlist"); //6
    System.out.println(SAVE + " to save");
    System.out.println(RETRIEVE + " to retrieve");
    System.out.println(HELP + " for help"); //9
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
    String name = getToken("Enter client ID");
    Client client = warehouse.searchClientId(name);

    if (client != null) {
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

            // Ask the user if they want to add another product to the wishlist
            String more = getToken("Do you want to add another product to the client's wishlist? (Y|y for yes, any other key for no): ");
            if (!more.equalsIgnoreCase("y")) {
                moreProducts = false;  // Exit the loop if the user doesn't want to add more
            }
        }
    } else {
        System.out.println("Client does not exist");
    }
}

public void displayClientsWishlist(){
  String name = getToken("Enter client id");
  Client client = warehouse.searchClientId(name);
  client.displayWishlist();
  
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