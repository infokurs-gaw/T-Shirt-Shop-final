/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop.server;

public class Account {

    private int id;
    private String name;
    private String address;
    private String email;
    private String creditCard;
    private Product lastViewedProduct;
    
    

    /**
     * Konstruktor für Objekte der Klasse Account
     */
    public Account(int id, String name, String address, String email, String creditCard, Product lastViewedProduct) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.creditCard = creditCard;
        this.lastViewedProduct = lastViewedProduct;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getEmail(){
        return email;
    }

    public String getCreditCard(){
        return creditCard;
    }

    public Product getLastViewedProduct(){
        return lastViewedProduct;
    }
    
    
    
}
