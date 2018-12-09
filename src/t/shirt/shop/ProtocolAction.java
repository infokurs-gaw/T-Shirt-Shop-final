/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop;

import java.util.HashMap;

public interface ProtocolAction {
    void performAction(HashMap<String, String> fields, String ip, int port);
}