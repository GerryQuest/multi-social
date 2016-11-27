/**
 * Functions handle interactions that involve
 * -the user profile, such as delete and update
 */

var multiSol = multiSol || {};
multiSol.profile = multiSol.profile || {};

(function () {
	// Delete User Profile
	multiSol.profile.deleteProfile = function (id) {
		window.cefQuery({
			request: "deleteProfile " + id,
			onSuccess: function (response) {
				console.log("DEL DEL ");
				//document.location.reload(true);
				window.location.reload();
			},
			onFailure: function (error_code, error_message) {
				alert("HEY " + error_message);
			}
		});
	};
	
	
	/*
	 * Returns the URL of the required username
	 */
	multiSol.profile.getSocialURL = function (e) {
		var parent = e.target.parentNode;
		var divs = parent.getElementsByTagName("DIV");
		if (divs.length > 0) {
			console.log("DIVS Length " + divs.length);
			for (var i = 0; i < divs.length; i++) {
				
				if (divs[i] === e.target) {
					console.log("get social");
					var spans = parent.getElementsByClassName("social-url");
					console.log("SPANS " + spans.length);
					console.log("i: " + i);
					console.log(spans[i].dataset.social);
					if (spans.length > 0) return spans[i].dataset.social;
					break;  // May not be needed
				}
			}
		}
		return 0;
	};
	
	
	/**
	 * When the user scrolls down towards the bottom of the page
	 * this function is called by multiSol.profile.twitterScroll to load more tweets
	 * 
	 */
	multiSol.profile.showMoreMedia = function (type, data) {
		console.log(data);
		console.log(JSON.stringify(data));
		console.log(data.profile.handle);
		if (type == "twitter") {
			var tweetsTemplate = document.getElementById("load-more-tweets-template").innerHTML;
			var render = Mustache.render(tweetsTemplate, data);
			
			// Add render to DOM
			var listItems = document.getElementById("twitter-content-area").getElementsByTagName("li");
			//var listItems = div.getElementsByTagName("li");
			console.log(listItems.length);
			if (listItems.length > 0) {
//				listItems[listItems.length - 1]
				var div = document.createElement("DIV");
				div.innerHTML = render;
				console.log(render);
				/*console.log(div.children[0].nodeName);
				console.log(listItems[0].parentNode.nodeName);*/
				console.log(div.children[0]);
				// changing i from 0 to 1 means than the last tweet is not repeated
				for (var i = 1; i < div.children.length; i++) {
					listItems[0].parentNode.insertBefore(div.children[i], 
							listItems[listItems.length - 1].nextSibling);
				}
				//listItems[0].parentNode.insertBefore(div.children[0], listItems[listItems.length - 1].nextSibling);
			}
			
			
			twttr.widgets.load(document.getElementById("twitter-content-area")); // render tweets
			multiSol.profile.twitterScroll(); // Enables event listener to load more tweets on scroll
			
		} else if (type == "instagram") {
			
		} else if (type == "youtube") {
			
		}
	};
	
	
	/*
	 * Returns full profile data when selected profile is clicked on the profile list
	 * */
	multiSol.profile.showProfile = function (data) {
		console.log("SHOW PROFILE " + JSON.stringify(data));
//		storedData = data; // profile json is stored in public field
		multiSol.setStoredData(data);
		
		var el = document.getElementById("profile-list");
		
		//  ------------- COULD MAKE THIS INTO A SEPARATE FUNCTION -----------------
		// Update status bar with selected profile details
		var status = document.getElementById("profile-status-bar");
		var statTemp = document.getElementById("status-bar-template").innerHTML;
		status.innerHTML = Mustache.render(statTemp, data);
		
		// Display selected users full profile
		// first check whether its instagram, twitter or tumblr
		var url = data.profile.url;
		console.log(data.profile.handle);
		console.log(url);
		console.log(multiSol.getStoredData().profile.handle);

		if (url.indexOf("instagram") != -1) {
			console.log("instagram detected");
			var profileTmpl = document.getElementById("instagram-user-profile").innerHTML;
		} else if (url.indexOf("youtube") != -1) {
			console.log("ytb detected");
			var profileTmpl = document.getElementById("youtube-vid-template").innerHTML;
		} else if (url.indexOf("twitter") > -1) {
			console.log("twitter detected");
			var profileTmpl = document.getElementById("twitter-timeline-tweets-template").innerHTML;
		}
		
		console.log("YTEGAA");
		var contentDiv = document.getElementById("main-content-area");
		contentDiv.innerHTML = Mustache.render(profileTmpl, data);
		
		if (url.indexOf("twitter.com") > -1 ) {
			multiSol.profile.twitterScroll();
			twttr.widgets.load(document.getElementById("twitter-content-area"));
		}
			
		if (url.indexOf("instagram.com") > -1 )
			instgrm.Embeds.process();
		
		spinner.stop();
		document.getElementById("content-overlay").className = ""; // removes dark overlay
		
	};
	
	/**
	 * Displays a single youtube video in the main window.
	 */
	multiSol.profile.showYoutubeVideo = function (id) {
		var template = document.getElementById("ytb-single-vid-template").innerHTML;
		var contentArea = document.getElementById("main-content-area");
		
		var data = {videoid: id};
		console.log(data.videoid);
		console.log(data.videoid[0].id);
		contentArea.innerHTML = Mustache.render(template, data);
	};
	
	/*
	 * Filters JSON object by name
	 * @returns
	 */
	multiSol.profile.getFilteredJSON = function (Json, filterString) {
		var filterByName = function (obj) {
			if ("name" in obj && typeof(obj.name) === "string" 
				&& obj.name.indexOf(filterString) > -1) {
				return true;
			} else {
				return false;
			}
		};
		var filtered = Json.filter(filterByName);
		return filtered;
	};
	
	/*
	 * Filters profiles by tag names
	 * @returns filted json
	 */
	multiSol.profile.getFilteredJsonTags = function (json, filterString) {
		console.log(json);
		console.log(JSON.stringify(json));
		console.log(json.profiles[0].tags);
		var filterByTags = function (obj) {
			if ("tags" in obj && Array.isArray(obj.tags)
					&& obj.tags.indexOf(filterString) > -1) {
				return true;
			} else {
				return false;
			}
		};
		return json.profiles.filter(filterByTags);
	};
	
	/*
	 * Renders JSON profile list
	 * 
	 */
	multiSol.profile.renderFilteredJSON = function (data) {
		var completed = false;
		var template = document.getElementById("user-profiles-tmpl").html;
		var profiles = document.getElementById("profile-list");
		profiles.innerHTML = Mustache.render(template, data);
		if (profiles.getElementsByClassName("social-url").length > 0) {
			completed = true;
		}
		
		return completed;
		
	};
	
	/*
	 * Uses JSON data to render template with Mustache
	 * @returns rendered boolean
	 */
	multiSol.profile.renderJsonTags = function (data) {
		var rendered = false;
		var template = document.getElementById("search-tags-template").innerHTML;
		var searchList = document.getElementById("search-profile-list");
		console.log(JSON.stringify(data));
		searchList.innerHTML = Mustache.render(template, data);
		if (searchList.getElementsByClassName("returnedTag")) {
			rendered = true;
			console.log(rendered);
		} // Add returnedTag to template
		
		return rendered;
	};
	
	multiSol.profile.filterByName = function (data, filterStr) {
		var json = multiSol.profile.getFilteredJSON(data, filterStr);
		multiSol.profile.renderFilteredJSON(json); 
	};
	
	/*
	 * Initiates filter/search process for a particular tag in a user profile
	 */
	multiSol.profile.filterByTags = function (filterStr) {
		
		window.cefQuery({
			request: "profiles",
			onSuccess: function (response) {
				console.log(filterStr);
				console.log("Response " + response);
				var data = {profiles: JSON.parse(response)};
				console.log("data varialble " + JSON.stringify(data));
				var json = multiSol.profile.getFilteredJsonTags(data, filterStr);
				var data2 = {profiles: json};
				console.log(data2);
				console.log(JSON.stringify(data2));
				/*console.log(data.profiles.)*/
				multiSol.profile.renderJsonTags(data2);
			},
			onFailure: function (error_code, error_message) {
				alert("Error: " + error_code + " " + error_message);
			}
		});
		
		
	};
	
})();






