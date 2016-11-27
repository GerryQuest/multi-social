/*
 * Twitter Object for receiving user timeline streams and twitter API
 * Uses Constructor/Protoype Pattern
 * By Eissek
 */


function Twitter (data) {
	this.profileData = "";
	this.consKey = null;
	this.consSec = null;
	
}

Twitter.prototype = {
		constructor: Twitter,
		jsonp: function (url) {
			var head = document.head;
			var script = document.createElement("script");
			script.setAttribute("src", url);
			head.appendChild(script);
			head.removeChild(script);
		},
		setCons: function () {
//			

			this.consKey = multiSol.auth.getCode("TWITKEY");
			this.consSec = multiSol.auth.getCode("TWITSEC");
		},
		getBase64: function () {
			var key = encodeURIComponent(this.consKey);
			var sec = encodeURIComponent(this.consSec);
			var keyAndSec = key + ":" + sec;
			var b64 = btoa(keyAndSec); // Encode to Base64
			return b64;
		},
		/*getNeeded: function (code) {
			window.cefQuery({
				request: "getAuth" + code,
				onSuccess: function (data) {
					alert("okay " + data);
				},
				onFailure: function (error_code, error_message) {
					alert("ERROR " + error_message);
				}
				
			});
		},*/
		getTweets: function (username, profileData) {
			this.profileData = profileData;
//			var b64 = this.getBase64();
//			console.log(b64);
			window.cefQuery({
//				request: "getBearerToken" + b64 + "requestUser" + username,
				request: "getBearerToken" + "requestUser" + username,
				onSuccess: function (data) {
//					console.log("TOKEN BEARER WORKS " + data);
					/*var twt = data;*/
					/*data = data.replace("/\\/g", "");*/
					var twt = JSON.parse(data);
//					console.log("TOKEN " +  twt[0].html);
//					console.log(profileData.profile.handle);
					profileData.profile.twt = twt;
					profileData.profile.timestamp = Date.now();
					// Add to localStorage to use it as cache
					localStorage.setItem(profileData.profile.handle + "__Twitter", JSON.stringify(profileData));
					multiSol.profile.showProfile(profileData);
				},
				onFailure: function (error_code, error_message) {
					alert("ERROR " + error_message);
				}
				
			});
		},
		getMoreTweets: function (username, profileData) {
//			var b64 = this.getBase64();
//			console.log(b64);
			window.cefQuery({
//				request:"getMoreTweets" + b64 + "requestUser" + username,
				request:"getMoreTweets" + "requestUser" + username,
				onSuccess: function (data) {
					var twt = JSON.parse(data);
					profileData.profile.twt = twt;
					profileData.profile.timestamp = Date.now();
					// Add to localStorage to use it as cache
					localStorage.setItem(profileData.profile.handle + "__Twitter", JSON.stringify(profileData));
					//multiSol.profile.showProfile(profileData);
					multiSol.profile.showMoreMedia("twitter", profileData);
				},
				onFailure: function (error_code, error_message) {
					alert("ERROR " + error_message);
				}
			});
		}
}

