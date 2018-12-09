/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop.server;

import t.shirt.shop.server.DatabaseAccess;
import t.shirt.shop.server.Order;
import java.util.Date;
import java.sql.Timestamp;
/**
 * Beschreiben Sie hier die Klasse BehaviorAnalyzer.
=======

/**
 * Beschreiben Sie hier die Klasse BehaiviorAnalyzer.
>>>>>>> origin/verhalten-timer
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */

public class BehaviorAnalyzer
{   

    public Product lastViewedProduct;
    public Product lastSoldProducts;
    public int datetime;

    private DatabaseAccess db = new DatabaseAccess();

    // anzahl an suzugeben produkten

    public BehaviorAnalyzer() {

    }

    /**
     * Liefert die 10 meist gekauften Produkte sortiert nach Typen(Tshirt,Pullover etc.)
     * Zur�ck gegeben werden soll ein array aus den besten 10 Produkten sortiert das [0] das beste 
     * Produkt ist
     */

    public String[] getTop10Products () {        
        //Die ben�tigten Arrays der Datenbank werden gespeichert
        // in order2 wird das Array gespeichert was noch nicht die volle L�nge hat
        Order[] orders = db.getOrders();
        String[] Typ = new String[db.getProducts().length]; 
        for(int a = 0 ;db.getProducts().length> a; a++) {
            Typ[a] = db.getProducts()[a].getType();
        }
        // Nur zaehlvariable

        // Eine Variable die die L�nge des Arrays bestimmt
        int lengthArray = 0;
        for(int b = 0; orders.length > b ; b++) {
            for(int i = 0; orders[b].getProducts().length> i; i++){
                lengthArray ++;
                if(orders[b].getProducts()[i].getAmount()>1) {
                    lengthArray += orders[b].getProducts()[i].getAmount()-1;
                }
            }
        }
        String[] ordersType = new String[lengthArray]; 
        int f = 0; 

        for(int b = 0; orders.length > b ; b++) {
            for(int i = 0; orders[b].getProducts().length> i; i++){
                ordersType[f] = orders[b].getProducts()[i].getType();  
                f++;
                if(orders[b].getProducts()[i].getAmount()>1) {
                    for(int a = 1; orders[b].getProducts()[i].getAmount()>a; a++) {
                        ordersType[f] = orders[b].getProducts()[i].getType();  
                        f++;
                    }
                }
            }
        }
        int[] Counter = new int[Typ.length]; // Das zweite Array das die selbe l�nge wie die Menge an Typen hat um parralel zu speichern wie oft
        // ein Produkt gekauft wurde

        //Er geht durch das Array durch und f�r jedes gekaufte Produkt setzt er den Z�hler h�her
        for(int a = 0 ;ordersType.length>a; a++){
            String typtmp = ordersType[a];
            int b = 0;
            if(typtmp.equals(Typ[b])){
                Counter[b] ++;
            }else {
                while(!typtmp.equals (Typ[b])){
                    b++;  

                }            
                Counter[b] ++; 
            }
        }

        // Jetzt muss das Array nur nach der Anzahl geordnet werden hier mit einem Bubblesort aber zuvor wird ein Temp�r�res Array erstellt da
        // das normale Array noch gebraucht wird

        String[] Typtmp = new String[Typ.length];
        Typtmp = Typ;

        for (int j=Typtmp.length; j>0; j--) {
            for (int i=0; i<j-1; i++) {
                if (Counter[i] > Counter[i+1]) {
                    int temp = Counter[i];
                    Counter[i] = Counter[i+1];
                    Counter[i+1] = temp;
                    String tmp2 = Typtmp[i];
                    Typtmp[i] = Typtmp[i+1];
                    Typtmp[i+1] = tmp2;
                }
            }
        }
        // Das Array muss nur noch umgedreht werden damit das an erster Stelle das meistgekaufte Produkt ist, da der Bubbleso

        String[] Typarray = new String [Typtmp.length];
        int d = Typtmp.length;
        for(int c = 0; c<Typtmp.length; c++){            
            Typarray[c] = Typtmp[d-c-1];
        }
        // Das sortierte Array muss nur noch auf die lange von 10 gek�rzt werden
        String[] returnArray = new String [10];
        if(Typarray.length > 10) {
            for(int x = 0; x< 9;x++) {
                returnArray[x] = Typarray[x];
            }
            return returnArray;
        }else {
            return Typarray;
        }
    }

    public Product[] getLastSoldProduct ( Account account) {
        Order[] orders = new Order[db.getOrders().length];
        orders = db.getOrders();
        Product[] SoldProducts = new Product[5];
        int id = account.getId();
        int a = 0;
        int b = 0;
        int c = 0;

        while(SoldProducts[4]== null && a< db.getOrders().length){
            if(orders[a].getAccount().getId() == account.getId()) {
                if(orders[a].getProducts().length>1) {
                    while(orders[a].getProducts().length>b && b< 4){
                        SoldProducts[c] = orders[a].getProducts()[b];
                        c++;
                        b++;
                    }
                }else{
                    SoldProducts[c] = orders[a].getProducts()[b];
                    c++;
                    b++;
                    a++;
                }
            }
            a++;

        }
        return SoldProducts;

    }

    /**
     * Gibt das zuletzt angeschaute Produkt wieder aus. Wenn noch kein angeschaut wurde dann nur null
     */
    public Product getLastViewedObject(Account account){
        return account.getLastViewedProduct();
    }

    public void startTimer(Account account){
        db.logLogin(account);
    }

    public void stopTimer(Account account){  
        db.logLogout(account);
    }

    public long timespend(Account account){
        Date[] loginDates =  db.getLoginDates(account);
        Date[] logoutDates =  db.getLogoutDates(account);
        long time= 0;
        for(int i=0; i<logoutDates.length; i++){
            long a = loginDates[i].getTime();
            long b = logoutDates[i].getTime();
            time = time + (b-a); 
        }
        if(loginDates.length!=logoutDates.length){
            long a = loginDates[loginDates.length-1].getTime();
            long b = new Date().getTime();
            time = time + (b-a); 
        }
        return time;
    }
}


