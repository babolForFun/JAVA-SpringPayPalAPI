package com.babolforfun.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.babolforfun.bean.JsonResponseLink;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Controller set payment
 * @author babolForFun
 *
 */
@Controller  
public class CreatePayment {
	
	// Log4J
	private static final Logger logger = 
			Logger.getLogger(AuthenticationOAuth .class.getName());

	// URLs
	private String URL_RETURN  = "http://[SERVER_IP]:[PORT]/SpringBaseLayout/web/process/success";
	private String URL_CANCEL  = "http://[SERVER_IP]:[PORT]/SpringBaseLayout/web/process/cancel";
    private String URL_PAYMENT = "https://api.sandbox.paypal.com/v1/payments/payment";

    // Payment
    private String PAYMENT_AMOUNT   = "75.47";
    private String PAYMENT_CURRENCY = "USD";
    
    /**
     * Create payment 
     * @param request request
     * @param res response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/createpayment", method = RequestMethod.POST)  
    public ModelAndView createPayment(HttpServletRequest request,HttpServletResponse res) throws IOException{  
    	
    	String ACCESS_TOKEN = request.getParameter("accesstoken");
    	String REDIRECT_URL = generatePaymentRequest(ACCESS_TOKEN);
        
    	return new ModelAndView("createpayment", "redirect_url", REDIRECT_URL);  
    }  
   
    /**
     * Generate payment 
     * @param ACCESS_TOKEN acces token
     * @return redirect URL
     * @throws IOException
     */
    public String generatePaymentRequest(String ACCESS_TOKEN)  throws IOException{
    	
    	// Variables
    	int BUFFER_INDEX;
    	StringBuilder JSON_RESPONSE = new StringBuilder();
    	List<JsonResponseLink> JSON_RESPONSE_LINKS = null;
    	
    	// curl_init and url
        URL HTTP_URL = new URL(URL_PAYMENT);
        HttpURLConnection HTTP_CONNECTION = (HttpURLConnection) HTTP_URL.openConnection();

        // Set HTTP attribute
        HTTP_CONNECTION.setRequestMethod("POST");
        HTTP_CONNECTION.setInstanceFollowRedirects(true);
        HTTP_CONNECTION.setDoOutput(true);
        HTTP_CONNECTION.setDoInput(true);

        // Header
        Map<String,String> HEADERS = new HashMap<>();
        HEADERS.put("Content-Type","application/json");
        HEADERS.put("Authorization","Bearer "+ ACCESS_TOKEN);
        setHeaderRequest(HEADERS,HTTP_CONNECTION);

        // Post data
        String POST_DATA = createJson();
        HTTP_CONNECTION.setRequestProperty("Content-length", String.valueOf(POST_DATA.length()));
        DataOutputStream OUTPUT_STREAM = new DataOutputStream(HTTP_CONNECTION.getOutputStream());
        OUTPUT_STREAM.writeBytes(POST_DATA);
        OUTPUT_STREAM.close();

        // Response Code
        logger.info("CODE response: ".concat(String.valueOf(HTTP_CONNECTION.getResponseCode())));
        
        // Buffer response
        DataInputStream INPUT_STREAM = new DataInputStream(HTTP_CONNECTION.getInputStream());
        while ( (BUFFER_INDEX = INPUT_STREAM.read()) != -1) {
        	JSON_RESPONSE.append((char) BUFFER_INDEX);
        }
        INPUT_STREAM.close();
        
        // Arraylist Links
        JSON_RESPONSE_LINKS = new ArrayList<JsonResponseLink>();
        JSONObject JSON_OBJ_RESPONSE = new JSONObject(JSON_RESPONSE.toString());
        JSONArray JSON_ARRAY = JSON_OBJ_RESPONSE.getJSONArray("links");
        if (JSON_ARRAY != null) { 
    	   for(Object LINK: JSON_ARRAY){
    		   JSON_RESPONSE_LINKS.add(new ObjectMapper().readValue(LINK.toString(), JsonResponseLink.class));
    	   }
    	}
        
        // Log4j
        logger.info("Response: ".concat(JSON_RESPONSE.toString()));
        logger.info("Redirect to approval_url ");
                
        // Return redirect URL
        return ((JsonResponseLink) JSON_RESPONSE_LINKS.get(1)).getHref();
    }
    
    /**
     * Create JSON for request
     * @return Json for request
     */
    public String createJson(){
    	 
    	Map<String, Object> REQUEST_CONFIG = new HashMap<String, Object>();
    	REQUEST_CONFIG.put("javax.json.stream.JsonGenerator.prettyPrinting", Boolean.valueOf(true));
        
    	JsonBuilderFactory JSON_FACTORY = Json.createBuilderFactory(REQUEST_CONFIG);
    	JsonObject JSON_OBJECT   	    = JSON_FACTORY.createObjectBuilder()
    	     .add("intent", "sale")
    	     .add("redirect_urls", JSON_FACTORY.createObjectBuilder()
    	         .add("return_url", URL_RETURN)
    	         .add("cancel_url", URL_CANCEL)
    	         )
    	     .add("payer", JSON_FACTORY.createObjectBuilder()
    	    	 .add("payment_method", "paypal")
    	    	 )
    	     .add("transactions", JSON_FACTORY.createArrayBuilder()
	    		 .add(JSON_FACTORY.createObjectBuilder()
	    				 .add("amount", JSON_FACTORY.createObjectBuilder()
	    				     .add("total", PAYMENT_AMOUNT)
	    				     .add("currency", PAYMENT_CURRENCY)
	    					 )
	    		 )
	    	 )
    	     .build();
    	 
		return JSON_OBJECT.toString();    	 
	}

    /**
     * Set header in http request
     * @param HTTP_MAP_HEADER header parameters
     * @param HTTP_CONNECTION connection to add 
     */
    public void setHeaderRequest(Map<String,String> HTTP_MAP_HEADER,HttpURLConnection HTTP_CONNECTION){
    	for (Entry<String, String> HTTP_SINGLE_HEADER : HTTP_MAP_HEADER.entrySet())
            HTTP_CONNECTION.setRequestProperty(HTTP_SINGLE_HEADER.getKey(),HTTP_SINGLE_HEADER.getValue());
    }
    
}
