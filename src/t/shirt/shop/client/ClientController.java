/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop.client;

import t.shirt.shop.client.Home;
import t.shirt.shop.client.LoginFalsch;
import t.shirt.shop.client.Login;
import t.shirt.shop.client.LoginLogout;
import t.shirt.shop.client.Produktänderung;
import t.shirt.shop.client.VielenDank;
import t.shirt.shop.client.Warenkorb;

/**
 *
 * @author marlon.rosendahl
 */
public class ClientController {

    Home home;
    LoginLogout loginLogout;
    Warenkorb warenkorb;
    Produktänderung produktänderung;
    Login login;
    VielenDank vielendank;
    LoginFalsch loginfalsch;
    ShopClient client;

    public ClientController(ShopClient client) {
        this.client = client;
        home = new Home(this);
        loginLogout = new LoginLogout(this);
        warenkorb = new Warenkorb(this);
        produktänderung = new Produktänderung(this);
        login = new Login(this);
        vielendank = new VielenDank(this);
        loginfalsch = new LoginFalsch(this);

        login.setVisible(true);
    }

    public void showFrame(String name) {
        warenkorb.setVisible(false);
        loginLogout.setVisible(false);
        login.setVisible(false);
        produktänderung.setVisible(false);
        vielendank.setVisible(false);
        loginfalsch.setVisible(false);
        login.setVisible(false);
        
        switch(name) {
            case "warekorb":
                warenkorb.setVisible(true);
            case "loginLogout":
                loginLogout.setVisible(true);
            case "home":
                home.setVisible(true);
            case "produktänderung":
                produktänderung.setVisible(true);
            case "vielendank":
                vielendank.setVisible(true);
            case "loginfalsch":
                loginfalsch.setVisible(true);
            case "login":
                login.setVisible(true);
            default:
                System.out.println("KEIN GÜLTIGES FENSTER");
        }
        
    }
    
    public void logIn(String email, String pass) {
        this.client.logIn(email, pass);
    }
}
