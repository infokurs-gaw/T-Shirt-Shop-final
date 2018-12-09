/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop;

import t.shirt.shop.client.ClientController;
import t.shirt.shop.client.ShopClient;
import t.shirt.shop.server.ShopServer;

/**
 *
 * @author rob
 */
public class TShirtShop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ShopServer(234);
        new ShopClient("127.0.0.1", 234);
        
        
    }
    
}
