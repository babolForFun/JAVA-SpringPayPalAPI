package com.babolforfun.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller after payment
 * @author babolForFun
 *
 */
@Controller
@RequestMapping("/process")
public class CallbackController {

	// Log4j
	private static final Logger logger = 
			Logger.getLogger(CallbackController .class.getName());
    
	/**
	 * Payment success
	 * @param request
	 * @param res
	 * @return
	 */
    @RequestMapping(value = "/success", method = RequestMethod.GET)  
    public ModelAndView createPayment(HttpServletRequest request,HttpServletResponse res){  
    	
        logger.info("Load callback success");
    	ModelAndView returnView = new ModelAndView("payment_success");
    	return returnView;
    }  
    
    /**
     * Payment canceled
     * @param request
     * @param res
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.GET)  
    public ModelAndView creaatePayment(HttpServletRequest request,HttpServletResponse res){  
    	
    	logger.info("Load callback canceled");
    	ModelAndView returnView = new ModelAndView("payment_cancel");
    	return returnView;
    }  


}
