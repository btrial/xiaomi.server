package com.btrial.xiaomi.server.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.btrial.xiaomi.server.XiaomiApplication;

@RestController()
@RequestMapping("/controller")
public class Controller {

	 private static final Logger logger = LoggerFactory.getLogger(XiaomiApplication.class);
	
	@RequestMapping(method = RequestMethod.GET, value="/vacuum/{command}")
    public boolean start(@PathVariable("command") String command) {
		
		Process process;
		try {
			validateCommand(command);
			process = Runtime.getRuntime().exec("mirobo --ip 192.168.1.25 --token 5a35344262666e526477315149535862 " + command);
			int result = process.waitFor();
			
			logger.info("Response from command execution: " + result);
			
			writeOutput(process);
			
			return result == 0 ? true : false;
		} catch (Exception e) {
			logger.error("Cannot send command: " + command + " to Xiaomi vacuum cleaner", e);
			return false;
		}
    }

	private void validateCommand(String command) throws Exception {
		
		if (command.equalsIgnoreCase("start") 
				|| command.equalsIgnoreCase("stop")
				|| command.equalsIgnoreCase("home")
				|| command.contains("fanspeed ")) {
			
		} else {
			throw new Exception("Not supported command: " + command);
		}
		
	}

	private void writeOutput(Process process) throws IOException {
		
		BufferedReader errorReader = new BufferedReader(
		        new InputStreamReader(process.getInputStream()));
		
		String line;
		while ((line = errorReader.readLine()) != null) {
		    logger.error(line);
		}
		 
		errorReader.close();
	}
	
}
