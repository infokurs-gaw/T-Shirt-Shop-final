/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop.server;

import t.shirt.shop.server.Order;
import t.shirt.shop.server.QueryResult;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * Beschreiben Sie hier die Klasse DatabaseAccess.
 * 
 * @author (Ihr Name)
 * @version (eine Versionsnummer oder ein Datum)
 */
public class DatabaseAccess {
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private DatabaseConnector dbConnector;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * Konstruktor für Objekte der Klasse DatabaseAccess
     */
    public DatabaseAccess() {
        // Instanzvariable initialisi eren
        // https://bitbucket.org/xerial/sqlite-jdbc/downloads/ Driver must be
        // installed!!!
        this.init();
    }

    public void init() {
        try {

            String dbName = "datenbank2.db";
            Path dbPath = Paths.get(dbName);

            if (!Files.exists(dbPath)) {
                this.dbConnector = new DatabaseConnector("", 0, dbName, "", "");
                String sql = new String(Files.readAllBytes(Paths.get("create-queries.sql")));
                String[] queries = sql.split(";");

                for (int i = 0; i < queries.length; i++) {
                    this.dbConnector.executeStatement(queries[i]);
                }

                System.out.println("SQLite Database " + dbName
                    + " should now be successfully initialized and populated. Continuing...");

            } else {
                System.out.println("Skipping init... db file already exists.");
                this.dbConnector = new DatabaseConnector("", 0, dbName, "", "");

                String m = this.dbConnector.getErrorMessage();

                System.out.println(m);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String extractField(String fieldName, String[] colNames, String[] row) {
        int index = -1;
        for (int i = 0; i < row.length; i++) {
            if (colNames[i].equals(fieldName)) {
                index = i;
                break;
            }
        }

        return row[index];

    }

    public void logLogin(Account a) {
        this.dbConnector
        .executeStatement("INSERT INTO event_log(account_id, event) VALUES (" + a.getId() + ", 'LOGIN');");
    }

    public void logLogout(Account a) {
        this.dbConnector
        .executeStatement("INSERT INTO event_log(account_id, event) VALUES (" + a.getId() + ", 'LOGOUT');");
    }

    public Date[] getLoginDates(Account a) {
        this.dbConnector
        .executeStatement("SELECT * from event_log el WHERE event = 'LOGIN' AND account_id = " + a.getId());
        QueryResult res = this.dbConnector.getCurrentQueryResult();

        if (res != null) {
            String[] cols = res.getColumnNames();

            Date[] dates = new Date[res.getRowCount()];

            for (int i = 0; i < res.getRowCount(); i++) {

                String[] row = res.getData()[i];

                String timestamp = this.extractField("timestamp", cols, row);
                try {
                    dates[i] = this.df.parse(timestamp);
                } catch (Exception e) {
                    dates[i] = null;
                }
            }

            return dates;
        } else {
            System.out.println(dbConnector.getErrorMessage());

            return null;
        }
    }

    public Date[] getLogoutDates(Account a) {
        this.dbConnector
        .executeStatement("SELECT * from event_log el WHERE event = 'LOGOUT' AND account_id = " + a.getId());
        QueryResult res = this.dbConnector.getCurrentQueryResult();

        if (res != null) {
            String[] cols = res.getColumnNames();

            Date[] dates = new Date[res.getRowCount()];

            for (int i = 0; i < res.getRowCount(); i++) {

                String[] row = res.getData()[i];

                String timestamp = this.extractField("timestamp", cols, row);
                try {
                    dates[i] = this.df.parse(timestamp);
                } catch (Exception e) {
                    dates[i] = null;
                }
            }

            return dates;
        } else {
            System.out.println(dbConnector.getErrorMessage());

            return null;
        }
    }

    public DatabaseConnector getConnector() {
        return this.dbConnector;
    }

    public Product getProductById(int pId) {
        this.dbConnector.executeStatement(
            "SELECT s.id, p.name, color, size, type, price, description from stock s JOIN colors c ON s.color_id = c.id JOIN products p on p.id = s.product_id JOIN sizes si ON s.size_id = si.id JOIN product_types pt ON pt.id = p.product_type_id WHERE s.id = "
            + pId + " LIMIT 1;");
        QueryResult res = this.dbConnector.getCurrentQueryResult();

        if (res != null) {
            String[] cols = res.getColumnNames();

            Product[] prod = new Product[res.getRowCount()];

            if (res.getRowCount() > 0) {
                String[] row = res.getData()[0];

                int id = Integer.parseInt(this.extractField("id", cols, row));
                double price = Double.parseDouble(this.extractField("price", cols, row));
                String name = this.extractField("name", cols, row), color = this.extractField("color", cols, row),
                size = this.extractField("size", cols, row), type = this.extractField("type", cols, row),
                description = this.extractField("description", cols, row);

                return new Product(id, type, name, description, price, color, size);
            } else {
                return null;
            }

        } else {
            System.out.println(dbConnector.getErrorMessage());

            return null;
        }

    }

    public int getAvailableAmountForProductInStock(Product p) {
        this.dbConnector.executeStatement("SELECT s.id, amount FROM stock s WHERE s.id = " + p.getId() + " LIMIT 1;");
        QueryResult res = this.dbConnector.getCurrentQueryResult();

        if (res != null) {
            String[] cols = res.getColumnNames();

            Product[] prod = new Product[res.getRowCount()];

            if (res.getRowCount() > 0) {
                String[] row = res.getData()[0];
                int amount = Integer.parseInt(this.extractField("amount", cols, row));

                return amount;
            } else {
                return -1;
            }

        } else {
            System.out.println(dbConnector.getErrorMessage());

            return -1;
        }
    }

    public Product[] getProducts() {
        this.dbConnector.executeStatement(
            "SELECT s.id, p.name, color, size, type, price, description from stock s JOIN colors c ON s.color_id = c.id JOIN products p on p.id = s.product_id JOIN sizes si ON s.size_id = si.id JOIN product_types pt ON pt.id = p.product_type_id;");
        QueryResult res = this.dbConnector.getCurrentQueryResult();

        if (res != null) {
            String[] cols = res.getColumnNames();

            Product[] prods = new Product[res.getRowCount()];

            for (int i = 0; i < res.getRowCount(); i++) {

                String[] row = res.getData()[i];

                int id = Integer.parseInt(this.extractField("id", cols, row));
                double price = Double.parseDouble(this.extractField("price", cols, row));
                String name = this.extractField("name", cols, row), color = this.extractField("color", cols, row),
                size = this.extractField("size", cols, row), type = this.extractField("type", cols, row),
                description = this.extractField("description", cols, row);

                prods[i] = new Product(id, type, name, description, price, color, size);

            }

            return prods;
        } else {
            System.out.println(dbConnector.getErrorMessage());

            return null;
        }

    }

    private Account getAccountById(int pId) {
        this.dbConnector.executeStatement("select * from accounts where id = " + pId);
        QueryResult res = this.dbConnector.getCurrentQueryResult();
        if (res != null) {
            String[] cols = res.getColumnNames();
            if (res.getRowCount() > 0) {
                String[] row = res.getData()[0];
                int id = Integer.parseInt(this.extractField("id", cols, row));
                String name = this.extractField("name", cols, row);
                String address = this.extractField("address", cols, row);
                String email = this.extractField("email", cols, row);
                String creditCard = this.extractField("credit_card", cols, row);
                String lastViewedProductIdString = this.extractField("last_viewed_product_id", cols, row);
                int last_viewed_product_id = !lastViewedProductIdString.equals("NULL") ? Integer.parseInt(lastViewedProductIdString) : -1;
                Product lastViewedProduct = this
                    .getProductById(last_viewed_product_id);

                return new Account(id, name, address, email, creditCard, lastViewedProduct);
            } else {
                return null;
            }
        } else {
            System.out.println(this.dbConnector.getErrorMessage());
            return null;
        }

    }

    private Product[] getProductsInOrderId(int pId) {
        this.dbConnector.executeStatement("SELECT * from order_stock WHERE order_id = " + pId);

        QueryResult res = this.dbConnector.getCurrentQueryResult();

        if (res != null) {
            String[] cols = res.getColumnNames();
            Product[] products = new Product[res.getRowCount()];

            for (int i = 0; i < res.getRowCount(); i++) {

                String[] row = res.getData()[i];

                int id = Integer.parseInt(this.extractField("id", cols, row));
                int stockId = Integer.parseInt(this.extractField("stock_id", cols, row));
                int amount = Integer.parseInt(this.extractField("amount", cols, row));

                Product p = this.getProductById(stockId);
                p.setAmount(amount);

                products[i] = p;

            }

            return products;
        } else {
            System.out.println(dbConnector.getErrorMessage());

            return null;
        }
    }

    public Order[] getOrders() {
        this.dbConnector.executeStatement(
            "SELECT o.id, o.account_id, o.order_date, os.status from orders o JOIN  order_status os ON os.id = o.status_id;");

        QueryResult res = this.dbConnector.getCurrentQueryResult();

        if (res != null) {
            String[] cols = res.getColumnNames();
            Order[] orders = new Order[res.getRowCount()];

            for (int i = 0; i < res.getRowCount(); i++) {

                String[] row = res.getData()[i];

                int id = Integer.parseInt(this.extractField("id", cols, row));
                int accountId = Integer.parseInt(this.extractField("account_id", cols, row));
                Date orderDate = null;
                try {
                    orderDate = this.df.parse(this.extractField("order_date", cols, row));
                } catch (Exception e) {
                    // TODO: handle exception
                }
                String status = this.extractField("status", cols, row);

                orders[i] = new Order(id, this.getProductsInOrderId(id), this.getAccountById(accountId), orderDate,
                    status);

            }

            return orders;
        } else {
            System.out.println(dbConnector.getErrorMessage());

            return null;
        }
    }

    public boolean addOrder(Order o) {
        this.dbConnector.executeStatement("INSERT INTO orders(account_id) VALUES (" + o.getAccount().getId() + ");");

        Order[] orders = this.getOrders();
        Order latestOrder = orders[orders.length - 1];
        int lastInsertedId = latestOrder.getId();

        Product[] prodsInOrder = o.getProducts();

        for (int i = 0; i < prodsInOrder.length; i++) {
            this.dbConnector.executeStatement("INSERT INTO order_stock(order_id, stock_id, amount) VALUES ("
                + lastInsertedId + ", " + prodsInOrder[i].getId() + ", " + prodsInOrder[i].getAmount() + ");");
        }

        return true;
    }

    public Account getAccount(String user, String password)
    {
        this.dbConnector.executeStatement("select * from accounts where email = '" + user + "' AND password = '" + password + "';");
        QueryResult res = this.dbConnector.getCurrentQueryResult();
        if (res != null) {            
            String[] cols = res.getColumnNames();
            if(res.getRowCount() > 0){
                String[] row = res.getData()[0];
                int id = Integer.parseInt(this.extractField("id", cols, row));
                String name = this.extractField("name", cols, row);
                String address = this.extractField("address", cols, row);
                String email = this.extractField("email", cols, row);
                String creditCard = this.extractField("credit_card", cols, row);
                String lastViewedProductIdString = this.extractField("last_viewed_product_id", cols, row);
                int last_viewed_product_id = !lastViewedProductIdString.equals("NULL") ? Integer.parseInt(lastViewedProductIdString) : -1;
                Product lastViewedProduct = getProductById(last_viewed_product_id);
                return new Account(id, name, address, email, creditCard, lastViewedProduct);
            }else{
                return null;
            }
        } 
        else {
            System.out.println(this.dbConnector.getErrorMessage());
            return null;
        }

    }

    public boolean  addAccount(Account newAccount, String password)
    {

        // Switched to use of formatted string because it is easier to read and write.
        this.dbConnector.executeStatement(String.format("INSERT INTO accounts(name, address, email, password, credit_card) VALUES ('%s', '%s', '%s', '%s', '%s');", newAccount.getName(), newAccount.getAddress(), newAccount.getEmail(), password, newAccount.getCreditCard()));

        // If there is no error, the method will return true.
        return this.dbConnector.getErrorMessage() == null;        
    }
}
