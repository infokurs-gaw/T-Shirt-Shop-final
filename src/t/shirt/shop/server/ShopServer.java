/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop.server;

import java.util.*;
import java.io.*;
import t.shirt.shop.ProtocolDefinition;

public class ShopServer extends Server {
    DatabaseAccess db = new DatabaseAccess();
    Basket basket;
    BehaviorAnalyzer ba = new BehaviorAnalyzer();
    // Store for each IP address the assoicated account
    HashMap<String, Account> accounts = new HashMap<String, Account>();
    HashMap<Account, Basket> baskets = new HashMap<Account, Basket>();

    ProtocolDefinition[] protocol;

    public ShopServer(int pPortnummer) {
        super(pPortnummer);

        this.protocol = new ProtocolDefinition[] { new ProtocolDefinition("LOGIN:<email>:<password>",
                (HashMap<String, String> fields, String ip, int port) -> {
                    Account account = this.accounts.get(ip);
                    if (account != null)
                        this.send(ip, port, "ERROR: User is already logged in as " + account.getName());
                    else
                        logIn(ip, port, fields.get("email"), fields.get("password"));
                }), new ProtocolDefinition("REGISTER:<name>:<address>:<email>:<credit-card>:<pass>",
                        (HashMap<String, String> fields, String ip, int port) -> {
                            this.register(ip, port, -1, fields.get("name"), fields.get("address"), fields.get("email"),
                                    fields.get("credit-card"), fields.get("pass"));
                        }),
                new ProtocolDefinition("ADD:<product-id>:<amount>",
                        (HashMap<String, String> fields, String ip, int port) -> {
                            addNewProducts(ip, port, Integer.valueOf(fields.get("product-id")),
                                    Integer.valueOf(fields.get("amount")));
                        }),
                new ProtocolDefinition("BUY", (HashMap<String, String> fields, String ip, int port) -> {
                    this.buy(ip, port);
                }), new ProtocolDefinition("LOGOFF", (HashMap<String, String> fields, String ip, int port) -> {
                    this.logOff(ip, port);
                }), new ProtocolDefinition("CLOSE_CONNECTION", (HashMap<String, String> fields, String ip, int port) -> {
                    closeConnection(ip, port);
                }), new ProtocolDefinition("GET_PRODUCT_INFO_BY_ID:<id>", (HashMap<String, String> fields, String ip, int port) -> {
                    this.sendProductInfoById(ip, port, Integer.parseInt(fields.get("id")));
                }) };
    }

    public void printProtocol(){
        System.out.println("-------- SERVER PROTOKOLL ---------");
        for (ProtocolDefinition p : protocol) {
            System.out.println(p.getDefinition());
        }
        System.out.println("----- SERVER PROTOKOLL ENDE ------");
    }

    public void processNewConnection(String pClientIP, int pClientPort) {

        send(pClientIP, pClientPort, "MESSAGE:Willkommen in unserem T-Shirt Shop!!!");
    }

    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        for (ProtocolDefinition def : protocol) {
            def.matchExtractPerform(pMessage, pClientIP, pClientPort);
        }
    }

    public void processClosingConnection(String pClientIP, int pClientPort) {
        // this.send(pClientIP, pClientPort, "Auf Wiedersehen!");
    }

    public void register(String pClientIP, int pClientPort, int id, String name, String address, String email,
            String creditCard, String pass) {
        Account newAccount = new Account(id, name, address, email, creditCard, null);

        db.addAccount(newAccount, pass);

        accounts.put(pClientIP, newAccount);
        String newId = String.valueOf(db.getAccount(email, pass).getId());
        send(pClientIP, pClientPort, "NEWACCOUNT:" + newId);
    }

    public void logIn(String pClientIP, int pClientPort, String email, String pass) {
        if (db.getAccount(email, pass) != null) {
            Account newAccount = db.getAccount(email, pass);
            send(pClientIP, pClientPort,
                    "SUCCESS: " + newAccount.getName() + " wurde eingeloggt und dieser IP zugeordnet.");
            this.accounts.put(pClientIP, newAccount);
            this.baskets.put(newAccount, new Basket(newAccount));
            this.ba.startTimer(newAccount);
        } else {
            send(pClientIP, pClientPort, "ERROR:Nutzername oder Passwort falsch! Bitte ?erpr?? Sie Ihre Eingabe");
        }

    }

    public void sendProductInfoById(String pClientIP, int pClientPort, int id) {
        Product p = db.getProductById(id);
        if (p != null) {
            send(pClientIP, pClientPort,
                    "PRODUCT_INFO:"
                            + String.join(":", new String[] { String.valueOf(p.getId()), p.getType(), p.getName(),
                                    p.getDescription(), String.valueOf(p.getPrice()), p.getColor(), p.getSize() }));
        } else {
            send(pClientIP, pClientPort, "ERROR: Product could not be found.");
        }
    }

    public void addNewProducts(String pClientIP, int pClientPort, int id, int amount) {
        int realAmount = -1;
        if (db.getProductById(id) != null) {
            Product newProduct = db.getProductById(id);

            if (db.getAvailableAmountForProductInStock(newProduct) != -1) {
                realAmount = db.getAvailableAmountForProductInStock(newProduct);

                if (realAmount >= amount) {
                    basket = baskets.get(accounts.get(pClientIP));
                    basket.addProduct(newProduct);

                    String productId = String.valueOf(id);
                    String productAmount = String.valueOf(amount);
                    String totalPrice = String.valueOf(basket.getTotalPrice());

                    send(pClientIP, pClientPort, "BASKETINFO:" + productId + ":" + productAmount + ":" + totalPrice);
                }

                else {
                    send(pClientIP, pClientPort, "MESSAGE:Nicht genug Produkte verf??r!");
                }
            } else {
                send(pClientIP, pClientPort, "ERROR:Ein unerwarteter Fehler ist aufgetreten!");
            }
        } else {
            send(pClientIP, pClientPort, "ERROR:Ein unerwarteter Fehler ist aufgetreten!");
        }

    }

    public void buy(String pClientIP, int pClientPort) {
        Basket basket = baskets.get(accounts.get(pClientIP));

        Product[] products = new Product[basket.getBasket().size()];
        basket.getBasket().toArray(products);

        Order newOrder = new Order(-1, products, accounts.get(pClientIP), new Date(), "AUFGEGEBEN");

        if (db.addOrder(newOrder)) {
            send(pClientIP, pClientPort, "SUCCESS:Bestellung erfolgreich");
        } else {
            send(pClientIP, pClientPort, "ERROR:Ein unerwarteter Fehler ist aufgetreten!");
        }

    }

    public void logAccountsHashMap() {
        System.out.println(this.accounts);
    }

    public void logOff(String pClientIP, int pClientPort) {
        Account a = accounts.get(pClientIP);
        ba.stopTimer(a);

        baskets.remove(accounts.get(pClientIP));
        accounts.remove(pClientIP);

        send(pClientIP, pClientPort, "MESSAGE:Auf Wiedersehen, " + a.getName());

        // closeConnection(pClientIP, pClientPort);

    }

}