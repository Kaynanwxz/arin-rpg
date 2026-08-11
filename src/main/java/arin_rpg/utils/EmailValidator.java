package arin_rpg.utils;

import org.xbill.DNS.*;

public class EmailValidator {

    public static boolean domainExists(String email){

        if(email == null || email.isBlank()) return false;

        String domain = email.substring(email.indexOf("@")+1);

        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            lookup.run();

            return lookup.getResult() == Lookup.SUCCESSFUL;

        } catch (Exception e){
            return false;
        }
    }
}
