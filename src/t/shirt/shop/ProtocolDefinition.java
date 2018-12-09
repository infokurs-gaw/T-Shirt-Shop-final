/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t.shirt.shop;

import t.shirt.shop.server.Queue;
import java.util.regex.*;
import java.util.HashMap;
import java.util.ArrayList;;

public class ProtocolDefinition {

    private String def;
    private Pattern regDef;
    private String[] fields;
    private ProtocolAction action;

    public ProtocolDefinition(String definition, ProtocolAction action) {
        this.action = action;
        this.def = definition;
        this.regDef = Pattern.compile("^" + definition.replaceAll("<.*?>", "([^:]*?)") + "$");

        Pattern fieldPattern = Pattern.compile("<(.*?)>");
        Matcher fieldMatcher = fieldPattern.matcher(def);

        int len = 0;
        Queue<String> res = new Queue<String>();

        while (fieldMatcher.find()) {
            res.enqueue(fieldMatcher.group(1));
            len++;
        }

        this.fields = new String[len];

        int i = 0;
        while (!res.isEmpty()) {
            this.fields[i] = res.front();
            res.dequeue();
            i++;
        }
    }

    public static void main() {
        ProtocolDefinition p = new ProtocolDefinition("REGISTER:<name>:<address>:<email>:<credit-card>:<pass>", null);
        System.out.println(p.matchesDefinition("REGISTER:Tim::tim@gaw.de:1234:1234"));;
    }

    public String getDefinition(){
        return this.def;
    }

    public HashMap<String, String> extractInformation(String str) {
        if (this.matchesDefinition(str)) {
            String[] parts = str.split(":");

            HashMap<String, String> res = new HashMap<String, String>();
            // Skip first, as it has no information, just protocol desc
            for (int i = 1; i < parts.length; i++) {
                res.put(this.fields[i - 1], parts[i]);
            }

            return res;
        } else {
            return null;
        }
    }

    public void matchExtractPerform(String str, String ip, int port) {
        if (this.matchesDefinition(str)) {
            action.performAction(this.extractInformation(str), ip, port);
        }
    }

    public boolean matchesDefinition(String str) {
        return this.regDef.matcher(str).matches();
    }

}