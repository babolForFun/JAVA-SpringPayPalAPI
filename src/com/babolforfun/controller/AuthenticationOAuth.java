package com.babolforfun.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;


/**
 * Controller oAuth2.0
 * @author babolForFun
 *
 */
@Controller  
public class AuthenticationOAuth {

	// Log4j
	private static final Logger logger = Logger.getLogger(AuthenticationOAuth .class.getName());
	
	// Credential
	private String CLIENT_ID 		= "[YOUR_CLIENT_ID]";
	private String CLIENT_SECRET 	= "[YOUR_CLIENT_SECRET]";
    
    // URL
	private String URL_FIRST_OAUTH 	= "https://api.sandbox.paypal.com/v1/oauth2/token";
    
    /**
     * Get access token
     * @return
     * @throws IOException
     */
    @RequestMapping("/accesstoken")  
    public ModelAndView getAccessToken() throws IOException {  
    	
    	String ACCESS_TOKEN = getValidAccessToken();

        return new ModelAndView("accesstoken", "accessToken", ACCESS_TOKEN);  
    }  
    
    /**
     * Get User credential for request header
     * @return encoded string clientid:clientsecret
     */
    public String getUserCredential(){
        String USER_CREDENTIAL  = CLIENT_ID
        							.concat(":")
        							.concat(CLIENT_SECRET);
        return 	"Basic ".concat(new String(new Base64().encode(USER_CREDENTIAL.getBytes())));
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
    
    /**
     * Set request payload
     * @param HTTP_MAP_PAYLOAD elements
     * @param HTTP_CONNECTION connection
     * @throws IOException write data
     */
    public void setPostdataRequest(Map<String,String> HTTP_MAP_PAYLOAD,HttpURLConnection HTTP_CONNECTION) throws IOException{

    	// Create URL payload
    	String POST_STRING = "";
    	for (Entry<String, String> entry : HTTP_MAP_PAYLOAD.entrySet())
    		POST_STRING += entry.getKey()+"="+entry.getValue()+"&";
    	
    	// Byte post data
    	byte[] POST_DATA       = POST_STRING.substring(0,POST_STRING.length()-1).getBytes( StandardCharsets.UTF_8 );
    	
    	// Write post data
    	HTTP_CONNECTION.setRequestProperty( "Content-Length", Integer.toString( POST_DATA.length ));
    	try( DataOutputStream wr = 
    			new DataOutputStream( HTTP_CONNECTION.getOutputStream())) {
    		   		wr.write( POST_DATA );
    			}
    }
    
    
    
    /**
     * Get valid access token oAuth2.0
     * @return access token
     * @throws IOException
     */
    public String getValidAccessToken() throws IOException {

    	// Variables
        StringBuilder BUFFER_RESPONSE = new StringBuilder();
        int BUFFER_INDEX;

    	// Connection
        URL HTTP_URL = new URL(URL_FIRST_OAUTH );
        HttpURLConnection HTTP_CONNECTION = (HttpURLConnection) HTTP_URL.openConnection();

        // Set HTTP attribute
        HTTP_CONNECTION.setRequestMethod("POST");
        HTTP_CONNECTION.setInstanceFollowRedirects(true);
        HTTP_CONNECTION.setDoOutput(true);
        HTTP_CONNECTION.setDoInput(true);
        
        // Header
        Map<String,String> HEADERS = new HashMap<>();
        HEADERS.put("Accept","application/json");
        HEADERS.put("Accept-Language","en_US");
        HEADERS.put("Authorization",getUserCredential());
        setHeaderRequest(HEADERS,HTTP_CONNECTION);
                
        // Post data
        Map<String,String> POST_DATA = new HashMap<>();
        POST_DATA.put("grant_type","client_credentials");
        setPostdataRequest(POST_DATA,HTTP_CONNECTION);

        // Response Code
        logger.info("CODE response: ".concat(String.valueOf(HTTP_CONNECTION.getResponseCode())));

        // Buffer response
        DataInputStream INPUT_STREAM = new DataInputStream(HTTP_CONNECTION.getInputStream());
        while ( (BUFFER_INDEX = INPUT_STREAM.read()) != -1) {
        	BUFFER_RESPONSE.append((char) BUFFER_INDEX);
        };
        INPUT_STREAM.close();
        
        // Get Map<> from JSON response
        HashMap<String, Object> map = jsonToMap(BUFFER_RESPONSE.toString());
        logger.info("Access token: ".concat(map.get("access_token").toString()));
     
        // Return
        return map.get("access_token").toString();
    }
    
    
    /**
     * Convert JSON string to MAP object
     * @param jsonString String to convert
     * @return Map object as JSON
     * @throws JSONException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static HashMap<String, Object> jsonToMap(String jsonString) throws JSONException, JsonParseException, JsonMappingException, IOException {

    	// Variables
    	JsonFactory factory = new JsonFactory(); 
        ObjectMapper mapper = new ObjectMapper(factory); 
        TypeReference<HashMap<String,Object>> typeRef 
                = new TypeReference<HashMap<String,Object>>() {};

    	return mapper.readValue(jsonString, typeRef);
        
    }
}
