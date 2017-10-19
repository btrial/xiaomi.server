package com.btrial.xiaomi.server.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.btrial.xiaomi.server.XiaomiApplication;
import com.btrial.xiaomi.server.entity.ResponseData;

@RestController()
@RequestMapping("/controller")
public class Controller {

	private static final Logger logger = LoggerFactory.getLogger(XiaomiApplication.class);
	
	@Value("${xiaomi.ip}")
    private String ip;
	
	@Value("${xiaomi.token}")
    private String token;
	
	@RequestMapping(method = RequestMethod.GET, value="/vacuum/{command}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseData execute(@PathVariable("command") String command) {
		
		ResponseData responseData = executeInternal(command);
		responseData.setCommand(command);
		
		return responseData;
    }

	private ResponseData executeInternal(String command) {
		
		try {
			if (!isValidCommand(command)) {
				return new ResponseData(false, "Not supported command: " + command + " . Supported commands are: " + validCommands);
			}
			
			String fullComand = "sudo mirobo --ip " + ip + " --token " + token + " " + command;
			
			logger.info("Executing command: " + fullComand);
			
			Process process = Runtime.getRuntime().exec(fullComand);
			int result = process.waitFor();
			
			logger.info("Response from command execution: " + result);
			
			writeErrorOutput(process);
			
			return new ResponseData(result == 0 ? true : false, String.valueOf(result)) ;
		} catch (Exception e) {
			String msg = "Cannot send command: " + command + " to Xiaomi vacuum cleaner";
			logger.error(msg, e);
			return new ResponseData(false, msg + " " + e.getMessage());
		}
	}
	
	private static ArrayList<String> validCommands = new ArrayList<String>();
	{
		validCommands.add("start");
		validCommands.add("pause");
		validCommands.add("stop");
		validCommands.add("home");
		validCommands.add("fanspeed");
		validCommands.add("spot");
		validCommands.add("find");
	}

	private boolean isValidCommand(String command) throws Exception {
		if (validCommands.contains(command.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	private void writeErrorOutput(Process process) throws IOException {
		
		BufferedReader errorReader = new BufferedReader(
		        new InputStreamReader(process.getErrorStream()));
		
		String line;
		while ((line = errorReader.readLine()) != null) {
		    logger.error(line);
		}
		 
		errorReader.close();
	}
	
}
