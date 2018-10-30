/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkiservice;
import pkiservice.Interfaces.*;
import java.util.Random;
import java.sql.Timestamp;
import java.security.cert.X509Certificate;
import java.io.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
/**
 *
 * @author harry
 */
public class Pkiservice {
    
    public static String VOUCHER_RES;
    public static String X509_RES;
    public static String ISSUE_X509;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        try{
         // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
 
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // Trust always
            }
 
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Trust always
            }
        }
    };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        // Create empty HostnameVerifier
        HostnameVerifier hv = new HostnameVerifier() {
                    public boolean verify(String arg0, SSLSession arg1) {
                            return true;
                    }
        };

        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);}catch(Exception e){
            e.printStackTrace();
        }
        
        requestVoucher();
        requestX509();
    
}
    public static void requestVoucher(){
        Random random = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        msgVoucherReq_V2LTCA.Builder voucherReq = msgVoucherReq_V2LTCA.newBuilder();

        voucherReq.setIReqType(120);
        voucherReq.setStrUserName("");
        voucherReq.setStrPwd("");
        voucherReq.setStrEmailAddress("hongyiz@kth.se");
        voucherReq.setStrCaptcha("captcha");
        voucherReq.setINonce(random.nextInt(65536));
        voucherReq.setTTimeStamp(timestamp.getTime());

        String encodedReq = Base64.encodeToString(voucherReq.build().toByteArray(), Base64.NO_WRAP);
        
        String response = (String) ltcaClient.call("ltca.operate", 120, encodedReq);
        
        VOUCHER_RES = response;
        
        System.out.println("Voucher for Hongyi :"+response);
        
        FileWriter writer = null;  
        
        try {     
            writer = new FileWriter("VOUCHER.crt", false);     
            writer.write(response);       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            } 
    }
    }
    
    public static void requestX509(){
        
        Random random = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        msgX509CertReq_V2LTCA.Builder voucherReq = msgX509CertReq_V2LTCA.newBuilder();

        voucherReq.setIReqType(122);
        voucherReq.setILTCAIdRange(1002);
        voucherReq.setStrProofOfPossessionVoucher(VOUCHER_RES);
        voucherReq.setStrX509CertReq("Yes");
        voucherReq.setINonce(random.nextInt(65536));
        voucherReq.setTTimeStamp(timestamp.getTime());

        String encodedReq = Base64.encodeToString(voucherReq.build().toByteArray(), Base64.NO_WRAP);
        
        String response = (String) ltcaClient.call("ltca.operate", 122, encodedReq);
        
        X509_RES = response;
        
        System.out.println("X509 for Hongyi :"+response);
        
        FileWriter writer = null;  
        
        try {     
            writer = new FileWriter("X509.crt", false);     
            writer.write(response);       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            } 
    }
        
    }
    
   
    
}
