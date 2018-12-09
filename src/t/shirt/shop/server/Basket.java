/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop.server;

import t.shirt.shop.server.Order;
import java.util.*;

public class Basket
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen

    ArrayList<Product> products = new ArrayList<>();
    Account owner;

    //public String bestaetigen;
    /**
     * Konstruktor fuer Objekte der Klasse Basket
     */
    public Basket(Account owner)
    {
        this.owner = owner;
    }

    public int getAmount() {
        int totalAmount = 0;
        for(Product p: products) {
            totalAmount += p.getAmount();
        }
        return totalAmount;
    }
    
    
    public int getTotalPrice() {
        int totalPrice = 0;
        for(Product p: products) {
            totalPrice += (p.getPrice() * p.getAmount());
        }
        return totalPrice;
    }
    
    
    /**
     * Ein Beispiel einer Methode - ersetzen Sie diesen Kommentar mit Ihrem eigenen
     * 
     * @param  y    ein Beispielparameter fuer eine Methode
     * @return        die Summe aus x und y
     */
    public void addProduct(Product product)
    {
        products.add(product);
    }

    public void AttentionDeleteBasket()
    {
        products.clear();    
    }

    public ArrayList<Product> getBasket() {
        return products;
    }

    public ArrayList<String> getBasketAsStringlist() {
        ArrayList<String> al = new ArrayList<String>();

        for(int i = 0; i<products.size(); i++) {
            String pString = "";
            pString += "PRODUCT:";
            pString += products.get(i).getType() + ":";
            pString += products.get(i).getType() + ":";
        }

        return al;
    }

    public void displayBasket()
    {
        if(products.isEmpty())
        {
            System.out.println("--------START-------");
            System.out.println("Es befinden sich keine Artikel im Warenkorb");
            System.out.println("--------END-------");
        }
        else{
            System.out.println("--------START-------");
            for (int i = 0;i<products.size();i++){
                System.out.println("Sie bestellen:");
                System.out.println("Artikelart: " +products.get(i).getType());
                System.out.println("Artikelname: "+products.get(i).getName());
                System.out.println("Produktfarbe: "+products.get(i).getColor());
                System.out.println("Gr��e: " +products.get(i).getSize());
                System.out.println("Produktbeschreibung: "+products.get(i).getDescription());
                System.out.println("Das Produkt kostet: "+products.get(i).getPrice());

                System.out.println("-------");
            }

            System.out.println("Der Gesamtpreis betr�gt: "+ this.getTotalPrice());

            System.out.println("f�r "+getAmount()+" Artikel");
            System.out.println("--------END-------");
        }

    }

    // public void deleteDasProduct(Product product)
    //{
    // products.remove(product);
    // }

    public void deleteProduct(int id)
    {
        for(Product p: products) {
            if(p.getId() == id) {
                products.remove(p);
            }
        }
    }


    public void purchase(Order x)
    {
        Product[] pArray = new Product[products.size()];
        pArray = products.toArray(pArray);
        x.setProducts(pArray);

    }

    public Account getOwner ()
    {
        return owner;
    }

}
