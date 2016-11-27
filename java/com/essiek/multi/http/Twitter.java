package com.essiek.multi.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.eissek.twitter.errors.TwitterErrorException;
import com.eissek.twitter.timeline.tweets.TimelineTweet;


/**
 * @author Sane
 *
 */
public class Twitter {
	
	private final String url;
	private final String b64;
	private final String username;
	private HttpClient client = HttpClients.createDefault();
//	private HttpClient client = new DefaultHttpClient();
	private HttpResponse response;
	private int count; // Number of tweets to retrieve at a time
	private long maxId;
	private long sinceId;
	private String bearer;
	
	
	// Constructor
	
	public Twitter (String url, String b64, String username) {
		this.url = url;
		this.b64 = b64;
		this.username = username;
		this.maxId = 0;
		this.count = 5;
		this.sinceId = 0;
	}
	
	// Inner class for JSON type conversion
	static class Bearer {
		private String token_type;
		private String access_token;
		
		public Bearer() {
			
		}
		
		public String getToken_type() {
			return token_type;
		}
		public void setToken_type(String token_type) {
			this.token_type = token_type;
		}
		public String getAccess_token() {
			return access_token;
		}
		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}
		
	}
	
	// Inner class for JSON single tweet conversion
	static class SingleTweet {
		private long  cache_age;
		private String url;
		private int height;
		private String provider_url;
		private String provider_name;
		private String author_name;
		private float version;
		private String author_url;
		private String type;
		private String html;
		private int width;
		
		public SingleTweet() {
			
		}
		

		public SingleTweet(long cache_age, String url, int height,
				String provider_url, String provider_name, String author_name,
				float version, String author_url, String type, String html,
				int width) {
			super();
			this.cache_age = cache_age;
			this.url = url;
			this.height = height;
			this.provider_url = provider_url;
			this.provider_name = provider_name;
			this.author_name = author_name;
			this.version = version;
			this.author_url = author_url;
			this.type = type;
			this.html = html;
			this.width = width;
		}


		public String getHtml() {
			return html;
		}
   
	}
	
	/*static class TwitterErrorException extends Exception {

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
		
		
	}*/
	
	
	public String getBearerToken() throws TwitterErrorException {
		String bearerToken = null;
		// Set Parameters and properties
		
		HttpPost post = new HttpPost(url);
		
		try {
			post.setHeader("Authorization", "Basic " + b64);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			StringEntity strEntity = new StringEntity("grant_type=client_credentials");
			post.setEntity(strEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// Execute and Handle Response
		try {
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				
				int code = response.getStatusLine().getStatusCode();
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				String rsp = EntityUtils.toString(entity);
				
				if (code == 200 || code == 304) {
					System.out.println(code);
					System.out.println(rsp);
					
					try {
						Bearer token  = mapper.readValue(rsp, Bearer.class);
						System.out.println(token.access_token);
						bearerToken = token.getAccess_token();
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					throw new TwitterErrorException(rsp);
				}
				
				/*switch (code) {
					case 401:
						System.out.println("Errors found 401");
					case 403:
						System.out.println("Errors found 403");
						Errors errors = mapper.readValue(rsp, Errors.class);
						for (Error err : errors.getErrors()) {
							System.out.println(err.getCode() + " " + err.getMessage());
						}
						
						break;
					default:
						System.out.println(code);
						System.out.println(rsp);
						Gson gson = new Gson();
						Bearer token = gson.fromJson(rsp, Bearer.class);
						
						try {
							Bearer token  = mapper.readValue(rsp, Bearer.class);
							System.out.println(token.access_token);
							bearerToken = token.getAccess_token();
						} catch (JsonGenerationException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}*/
		
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("TOKEN " + bearerToken);
		return bearerToken;
		
	}
	
	
	public List<Long> getUserTimeline(String bearer) throws TwitterErrorException {
		List<Long> tweetIds = new ArrayList<Long>();
		this.bearer = bearer; // set bearer token object field
		
		String url = "https://api.twitter.com/1.1/statuses/user_timeline.json?" 
		+ "screen_name=" +  this.username + "&count=" + this.count;
		
		HttpGet get = new HttpGet(url);
		
		get.setHeader("Authorization", "Bearer " + bearer);
		
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null){
				String rsp = EntityUtils.toString(entity);
				
				
				System.out.println("TIMELINE " + rsp);
				ObjectMapper mapper = new ObjectMapper();
				
				try {
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					int code = response.getStatusLine().getStatusCode();
					
					if (code == 200 || code == 304) {
						List<TimelineTweet> tweets = mapper.readValue(rsp, TypeFactory.defaultInstance().constructCollectionType(List.class, 
								TimelineTweet.class));
						System.out.println(tweets.size());
						for (int i = 0; i < tweets.size(); i++) {
							System.out.println(tweets.get(i).getId());
							
							// ids are added to list to be returned
							tweetIds.add(tweets.get(i).getId());
							// SinceID is the largest tweet id which is usually the first
							if (i == 0) this.sinceId = tweets.get(i).getId();
							
							// Max-id is the last tweet in the group of tweets received
							if (i == tweets.size() - 1) {
								System.out.println("Tweet with the lowest id "+ tweets.get(i).getId());
								this.maxId = tweets.get(i).getId(); // Subtract 1 for optimization
							}
						}
					} else {
						throw new TwitterErrorException(rsp);
					}
					
					/*switch (code) {
						case 401:
						System.out.println("Twitter API Error  401");
						case 403:
							System.out.println("Twitter API Error 403");
							Errors errors = mapper.readValue(rsp, Errors.class);
							for (Error err : errors.getErrors()) {
								System.out.println(err.getCode() + " " + err.getMessage());
							}
							break;
						default:
							List<TimelineTweet> tweets = mapper.readValue(rsp, TypeFactory.defaultInstance().constructCollectionType(List.class, 
									TimelineTweet.class));
							System.out.println(tweets.size());
							for (int i = 0; i < tweets.size(); i++) {
								System.out.println(tweets.get(i).getId());
								
								// ids are added to list to be returned
								tweetIds.add(tweets.get(i).getId());
								// SinceID is the largest tweet id which is usually the first
								if (i == 0) this.sinceId = tweets.get(i).getId();
								
								// Max-id is the last tweet in the group of tweets received
								if (i == tweets.size() - 1) {
									System.out.println("Tweet with the lowest id "+ tweets.get(i).getId());
									this.maxId = tweets.get(i).getId(); // Subtract 1 for optimization
								}
							}
							
					} */
					
					
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return tweetIds;
	}
	
	public List<Long> getUserTimelineAgain(String bearer) throws TwitterErrorException {
		System.out.println(this.maxId + " " + this.count + " " 
	+ this.sinceId + " " + this.username);
		
		List<Long> tweetIds = new ArrayList<Long>();
		String url = "https://api.twitter.com/1.1/statuses/user_timeline.json?"
		+ "screen_name=" +  this.username + "&count=" + this.count + 
		"&max_id=" + this.maxId /*+ "&since_id=" + this.sinceId*/;
		HttpGet get = new HttpGet(url);
		
		// since_id and max_id may not work because there are no new tweets
		// in which case return old tweets
		
		// Should be used to get previous pagesd, maybe have this function called
		// when the used tries to scroll down to see more  old tweets
		
		get.setHeader("Authorization", "Bearer " + bearer);
		
		
			try {
				response = client.execute(get);
				HttpEntity ent = response.getEntity();
				if (ent != null) {
					String rsp = EntityUtils.toString(ent);
					System.out.println("TIMELINE " + rsp);
					
					// Convert entity JSON data to TimelineTweet Object
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					
					int code = response.getStatusLine().getStatusCode();
					if (code == 200 || code == 304) {
						List<TimelineTweet> tweets = mapper.readValue(rsp, TypeFactory.defaultInstance().constructCollectionType(List.class, TimelineTweet.class));
						for (int i = 0; i < tweets.size(); i++) {
							// List of ids will be returned and used to retrieve oEmbed html code
							tweetIds.add(tweets.get(i).getId()); 
							// Since id is the tweet with the largest id
							if (i == 0) this.sinceId = tweets.get(i).getId();
							if (i == tweets.size() - 1) this.maxId = tweets.get(i).getId();
						}
					} else {
						throw new TwitterErrorException(rsp);
					}
					/* switch (code) {
						case 401:
							System.out.println("Errors found 401");
						case 403:
							System.out.println("Errors found 403");
							Errors errors = mapper.readValue(rsp, Errors.class);
							for (Error err : errors.getErrors()) {
								System.out.println(err.getCode() + " " + err.getMessage());
							}
							break;
						default:
							List<TimelineTweet> tweets = mapper.readValue(rsp, TypeFactory.defaultInstance().constructCollectionType(List.class, TimelineTweet.class));
							
							for (int i = 0; i < tweets.size(); i++) {
								// List of ids will be returned and used to retrieve oEmbed html code
								tweetIds.add(tweets.get(i).getId()); 
								
								// Since id is the tweet with the largest id
								if (i == 0) this.sinceId = tweets.get(i).getId();
								
								if (i == tweets.size() - 1) this.maxId = tweets.get(i).getId();
							}
						
					} // Switch */
				} // If ent
			
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return tweetIds;
	};
	
	public String getSingleTweet (/*String bearer,*/ Long tweetid) {
		String html = null;
		/*this.bearer = bearer;*/ // Bearer token need for Authorization
		String url = "https://api.twitter.com/1.1/statuses/oembed.json?id=" +
		tweetid + "&omit_script=true";
		
		HttpGet get = new HttpGet(url);
		get.setHeader("Authorization", "Bearer " + this.bearer);
		
		try {
			response = client.execute(get);
			HttpEntity ent = response.getEntity();
			if (ent != null) {
				String rsp = EntityUtils.toString(ent);
				System.out.println("SINGLE TWEET" + rsp);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				SingleTweet tweet = mapper.readValue(rsp, SingleTweet.class);
				html = tweet.getHtml();
				System.out.println("HTML " + tweet.getHtml());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return html;
	}
	
	public SingleTweet returnSingleTweet (Long tweetid) {
		SingleTweet tweet = null;
		/*this.bearer = bearer;*/ // Bearer token need for Authorization
		String url = "https://api.twitter.com/1.1/statuses/oembed.json?id=" +
		tweetid + "&omit_script=true&hide_thread=true&align=center";
		
		HttpGet get = new HttpGet(url);
		get.setHeader("Authorization", "Bearer " + this.bearer);
		
		try {
			response = client.execute(get);
			HttpEntity ent = response.getEntity();
			if (ent != null) {

				String rsp = EntityUtils.toString(ent);
				System.out.println("SINGLE TWEET" + rsp);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				tweet = mapper.readValue(rsp, SingleTweet.class);
//				html = tweet.getHtml();
				/*System.out.println("HTML " + tweet.getHtml());*/
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return tweet;
	}
	
	public String getTweets(String bearer, List<Long> tweets) {
		String json = null;
		List<SingleTweet> list = new ArrayList<SingleTweet>();
		for (Long tweetId : tweets) {
			/*if (tweetId != null)
				list.add(returnSingleTweet(tweetId));*/
			SingleTweet tweet = returnSingleTweet(tweetId);
			if (tweet != null)
				list.add(tweet);
		}
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			// Convert list of tweets to JSON using ByteArrayOutput stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			mapper.writerWithDefaultPrettyPrinter().writeValue(out, list);
			json = new String(out.toByteArray());
			System.out.println("YES" + json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	public List<String> getTweetEmbedCodeList (String bearer, List<Long> tweets) {
		this.bearer = bearer;
		List<String> htmlEmbedList = new ArrayList<String>();
		for (Long tweetId : tweets) {
			htmlEmbedList.add(getSingleTweet(tweetId));
		}
		
		return htmlEmbedList;
		
	}
	
	public String getTweetEmbedCodeString (String bearer, List<Long> tweets) {
		/*this.bearer = bearer;*/
		String htmlJSON = null;
		
		List<String> list = getTweetEmbedCodeList(bearer, tweets);
		ObjectMapper mapper = new ObjectMapper();
//		mapper.writerWithType(new TypeReference<List<String>>(){}).writeValueAsString();
		try {
			htmlJSON = mapper.writeValueAsString(list);
			

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return htmlJSON;
	}
	
	
	
	public String getUsername() {
		return username;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getMaxId() {
		return maxId;
	}

	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}

	public long getSinceId() {
		return sinceId;
	}

	public void setSinceId(int sinceId) {
		this.sinceId = sinceId;
	}
	
	
	
	
	
	
	

}
