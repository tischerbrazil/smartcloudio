package org.geoazul.model.security;



import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Set of helper methods, which are useful in various model implementations.
 *
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public final class KeycloakModelUtils {
	
	 public static String getHostName(String urlInput) {
	        urlInput = urlInput.toLowerCase();
	        String hostName=urlInput;
	        if(!urlInput.equals("")){
	            if(urlInput.startsWith("http") || urlInput.startsWith("https")){
	                try{
	                    URL netUrl = new URL(urlInput);
	                    String host= netUrl.getHost();
	                    if(host.startsWith("www")){
	                        hostName = host.substring("www".length()+1);
	                    }else{
	                        hostName=host;
	                    }
	                }catch (MalformedURLException e){
	                    hostName=urlInput;
	                }
	            }else if(urlInput.startsWith("www")){
	                hostName=urlInput.substring("www".length()+1);
	            }
	            return  hostName;
	        }else{
	            return  "";
	        }
	    }

    private KeycloakModelUtils() {
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static byte[] generateSecret() {
        return generateSecret(32);
    }

    public static byte[] generateSecret(int bytes) {
        byte[] buf = new byte[bytes];
        new SecureRandom().nextBytes(buf);
        return buf;
    }






 

    public static String getDefaultClientAuthenticatorType() {
        return "client-secret";
    }

    public static String generateCodeSecret() {
        return UUID.randomUUID().toString();
    }

    
   


    


    public static String getMasterRealmAdminApplicationClientId(String realmName) {
        return realmName + "-realm";
    }

    // USER FEDERATION RELATED STUFF



  


    // END USER FEDERATION RELATED STUFF

    public static String toLowerCaseSafe(String str) {
        return str==null ? null : str.toLowerCase();
    }

   

  









    // Used for hardcoded role mappers
    public static String[] parseRole(String role) {
        int scopeIndex = role.lastIndexOf('.');
        if (scopeIndex > -1) {
            String appName = role.substring(0, scopeIndex);
            role = role.substring(scopeIndex + 1);
            String[] rtn = {appName, role};
            return rtn;
        } else {
            String[] rtn = {null, role};
            return rtn;

        }
    }

   
   

   

   

}
