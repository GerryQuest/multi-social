package com.eissek.twitter.errors;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TwitterErrorException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public TwitterErrorException (String rsp) {
		super("Twitter API Error");
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			Errors errors = mapper.readValue(rsp, Errors.class);
			for (Error err : errors.getErrors()) {
				System.out.println(err.getCode() + " " + err.getMessage());
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
