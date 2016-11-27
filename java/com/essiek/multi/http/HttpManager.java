package com.essiek.multi.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/*
 * Handles Post and Get Http Interactions
 */
public class HttpManager {
	HttpClient client = HttpClients.createDefault();
	HttpResponse response;
	HttpEntity entity;
	
	public HttpManager () {
		
	}
	
	public void doPost(String requestURL, /*List<NameValuePair> reqParams,*/ 
			String b64) {
		// Set Parameters and other properties
		HttpPost post = new HttpPost(requestURL);
		
		/*
		try {
			StringEntity strEntity = new StringEntity("grant_type=client_credentials");
			post.setEntity(strEntity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		post.setHeader("Authorization", "Basic " + b64);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		
		
		
		// Execute Handle Response
		try {
			response = client.execute(post);
			entity = response.getEntity();
			if (entity != null) {
					System.out.println(EntityUtils.toString(entity));
					System.out.println(entity.getContent());
			}
			
		} catch (IOException | IllegalStateException e) {
			e.printStackTrace();
		}*/

		
	}

}
