package com.barq.nadra.utils;


import org.json.JSONObject;
import org.json.XML;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;

@SuppressWarnings({"java:S1168","java:S1192","java:S4830","java:S1186","java:S4423","java:S1604","java:S5527"})
public class Utills {
    private Utills() {
    }

    public static JSONObject getSoapResponse(String url1, String xmlInputs) {
        JSONObject jObject = null;
        try {

            // Code to make a webservice HTTP request
            String responseString = "";
            StringBuilder outputString = new StringBuilder("");


            URL url = new URL(url1);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buffer;
            buffer = xmlInputs.getBytes(StandardCharsets.UTF_8);
            bout.write(buffer);
            byte[] b = bout.toByteArray();

            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", "");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            // Write the content of the request to the outputstream of the HTTP
            // Connection.
            out.write(b);
            out.close();
            // Ready with sending the request.

             // Read the response.
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(),StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);


            while ((responseString = in.readLine()) != null) {
                outputString.append(responseString);
            }


            jObject = XML.toJSONObject(outputString.toString());



        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObject;
    }
    public static JSONObject getSoapResponseFromGenerateNotificationWsdl(String url1, String xmlInputs) {
        JSONObject jObject = null;
        try {

            // Code to make a webservice HTTP request
            String responseString = "";
            StringBuilder outputString = new StringBuilder("");

            URL url = new URL(url1);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            String xmlInput = xmlInputs;
            byte[] buffer ;
            buffer = xmlInput.getBytes(StandardCharsets.UTF_8);
            bout.write(buffer);
            byte[] b = bout.toByteArray();

            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", "");
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Host","mb-jsblintegration.ermispk.com");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            // Write the content of the request to the outputstream of the HTTP
            // Connection.
            out.write(b);
            out.close();
            // Ready with sending the request.

            // Read the response.
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(),StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);


            while ((responseString = in.readLine()) != null) {
                outputString.append(responseString);
            }


            jObject = XML.toJSONObject(outputString.toString());



        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObject;
    }
    public static JSONObject getSoapResponseFromOldMicroWsdl(String url1, String xmlInputs) {
        JSONObject jObject = null;
        try {

            // Code to make a webservice HTTP request
            String responseString = "";
            StringBuilder outputString = new StringBuilder("");

            URL url = new URL(url1);
            trustAllHosts();
            URLConnection connection = url.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            String xmlInput = xmlInputs;
            byte[] buffer ;
            buffer = xmlInput.getBytes(StandardCharsets.UTF_8);
            bout.write(buffer);
            byte[] b = bout.toByteArray();

            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", "");
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Host","blbuat.jappuat.com");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            // Write the content of the request to the outputstream of the HTTP
            // Connection.
            out.write(b);
            out.close();
            // Ready with sending the request.

            // Read the response.
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(),StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);


            while ((responseString = in.readLine()) != null) {
                outputString.append(responseString);
            }


            jObject = XML.toJSONObject(outputString.toString());



        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObject;
    }

    public static void trustAllHosts()
    {
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509ExtendedTrustManager()
                    {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {

                        }



                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle)
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle)
                        {

                        }

                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new  HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e)
        {
         e.printStackTrace();
        }
    }


}
