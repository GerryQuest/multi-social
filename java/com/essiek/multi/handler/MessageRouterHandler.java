package com.essiek.multi.handler;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
//import java.util.Base64;
import org.apache.commons.codec.binary.Base64;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.callback.CefURLRequestClient;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import org.cef.network.CefRequest;
import org.cef.network.CefURLRequest;

import com.essiek.multi.Auth;
import com.eissek.twitter.errors.TwitterErrorException;
import com.essiek.multi.http.HttpManager;
import com.essiek.multi.http.Twitter;
import com.essiek.multi.model.UserProfile;
import com.essiek.multi.users.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MessageRouterHandler extends CefMessageRouterHandlerAdapter {
	private final CefMessageRouter router = null;
	private UserManager manager;
	private Twitter twitter;
	/*public MessageRouterHandler (final CefMessageRouter msgrouter) {
		router = msgrouter;
	}*/
	
	@Override
	public boolean onQuery (CefBrowser browser, long query_id, String request,
							boolean persistent, CefQueryCallback callback) {
		
		/*browser.executeJavaScript("multiSol.ping()", browser.getURL(), 0);*/
		/*File file = new File("src/main/resources/index.html");
		CefRequest req = CefRequest.create();
		req.setURL(file.getAbsolutePath());
		browser.loadRequest(req);*/
		
		/*CefURLRequest r = CefURLRequest.create(arg0, arg1);
		r.getResponse().*/
		
		
		
		if (request.startsWith("cefTest")) {
			// Adds new user
			
//			if (router != null) {
				System.out.println(request.substring(8));
				System.out.println("success");
				Gson gson = new Gson();
				UserProfile user = gson.fromJson(request.substring(7), UserProfile.class);
				manager = new UserManager();
				/*user.setAccordionID(user.getName());*/
				ArrayList<String> sm = (ArrayList<String>) user.getSocialMedia();
				System.out.println("SM Size " + sm.size() + " " + sm.get(0));
				System.out.println("URL " + user.getSocialMediaURL().size() + " first " + user.getSocialMediaURL().get(0));
				user.setsocialMediaHandle(manager.extractMultiUsers(sm));
				/*System.out.println(user.getTags());*/
				System.out.println("Accordion " + user.getAccordionID());
				
				// Finally Store POJO in database
				String id = manager.addUser(user.getName(), user.getLocation(), 
						user.getSocialMedia(), user.getTags(),
						user.getSocialMediaHandle(), user.getSocialMediaURL());
				
				if (id != null) {
					user.setId(id);
					// User is JSON data is needed by Javascript to update the DOM
					String userJSON = gson.toJson(user);
					if (userJSON instanceof String)
						callback.success(userJSON);
					else
						callback.failure(0, "Failed to convert and return used JSON");
				} else {
					callback.failure(0, "Failed to add user");
				}
				

		} else if (request.startsWith("profiles")) {
			// Returns all profiles
			
			Gson gson = new Gson();
			UserManager manager = new UserManager();
			/*manager.deleteProfile("79db1e73-44c0-4eb5-808a-4a4cfb6a46cc");*/
			Type type = new TypeToken<List<UserProfile>>(){}.getType();
			/*System.out.println(manager.initQuery().toString());*/
			String json = gson.toJson(manager.initQuery(), type);
			if (json instanceof String) {
				System.out.println("JSON " + json);
				callback.success(json);
			} else 
				callback.failure(0, "Failure, could not retrieve profiles");
			
			
			
			
		} else if (request.startsWith("getProfile")) {
					// Fetch profile by id and return it as json
					
					String id = request.substring(11);
					System.out.println("ID IS " + id);
					Gson gson = new Gson();
					UserManager manager = new UserManager();
					UserProfile profile = manager.getUserProfile(id);
					if (!profile.getName().isEmpty()) {
						System.out.println("SUCCESS");
						callback.success(gson.toJson(profile));
					} else
						callback.failure(0, "Failed to get profile. Try Again.");
			
			
			
			
		} else if (request.startsWith("deleteProfile")) {
			
			String id = request.substring("deleteProfile".length() + 1);
			System.out.println("Deleted " + id);
			UserManager manager = new UserManager();
			if (manager.deleteProfile(id)) {
				callback.success("ssd");
			} else
				callback.failure(0, "failed");
			
			
		} else if (request.startsWith("addFeed")) { // Add A single Feed to selected profile
			
			String id = request.substring("addFeed".length() + 1, request.indexOf("feedURL") - 1);
			String feed = request.substring(request.indexOf("feedURL") + "addFeed".length());
			feed = feed.replaceAll(" ", "");
			
			System.out.println("ID: " + id + " feed is " + feed);
			UserManager manager = new UserManager();
			boolean updated = manager.updateProfile(id, feed);
			if (updated) {
				UserProfile profile = manager.getUserProfile(id);
				if (profile.getId() != null) {
					System.out.println("PROFILE IS NOT NULL");
					System.out.println(profile.getId());
					System.out.println(profile.getSocialMediaURL().toString());
					System.out.println(profile.getSocialMediaHandle().toString());
					// Uses last handle so javascript code wont need to iterate through handle list
					String addHandle = profile.getSocialMediaHandle()
							.get(profile.getSocialMediaHandle().size() -1);
					List<String> handles = new ArrayList<String>();
					handles.add(addHandle);
					profile.setsocialMediaHandle(handles);
					
					Gson gson = new Gson();
					callback.success(gson.toJson(profile));
				} else
					callback.failure(0, "Failed to return updated profile");
				
			} else
				callback.failure(0, "Failed to update profile");
			
		} else if(request.startsWith("deleteFeed")) { // Remove Single feed from profile
			String id = request.substring("deleteFeed".length() + 1, request.indexOf("feedURL"));
			String feed = request.substring(request.indexOf("feedURL") + "feedURL".length());
			System.out.println(id);
			System.out.println(feed.replaceAll(" ", ""));
			
			UserManager manager = new UserManager();
			if (manager.deleteFeed(id, feed.replace(" ", ""))) {
				System.out.println("FEED DELETE Success");
				callback.success("true");
			} else {
				callback.failure(0, "Error Deleting feed");
			}
			
		} else if (request.startsWith("getBearerToken")) {
//			String b64 = request.substring("getBearerToken".length(), request.indexOf("requestUser"));
			String url = "https://api.twitter.com/oauth2/token"; 
			String b64 = Base64.encodeBase64String( new String (
					Auth.getTwitKey() + ":" + Auth.getTwitSec()).getBytes());
			String username = request.substring(request.indexOf("requestUser") + "requestUser".length());
//			System.out.println("GETTING BEARER " + b64 + " " + username);
			
			// Maybe make twitter static so there's only one
			twitter = new Twitter(url, b64, username);
			
			try {
				String token = twitter.getBearerToken();
				if (token != null) {
					List<Long> tweetIds = twitter.getUserTimeline(token);
//					List<String> html = twitter.getTweetEmbedCodeList(token, tweetIds);
//					String js = twitter.getTweetEmbedCodeString(token, tweetIds);
					String js = twitter.getTweets(token, tweetIds);
					if (js != null) {
						System.out.println("SOmething " + js);
						callback.success(js);
					} else {
						callback.failure(0, "Problem getting tweets");
					}
				} else {
					callback.failure(0, "Failure to get bearer token");
				}
			} catch (TwitterErrorException e) {
				// TODO Auto-generated catch block
				callback.failure(0, "Failure to get bearer token");
				e.printStackTrace();
			}
			
			
		} else if (request.startsWith("getMoreTweets")) { 
//			String b64 = request.substring("getMoreTweets".length(), request.indexOf("requestUser"));
			String username = request.substring(request.indexOf("requestUser") + "requestUser".length());
			String url = "https://api.twitter.com/oauth2/token";
			String b64 = Base64.encodeBase64String( new String (
					Auth.getTwitKey() + ":" + Auth.getTwitSec()).getBytes());
			try {
				String token = new Twitter(url, b64, username).getBearerToken();
				if (token != null) {
					List<Long> listOfIds = twitter.getUserTimelineAgain(token);
					System.out.println(listOfIds.size());
					String js = twitter.getTweets(token, listOfIds);
					if (js != null) {
						System.out.println("SOmething " + js);
						callback.success(js);
					} else {
						callback.failure(0, "Problem getting tweets");
					}
				}
			} catch (TwitterErrorException e) {
				// TODO Auto-generated catch block
				callback.failure(0, "Problem getting tweets");
				e.printStackTrace();
			}
			
			
		} else if (request.startsWith("getAuth")) {
			String sub = request.substring("getAuth".length());
			System.out.println("SUB IS: " + sub);
			String value = null;
			switch (sub) {
				case "TWITKEY":
					value = Auth.getTwitKey();
					System.out.println("twitkey: " + value);
					break;
				case "TWITSEC":
					value = Auth.getTwitSec();
					System.out.println("sec: " + value);
					break;
				case "YTBKEY":
					value = Auth.getYtbKey();
					System.out.println("yetb");
					break;
				case "INSTAID":
					value = Auth.getInstaID();
				default: System.out.println("reached default");
			}
			
			if (value == null)
				callback.failure(0, "Could not return code");
			
			callback.success(value);
			
		} else {
			callback.failure(0, "");
			return false;
		}
	
		return true;
	}
}


