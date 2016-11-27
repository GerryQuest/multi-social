/**
 * Main JavaScript file for handling interactivity for html page
 */

var multiSol = multiSol || {};
multiSol.form = multiSol.form || {};
multiSol.MsgRouter = multiSol.MsgRouter || {};
multiSol.profile = multiSol.profile || {};
multiSol.instagram = multiSol.instagram || {};
multiSol.callback = multiSol.callback || {};
multiSol.search = multiSol.search || {};
multiSol.auth = multiSol.auth || {};

var opts = {
		color: "#fff",
		opacity: 0.30,
		scale: 1.20
};
var spinner = new Spinner(opts); // Loading spinner

(function () {
	var storedData = null; // User Profile JSON /** MAYBE HAVE THIS in function withg a getter function **/
	var socialMediaObj = "";  // Needed by Callback.js
	var twitterObj = ""; // Twitter Instance Object
	
	multiSol.getStoredData = function () {
		return storedData;
	};
	multiSol.setStoredData = function (data) {
		storedData = data;
	};
	
	
	/*
	 * getter for socialMediaObj
	 */
	multiSol.getSocialMediaObj = function () {
		return socialMediaObj;
	};

	
	multiSol.showLeftMenu = function () {
		$('.off-canvas-wrap').foundation('offcanvas', 'show', 'offcanvas-overlap-right');
	};
	
	
	multiSol.searchMenu = function () {
		var enabled = false;
		var menu = document.getElementById("search-menu");
		if (menu) {
				menu.addEventListener("click", function (e) {
					$('.off-canvas-wrap').foundation('offcanvas', 'toggle', 'offcanvas-overlap-left');
					e.preventDefault();
					
				}, false);
				enabled = true;
		}
		return enabled;
	};
	
	multiSol.closeRightMenu = function () {
		var enabled = false;
		var exitRightMenu = document.getElementById("exit-right-menu");
		if (exitRightMenu) {
			exitRightMenu.addEventListener("click", function (e) {
				$('.off-canvas-wrap').foundation('offcanvas', 'toggle', 'offcanvas-overlap-left');
				e.PreventDefault;
				console.log("Whshs");
				var icon = document.getElementById("menuIcon");
				icon.className = "icon-right-open";
				
			}, false);
			enabled = true;
		}
		return enabled;	
	};
	
	
	multiSol.toggleRightMenu = function () {
		var enabled = false;
		var searchMenu = document.getElementById("search-menu");
		if (searchMenu) {
				searchMenu.addEventListener("click", function(e) {
					var icon = document.getElementById("menuIcon");
					if (icon.className == "icon-right-open")
						icon.className = "icon-down-open";
					 else
						icon.className = "icon-right-open";
					
					
					e.preventDefault();
				}, false);
				enabled = true;
		}
		return enabled;
	};
	
	
	
	
	
	multiSol.profile.searchButton = function () {
		/*var button = document.getElementById("searchButton");*/
		var searchForm = document.getElementById("searchForm");
		var searchClicked = function (e) {
			// Should stop the browser from initiating event when button is clicked
			e.preventDefault();  
		};
		/*button.addEventListener("click", searchClicked, false);
		button.addEventListener("keydown", searchClicked, false);*/
		searchForm.onsubmit = function () {
			return false;
		};
	};
	
	/*
	 * Searches/Filters User list
	 */
	multiSol.enterSearch = function () {
		multiSol.profile.searchButton();
		var enabled = false;
		var searchBox = document.getElementById("search-box");
		if (searchBox) {
			multiSol.search.profileSelection();
				searchBox.addEventListener("keyup", function(e) {
					var icon = document.getElementById("menuIcon");
					if (!searchBox.value.length == 0 && (e.keyCode == 13 || e.which == 13)) {
						$('.off-canvas-wrap').foundation('offcanvas', 'show', 'offcanvas-overlap-left');
						if (icon.className == "icon-right-open")
							icon.className = "icon-down-open";
						
						multiSol.profile.filterByTags("actress");
						
						// Should AlSO return menu list back to normal ****/////
					} else {
						icon.className = "icon-right-open";
						$('.off-canvas-wrap').foundation('offcanvas', 'hide', 'offcanvas-overlap-left');
					}
					enabled = true;
					e.preventDefault();
				}, false);
		}
		
		return enabled;
	};
	
	/*
	 * @Params doc, classname
	 * Removes class (classname) from given elements (doc)
	 */
	multiSol.removeClass = function (doc, classname) {
		var el = doc.getElementsByClassName(classname);
		for (var i=0; i < el.length; i++) {
			console.log(el[i].className);
			el[i].className.replace(classname, "");
		}
		
	};
	
	/*
	 * 
	 * gets the Profile of selected User in search results
	 */
	multiSol.search.selectSocialMedia = function (div) {
		var span = div.getElementsByTagName("span");
		/*var id = span[0].dataset.id;
		var url = span[0].dataset.url;*/
		var names = div.getElementsByTagName("h3");
		var block = div.getElementsByClassName("small-block-grid-1")[0];// The div the loading spinner will be part of
		
		var userHandleClick = function (e) {
			var target = e.target;
			for (var i = 0; i < span.length; i++) {
				if (target.nodeName.toLowerCase() == "h3" &&
						target == names[i]) {
					console.log("YAAAAAAH");
					var id = span[i].dataset.id;
					var url = span[i].dataset.url;
					var name = names[i].innerText;
					
					multiSol.MsgRouter.getProfile(id, block, name, url)
					
					break;
					
				}
			}
			
			
			console.log(id + " " + url + " " + name);
//			multiSol.MsgRouter.getProfile(id, block, name, url);
			div.removeEventListener("click", userHandleClick, false);
		};
		div.addEventListener("click", userHandleClick, false);
	};
	
	/*
	 * Renders user profile as a search result
	 */
	multiSol.search.renderProfile = function (profile) {
		var template = document.getElementById("user-search-template").innerHTML;
		var main = document.getElementById("main-content-area");
		main.innerHTML = Mustache.render(template, profile);
		console.log("render profile");
		multiSol.search.selectSocialMedia(main); // Add Event Listener for clicking on user handle
		
	};
	
	/*
	 * Creates a Profile JSON using the given parameters
	 * @returns JSON
	 */
	multiSol.search.createProfileJSON = function (name, id, handles, urls) {
		console.log(handles);
		/*console.log(JSON.stringify(handles));*/
		var userHandles = handles;
		userHandles = handles.split(",");
		console.log(JSON.stringify(userHandles));
		var socialURL = urls.split(",");
		var profileJson = {profile:[{"name": name, 
			"id": id,
			"social": [{"socialMediaHandle": userHandles,"socialMediaURL": socialURL}]
		}]};

		console.log(JSON.stringify(profileJson));
		return profileJson;
	};
	
	/*
	 * Highlights/Unhighlights selected profile in search results list
	 * Also invokes renderProfile function on selected profile 
	 */
	multiSol.search.profileSelection = function () {
		var searchList = document.getElementById("search-profile-list");
		var toggleSelection = function (e) {
			multiSol.removeClass(searchList, "highlightTag");
			var target = e.target;
			if (target.nodeName.toUpperCase() == "A") {
				
				if (target.className.indexOf("highlightTag") > -1) {
					target.className = "";
				} else {
					target.className = "highlightTag";
					console.log(target.dataset.id);
					/*console.log(JSON.stringify(target.dataset.handle).split(","));*/
					var data = multiSol.search.createProfileJSON(target.dataset.name, 
							target.dataset.id, target.dataset.handle, target.dataset.url);
					console.log(data);
					multiSol.search.renderProfile(data);
				}
				e.preventDefault();	
			}
		};
		searchList.addEventListener("click", toggleSelection, false);
	};
	
	

	
	
	multiSol.deleteFeed = function (id, feed) {
		var feedURL = feed[0].parentNode.getElementsByClassName("social-url");
		console.log("feeds " + feedURL.length);
		var divs = feed[0].parentNode.getElementsByTagName("DIV");
		if (divs) {
				for (var i = 0; i < divs.length; i++) {
					if (divs[i] == feed[0]) {
						console.log("MATCH MADE " + i);
						if (feedURL.length >= i) {
							console.log("LENGTH is fine");
							console.log(feedURL[i].dataset.social);
							
							window.cefQuery({
								request: "deleteFeed " + id + "feedURL" + feedURL[i].dataset.social,
								onSuccess: function (response) {
									console.log(response);
									// Remove both span data and div from DOM
									feed[0].parentNode.removeChild(feed[0]); 
									feedURL[i].parentNode.removeChild(feedURL[i]);
								},
								onFailure: function (error_code, error_message) {
									alert(error_message);
								}
							});
						}
						break;
					}
				}
		}
		
		
	};
	
	/* Calls correct function depending of event source */
	multiSol.eventDelegation = function () {
		var wrap = document.getElementById("the-wrap");
		if (wrap) {
					wrap.addEventListener("click", function (e) {
		
						if (e.target.id == "deleteUserProfile") {
							var profiles = document.getElementsByClassName("selected");
							console.log("Number of selected Profiles " + profiles.length);
							if (profiles.length > 0 && profiles[0].nodeName.toUpperCase() == "LI") {
								console.log("IN IN");
								var profile = profiles[0].getElementsByTagName("A")[0].text;
								if (confirm("Delete " + profile)) {
									console.log("found one");
									console.log(profiles[0].getElementsByTagName("A")[0].dataset.id);
									var id = profiles[0].getElementsByTagName("A")[0].dataset.id;
									multiSol.profile.deleteProfile(id);
								}
								//console.log(profiles[0].childNodes[1].dataset.id);
							} else 
								console.log("No Profile has been selected.");
							
							
						} else if (e.target.nodeName.toUpperCase() == "A" && 
								e.target.parentNode.parentNode.id == "profile-list") {
							
							// Higlights a selected row in the profile list
							// Could probably abstract this into its own function
							
							console.log("profile list target");
							console.log("Target node " + e.target.nodeName);
							console.log("Parent " + e.target.parentNode.nodeName);
							var nodeA = e.target;
							var nodeLi = nodeA.parentNode;
							
							// Checks to see if any other elements are selected
							// If so then removes selected from class tags
							var selected = document.getElementsByClassName("selected");
							if (selected.length > 0) {
								for (var i=0; i < selected.length; i++) {
									if (selected[i] == nodeLi) {
										console.log("break");
										
										continue;
									}
									console.log("Select Node " + i);
									selected[i].className = "accordion-navigation"; 
								}
							}
							
							// Checks LI does not have active in its class list
							if (nodeLi.className.indexOf("selected") == -1) {
		//						console.log(target.parentNode.className);
								nodeLi.className += " selected";
								console.log(nodeLi.className);
							} else { 
								// Else May not be needed since the selected classes
								// - selected classes are all removed up above
								nodeLi.className = "accordion-navigation";
								console.log(nodeLi.className);
								console.log(" SELCTED " + document.getElementsByClassName("selected").length);
								
							}
							e.preventDefault();
								
						} else if (e.target.parentNode.className.indexOf("accordion-navigation") != -1 &&
								e.target.className.indexOf("content") != -1) { 
							// ********* Toggles Highlight on text in profile list ***********/
		
							// Sets class name back to default by removing selected2 class
							if (e.target.className.indexOf("selected2") != -1) {
		//							e.target.className += " selected2";
								console.log("not not" + e.target.className);
								e.target.className = "content active";
								
							} else {
								
								/** Highlight Selected Profile in menu by adding "selected2" to class list */
								var el = e.target.parentNode.parentNode.getElementsByClassName("selected2");
								console.log("ELLL " + el.length);
								
								// Clears any of the other highlighted menu items by
								// clearing selected2 from other classes as a precaution
								for (var i = 0; i < el.length; i++) {
									console.log("REPLACE " + el[i].className.replace(" selected2", ""));
									el[i].className = el[i].className.replace(" selected2", "");
								}
								
								e.target.className += " selected2"; // Highlights selected item
								var parent = e.target.parentNode;
								var id = parent.getElementsByTagName("A")[0].dataset.id;
								var target = document.getElementById("main-content-area").getElementsByClassName("small-block-grid-2")[0];
								var handle = e.target.innerText;
								console.log(e.target.nodeName);
								console.log(e.target.innerText);
								console.log(e.target.className);
								var url = multiSol.profile.getSocialURL(e);
								console.log(url);
								/*var user = e.target.innerText;*/
								console.log("ID is :" + id);
								multiSol.MsgRouter.getProfile(id, target, handle, url); // Retrives selected profile
								
							}
								
							
						} else if (e.target.className.indexOf("ytb-thumb") > -1) { // Click on Ytb video
							console.log("CLicke dhtis");
							console.log(e.target.dataset.videoid);
							multiSol.profile.showYoutubeVideo(e.target.dataset.videoid);
							
							
						} else if (e.target.id == "deleteFeed") { // Remove a media feed from the current profile
							var feed = document.getElementsByClassName("selected2");
							if (feed.length > 0 && feed[0].nodeName.toUpperCase() == "DIV") {
								var id = feed[0].parentNode.getElementsByTagName("A")[0];
								/*var id = feed[0].previousSibling;*/
								/*multiSol.deleteFeed(id.dataset.id, feed[0].dataset.social);*/
								multiSol.deleteFeed(id.dataset.id, feed);
								console.log("Deleted " + id.dataset.id);
		//						console.log("div value " + feed[0].dataset.social);
							}
							
							
						} else if (e.target.id == "showAddFeedForm") { // Display the add feed form
							var profile = document.getElementById("profile-list");
							if (profile.getElementsByClassName("selected").length > 0) {
								/*e.target.dataset.revealid = "addFeedModal";
								console.log("yeahahaha");*/
								$("#addFeedModal").foundation("reveal", "open");
								multiSol.form.addFeedEventListener();
								
								// remove listener on close
							} else {/* e.preventDefault();*/}
						}
					}, false);
		}
		
	};
	
	// Listener for instagram scroll
	multiSol.profile.twitterScroll = function () {
		console.log("In Twitter scroll");
		var twitterDiv = document.getElementById("twitter-content-area");
		
		if (twitterDiv !== null || twitterDiv !== undefined) {
			console.log("Twitter div is not null");
			var loadMoreTweets = function () {
				// Get More tweets if twitter instance is not empty
				if (twitterObj != "") {
					console.log(storedData.profile.handle);
					twitterObj.getMoreTweets(storedData.profile.handle, storedData);
					twitterDiv.removeEventListener("scroll", checkScrollbarPostion, false);
				}
			};
			var checkScrollbarPostion = function () {

				if (twitterDiv.offsetHeight + twitterDiv.scrollTop >= 
					twitterDiv.scrollHeight * 0.90 && 
					twitterDiv.offsetHeight + twitterDiv.scrollTop <=
						twitterDiv.scrollHeight * 0.92) {
					//needs to be called once
					console.log("Scrolled 75 percent of the way");
					loadMoreTweets();
				}
			};
			
			twitterDiv.addEventListener("scroll", checkScrollbarPostion, false);
		}
		
	};
	
	//Add feed to user profile
	multiSol.addFeed = function (feed) {
		var profile = document.getElementsByClassName("selected");
		if (profile.length > 0) {
			//Id of document is needed to update the database document
			var id = profile[0].getElementsByTagName("A")[0].dataset.id; 
			console.log(id);
			
			window.cefQuery({
				request: "addFeed " + id + " feedURL" + feed.value,
				onSuccess: function (response) {
					var template = document.getElementById("profile-single-feed").innerHTML;
					var data = {profiles: JSON.parse(response)};
					console.log(response);
					
					var selected = document.getElementsByClassName("selected");
					
					/** could probably put all this in its own section ***/
					
					if (selected.length > 0 && selected[0].nodeName == "LI") {
						/*if (selected[0].childNodes.length > 0) {
							console.log("CHILD NODES");
							for (var i = 0; i < selected[0].childNodes.length; i++) {
								
							}
						}*/
						console.log("Selected is " + selected[0].nodeName + " " + selected[0].className + " " + selected[0].innerText);
						var feedDivs = selected[0].getElementsByTagName("DIV");
						console.log("fffff " + feedDivs.length);
						console.log("YES YES");
						
						if (feedDivs.length > 0) {
							console.log("YEH YEHEHEHE");
							var lastDiv = feedDivs[feedDivs.length - 1];
//							lastDiv.parentNode.insertBefore(elements[0], lastDiv.nextSibling);
//							feedDivs[feedDivs.length - 1].app
//							selected[0].appendChild(output);
							var spanTemplate = document.getElementById("profile-feed-url-span").innerHTML;
							var divTemplate = document.getElementById("profile-feed-div").innerHTML;
							
							var spans = selected[0].getElementsByTagName("SPAN");
							//https://twitter.com/fsharporg
							if (spans.length > 0) {
								console.log(selected[0].innerHTML);
								var output = Mustache.render(spanTemplate, data);
								
								var div = document.createElement("DIV"); // Needed to create node element for render data
								div.innerHTML = output;
								console.log(div.children.length);
								console.log(div.children[0].nodeName);
								console.log(div.innerHTML);
								// Gets last user handle, which is the latest to be added
								var handle = div.children[div.children.length - 1]; 
								selected[0].insertBefore(handle, spans[spans.length - 1].nextSibling);
								console.log("YEAH SPANSS");
								console.log(selected[0].innerHTML);
								/*console.log(selected[0])*/
							}
							// Insert div data to DOM
							console.log("INSERT");
							console.log(JSON.stringify(data));
							var output2 = Mustache.render(divTemplate, data);
							console.log("INNN");
							var div2 = document.createElement("DIV");
							div2.innerHTML = output2;
							var data = div2.children;
							selected[0].insertBefore(data[0], lastDiv.nextSibling);
							 
						} else {
							console.log("nah");
							var output = Mustache.render(template, data);
							var div = document.createElement("div");
							div.innerHTML = output;
							var elements = div.children;
							var links = selected[0].getElementsByTagName("A");
							console.log("link len " + links.length);
							
							console.log(elements[0].nodeName);
							// Adds First span to DOM
							var span = links[0].parentNode.insertBefore(elements[0], links[0].nextSibling);
							// Adds DIV to DOM sescond
							links[0].parentNode.insertBefore(elements[0], span.nextSibling);
							console.log("Elements length " + elements.length);
							//https://instagram.com/zoeisabellakravitz/
							
						}
					} 
					/*var output = Mustache.render(template, data);*/
					
				},
				onFailure: function (error_code, error_message) {
					alert("ERROR " + error_message);
				}
			});
		}
	};
	
	
	/** Event Delegation for Add Feed Form **/
	multiSol.form.addFeedEventListener = function () {
		var add = document.getElementById("add-feed-button");
		var close = document.getElementById("cancel-feed-button");
		
		var addHandler = function () {
			/*var feed = document.getElementById("feedInput").value;*/
			var feed = document.getElementById("feedInput");
			console.log("Feed!! " + feed);
			console.log("hey");
			console.log(feed.value.length);
			if (feed.value.length > 0) {
				multiSol.addFeed(feed);
			}
		}
		var handler = function () {
			$("#addFeedModal").foundation("reveal", "close");
			add.removeEventListener("click", addHandler, false);
			close.removeEventListener("click", handler, false);
			xClose.removeEventListener("click", xHandler, false);
			console.log("CLOSING.................");
		}
		
		add.addEventListener("click", addHandler, false);
		close.addEventListener("click", handler, false)
		console.log("closssssssssssssse");
		
		var xClose = document.getElementById("close-add-feed");
		var xHandler = function () {
			$(document).on('close.fndtn.reveal', '[data-reveal]', function () {
				  //var modal = $(this);
				  add.removeEventListener("click", addHandler, false );
				  close.removeEventListener("click", handler, false);
				  xClose.removeEventListener("click", xHandler, false);
				  console.log("Add Feed form has been closed");
				});
		};
		xClose.addEventListener("click", xHandler, false);
		
	};
	
	// Removes addfeed listener when form is closed
	multiSol.onFeedFormClose = function (feedForm, listener) {
		$(document).on('close.fndtn.reveal', '[data-reveal]', function () {
			  var modal = $(this);
			  feedForm.removeEventListener("click", listener, false );
			  console.log("Add Feed form has been closed");
			});
	};
	
	
	
	
	
	/** Add new user data to database via Java server side */
	multiSol.MsgRouter.addUserToDatabase = function(data) {
		window.cefQuery({
			request: "cefTest " + data,
			onSuccess: function(response) {
//				alert(response);
				var data = {profiles: JSON.parse(response)};
				
				// Add to the DOM
				var profiles = document.getElementById("profile-list");
				var list = profiles.getElementsByTagName("LI");
				console.log("Change class " + data.profiles.name);
				
				if (list.length > 0) {
					var template = document.getElementById("single-user-profile").innerHTML;
					var output = Mustache.render(template, data);
					var div = document.createElement("DIV");
					div.innerHTML = output;
					//profiles.insertBefore(output, list[list.length - 1]);
					profiles.insertBefore(div.children[0], list[0].nextSibling); //insert at top
				} else {
					var template = document.getElementById("user-profiles-tmpl").innerHTML;
					
					var output = Mustache.render(template, data);
//					profiles.innerHTML = output;
				}
				console.log(data.profiles.name);
				console.log("output " + data.profiles);
				
			},
			onFailure: function (error_code, error_message) {
				alert("Error " + error_message);
			}
		});
	};
	
	
	/*multiSol.MsgRouter.getProfileJSON = function () {
		window.cefQuery({
			request: "profiles",
			onSuccess: function (response) {
				var data = {profiles: JSON.parse(response)};
				return data;
			},
			onFailure: function (error_code, error_message) {
				alert("Error " + error_message);
			}
		});
	};*/
	
	/*
	 * Creates and generates list profiles for menu list
	 * */
	multiSol.MsgRouter.getProfiles = function () {
		
		window.cefQuery({
			request: "profiles ",
			onSuccess: function (response) {
				var template = document.getElementById("user-profiles-tmpl").innerHTML;
				console.log("Heeeeeeeeeeeeeeey" + response);
				var data = {profiles: JSON.parse(response)};
				//data = encodeURIComponent(data.profiles);
//				for (var i = 0; i < data.profiles.length; i++) {
//					console.log(encodeURIComponent(data.profiles[i].name));
//					console.log(data.profiles[i].name);
//				}
				var output = Mustache.render(template, data);
//				var div = document.createElement("DIV");
//				div.innerHTML = output;
//				var test = div.getElementsByClassName("content");
//				console.log(data.profiles.length);
				document.getElementById("profile-list").innerHTML = output;
			},	
			onFailure: function (error_code, error_message) {
				alert("ERROR " + error_message);
			}
		});
		
	};
	
	
	/* *
	 * Gets a single profile by its id 
	 * */
	multiSol.MsgRouter.getProfile = function (id, target, handle, url) {  
		// Maybe I should pass data instead of event object
		/*var id = e.target.parentNode.getElementsByTagName("A")[0].dataset.id;
		console.log("IDD " + id);
		var target = document.getElementById("main-content-area").getElementsByClassName("small-block-grid-2")[0];
		var handle = e.target.innerText; */
//		target.innerHTML = "";
//		target.className +=
		document.getElementById("content-overlay").className="transp-overlay";
		
		spinner.spin(target); // Show Loading Spinner
		
		console.log("GOT IT " + id);
		
		var storageId = "";
		var mediaType = "";
		/*var url = multiSol.profile.getSocialURL(e);*/
		
		if (url.indexOf("instagram") > -1) {
			storageId = handle + "__Instagram";
			mediaType = "instagram";
		} else if (url.indexOf("youtube") > -1) {
			storageId = handle + "__YouTube";
			mediaType = "youtube";
		} else if (url.indexOf("twitter.com") > -1) {
			storageId = handle + "__Twitter";
			mediaType = "twitter.com";
		} else {
			console.log("Error: Social Media not found");
			return alert("Error: Social Media not found");
		}
		console.log(storageId);
		
		// Check if data object has been stored for use as cache 
		storedData = JSON.parse(localStorage.getItem(storageId));
		if (storedData == null 
				|| storedData == undefined) {
			
			console.log("storage not found");
			multiSol.MsgRouter.findProfile(id, handle, url, mediaType);
			
		} else {
			
			console.log("storage found");
//			console.log(localStorage.getItem(storageId));
			var cachedData = JSON.parse(localStorage.getItem(storageId));
			console.log(cachedData.profile.handle);
			
			// Stored data object should be renewed after 5 minutes or 300 seconds
			var elapsedTime = Date.now() - storedData.profile.timestamp;
			console.log(elapsedTime/1000);
			if (elapsedTime/1000 <= 120) {
				
				/*if (elapsedTime/1000 <= 120 && mediaType == "twitter.com") {
					// Uses getTweets Again method in java
					// Gets more tweets if twitter Object already exists
//					if (twitterObj != "") {
//						console.log(storedData.profile.handle);
//						twitterObj.getMoreTweets(storedData.profile.handle, storedData);
//					}
					
//					multiSol.twitterScroll();
					// Maybe get again should be used for refresh button instead
				} else {
					multiSol.MsgRouter.findProfile(id, handle, url, mediaType);
				}*/
				console.log("TEST");
				// Show Profile with cached data
				multiSol.profile.showProfile(storedData);
			} else {
				/*multiSol.profile.showProfile(storedData);*/
				// Get fresh and new data
				multiSol.MsgRouter.findProfile(id, handle, url, mediaType);
			}
		}
		
		console.log("DONE");
	};
	
	multiSol.MsgRouter.findProfile = function (id, handle, url, mediaType) {
		window.cefQuery({
			request: "getProfile " + id ,
			onSuccess: function (response) {
				console.log("YESSSSS " + response);
				var data = {profile: JSON.parse(response)};
				
				//Gets the users handle name from html and adds it to JSON data object
				// ** Maybe its better to have it already saved in the database
				/*data.profile.handle = e.target.innerText; */
				data.profile.handle = handle;
				//var url = multiSol.profile.getSocialURL(e);
				console.log("URL " + url);
				
				//Add and store in JSON data object
				if (url.length > 0) 
					data.profile.url = url 
				else 
					return alert("Error: URL Not found");
				
				console.log(data.profile.url);
				/*console.log("AGAIN " + JSON.stringify(data));*/
				/*console.log("HANDLE " + e.target.innerText);*/
				
				// Decides whether to call instagram or youtube function depending on the url
				if (mediaType == "instagram") {
//					multiSol.instagram.getUserId(data);
					socialMediaObj = new Instagram(data);
					socialMediaObj.getUserID();
					console.log(socialMediaObj.getHandle());
				} else if (mediaType == "youtube") {
					socialMediaObj = new Youtube(data);
					socialMediaObj.getPlaylistID(data.profile.handle);
				} else if (mediaType == "twitter.com") {
					console.log("IN TWITTER");
					/*if (twitterObj == "") {*/
						twitterObj = new Twitter();
						twitterObj.getTweets(data.profile.handle, data);
					/*} else {
						console.log("ERROR");
					}*/
					
				} else {
					console.log("Error: Social Media Type not found");
					return alert("Error: Social Media Type not found. \nPlease Try Again");
				}
			},
			onFailure: function (error_code, error_message) {
				alert("HEY ERR " + error_message);
			}
		});
	};
	
	
	
	
	
	
	multiSol.ping = function () {
		console.log("Pong");
		alert("Pong");
	};
		
	
})();




/*multiSol.searchMenu();
multiSol.closeRightMenu();
multiSol.toggleRightMenu();
multiSol.enterSearch();
multiSol.form.addFeedRow();
multiSol.form.cancelUser();
multiSol.form.submitNewUser();
multiSol.form.onAddUserModalShow();
multiSol.MsgRouter.getProfiles();
multiSol.eventDelegation();*/


