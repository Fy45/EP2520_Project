/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkiservice;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;
/**
 *
 * @author harry
 */
public class ltcaClient {
    
    
     public static String call(String method, int reqType, String encodeReq) {
        String result = null;
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    
        try{

            config.setServerURL(new URL("https://172.31.212.119/cgi-bin/ltca"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            // Assuming some.method has a 'String', 'Int', 'Int' signature and returns Int
            Object[] params = new Object[]{ 
                                            new Integer(reqType),
                                            new String(encodeReq)
                                          };
            
           
		    
            result = (String)client.execute(method, params);
            
            
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e.getMessage());
	}
        return result;
     }

}
