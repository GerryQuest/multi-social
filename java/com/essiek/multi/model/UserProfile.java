package com.essiek.multi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class UserProfile {
	private String name;
	private String accordionID; // Replaces spaces with hyphens, foundation js has issues with spaces
	private String location;
	private Object socialMedia;
	/*private String[] tags;*/
	private List<String> tags = new ArrayList<String>();
	private String id;
	private List<String> socialMediaHandle = new ArrayList<String>(); 
	private List<String> socialMediaURL = new ArrayList<String>();
	
	public UserProfile() {
		/*this.id = UUID.randomUUID().toString();*/
	}
	
	
	/**
	 * @param name
	 * @param location
	 * @param socialMedia
	 * @param tags
	 * @param id;
	 */
	public UserProfile(String name, String location, String[] socialMedia,
			List<String> tags, String id) {
		super();
		this.name = name;
		this.location = location;
		this.socialMedia = socialMedia;
		this.tags = tags;
		this.id = id;
		/*this.id = UUID.randomUUID().toString();*/
		setAccordionID(name);
		// Why not use socialMedia ID instead of name?????
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		System.out.println("NANANANAME IS " + name);
		setAccordionID(name);
	}
	
	
	
	public String getAccordionID() {
		return accordionID;
	}


	public void setAccordionID(String accordionID) {
		//May check if it contains space before applying
		if (accordionID.indexOf(" ") != -1) {
			this.accordionID = accordionID.replaceAll(" ", "-");
		} else {
			this.accordionID = accordionID;
		}
		
	}


	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	/*public String[] getSocialMedia() {
		return socialMedia;
	}


	public void setSocialMedia(String[] socialMedia) {
		this.socialMedia = socialMedia;
	}*/

	
	
	public void setSocialMedia(Object arg) {
		// TODO Auto-generated method stub
		this.socialMedia =  arg;
		
	}
	
	public Object getSocialMedia() {
		return socialMedia;
	}


	public List<String> getTags() {
		return tags;
	}


	public void setTags(List<String> tags) {
		this.tags = tags;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
	

	


	public List<String> getSocialMediaHandle() {
		return socialMediaHandle;
	}


	public void setsocialMediaHandle(List<String> socialMediaHandle) {
		this.socialMediaHandle = socialMediaHandle;
	}


	/*@Override
	public String toString() {
		return "UserProfile [name=" + name + ", location=" + location
				+ ", socialMedia=" + socialMedia + ", tags="
				+ Arrays.toString(tags) + ", id=" + id + ", socialMediaHandle="
				+ socialMediaHandle + "]";
	}*/


	@Override
	public String toString() {
		return "UserProfile [name=" + name + ", accordionID=" + accordionID
				+ ", location=" + location + ", socialMedia=" + socialMedia
				+ ", tags=" + tags + ", id=" + id
				+ ", socialMediaHandle=" + socialMediaHandle
				+ ", socialMediaURL=" + socialMediaURL + "]";
	}


	public List<String> getSocialMediaURL() {
		return socialMediaURL;
	}



	public void setSocialMediaURL(List<String> socialMediaURL) {
		this.socialMediaURL = socialMediaURL;
	}


	
	


	

	
	/*public String getSocialMedia() {
		return socialMedia;
	}
	public void setSocialMedia(String socialMedia) {
		this.socialMedia = socialMedia;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}*/
	
	
	
	

}
