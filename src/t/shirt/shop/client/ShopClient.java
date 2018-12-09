/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop.client;

import java.util.HashMap;
import java.util.Date;
import java.util.regex.Pattern;
import t.shirt.shop.server.Product;
import t.shirt.shop.ProtocolDefinition;

public class ShopClient extends Client {

    private ClientController ctrl;
    
    public ShopClient(String pServerIP, int pServerPort) {
        super(pServerIP, pServerPort);
        this.ctrl = new ClientController(this);
    }

    public void processMessage(String pMessage) {
        
        String[] splittedMsg = pMessage.split(Pattern.quote(":")); 
        
        switch(splittedMsg[0]) {
            case "SUCCESS":
                this.ctrl.showFrame("home");
            case "ERROR":
                this.ctrl.showFrame("loginfalsch");
        }
        
        
        System.out.println(splittedMsg[0]);
        
                // TODO add your handling code here:
        
                /**
         if(loginRichtig() == true){this.setVisible(false);
        this.ctrl.showFrame("home");
         }
         else{
             this.setVisible(false);
        this.ctrl.showFrame("loginfalsch");
         }
         */
         
    }

    public void logIn(String email, String pass) {
        send("LOGIN:" + email + ":" + pass);
    }

    public void register(int id, String name, String address, String email, String creditCard, String pass) {
        send("REGISTER:" + name + ":" + address + ":" + email + ":" + creditCard + ":" + pass);
    }

    public void addNewProducts(int id, int amount) {
        send("ADD:" + id + ":" + amount);
    }

    public void buy() {
        send("BUY");
    }

    public void logOff() {
        send("LOGOFF");
    }

    public void closeConnection() {
        send("CLOSE_CONNECTION");
    }

    /**
    public Product getProductInfoById(int id) {
        send("GET_PRODUCT_INFO_BY_ID:" + id);

        ProtocolDefinition def = new ProtocolDefinition("PRODUCT_INFO:<id>:<type>:<name>:<desc>:<price>:<color>:<size>",
                null);

        Date startDate = new Date();

        while (!def.matchesDefinition(lastMessage) && (new Date().getTime()-startDate.getTime())/1000 < 10) {
            //System.out.println("WAITING...");
        }


        if((new Date().getTime()-startDate.getTime())/1000 >= 10){
            System.out.println("Could not get Product, timeout of 10 seconds expired.");
            return null;
        }else{
            HashMap<String, String> map = def.extractInformation(lastMessage);

            return new Product(Integer.parseInt(map.get("id")), map.get("type"), map.get("name"), map.get("desc"),
                    Double.parseDouble(map.get("price")), map.get("color"), map.get("size"));
        }

       
    }
    **/
}
