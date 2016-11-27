/**
 * Module for multiSol.form
 * Handles various form interactions
 */

var multiSol = multiSol || {};
multiSol.form = multiSol.form || {};
multiSol.MsgRouter = multiSol.MsgRouter || {};

(function () {
	
	
	multiSol.form.addFeedRow = function () {
		var enabled = false;
			var addUser = document.getElementById("add-user-div");
			var socialRow = document.getElementsByClassName("social-row");
			var lastSocialRow = socialRow[socialRow.length - 1];
			var addSocial = document.getElementById("add-social");
			
			/*newSocialRow.innerHTML*/
			if (addSocial) {
					addSocial.addEventListener("click", function (e) {
						if (socialRow.length < 4) {
							var newSocialRow = document.createElement("div");
							newSocialRow.className = "row social-row ";
							/*newSocialRow.innerHTML = "<div class=\"small-9 large-11 columns\">"
								+ "<input type=\"text\" placeholder=\"Social Media Feed\"></div>";*/
							newSocialRow.innerHTML = "<div class=\"small-9 large-11 columns\">"
								+ "<input name=\"socialMedia\" type=\"text\" placeholder=\"Social Media Feed\"></div>" +
								"<div class=\"small-3 large-1 columns\">" +
							  					"<a id=\"remove-social\" href=\"#\">" +
							  					"<i class=\"fi-minus\"></i></a></div>";
							
							console.log("add social clicked");
							/*console.log("length " + lastSocialRow);*/
							lastSocialRow.parentNode.insertBefore(newSocialRow, lastSocialRow.nextSibling);
							multiSol.form.removeFeedRow(newSocialRow);
						} else {
							
						}
						
						e.preventDefault();
					}, false);
					enabled = true;
			}
			return enabled;
			
	};
	
	multiSol.form.removeFeedRow = function (row) {
		var enabled = false;
		var removeSocial = document.getElementById("remove-social");
		if (removeSocial) {
			removeSocial.addEventListener("click", function (e) {
				/*console.log(removeSocial.parentNode.parentNode);*/
				row.parentNode.removeChild(row);
				console.log("Removed Node");
			}, false);
			enabled = true;
		}
		
		return enabled;
	};
	
	multiSol.form.cancelUser = function () {
		var enabled = false;
		var cancel = document.getElementById("cancelUserButton");
		var close = function () {
			var addUserModal = document.getElementById("addUserModal");
			$(addUserModal).foundation('reveal', 'close');
			/*document.getElementById("addUserButton").className = "button small";*/
			
			cancel.removeEventListener("click", close, false);
		};
		if (cancel) {
				
				cancel.addEventListener("click", close, false);
				multiSol.form.clearInput(document.getElementById("addUserForm"));
				enabled = true;
		}
		
		return enabled;
	};
	
	multiSol.form.onAddUserModalShow = function () {
		var enabled = false;
		var addUserForm = document.getElementById("showAddUserForm");
		/*var add = "ge";*/
		if (addUserForm) {
			console.log("fffffffff");
			var addButton = document.getElementById("addUserButton");
			addUserForm.addEventListener("click", function (e) {
				multiSol.form.validation();  // Enables Validation EventListener
				addButton.className = "button small disabled";
				multiSol.form.cancelUser(); // Enables Cancel Form Event Listener
				multiSol.form.submitNewUser(); // Enable listener for Submit button
				addButton.className = "button small disabled";
			}, false);
			enabled = true;
		}
		return enabled;
	};
	
	// Clear input fields of given form
	multiSol.form.clearInput = function (form) {
		for (var i = 0; i < form.elements.length; i++) {
			var element = form.elements[i];
			if (element.type == "text") {
				if (element.value != "") {
					element.value = "";
				}
			}
		}
	};
	
	multiSol.form.validation = function () {
//		var form = document.forms.["addUserForm"];
		var form = document.getElementById("addUserForm");
		var valid = false;
		
		var validate = function () {
			for (var i = 0; i < form.elements.length; i++) {
				var element = form.elements[i];
				/*console.log(form.elements[i].type);*/
				if (element.type == "text") {
//							console.log("Pattern result: " + pattern.test(form.elements[i].value));
					if (!pattern.test(element.value)) {
						valid = false;
						/*console.log("hhshss");*/
						break;
						
					} else if (element.name == "socialMedia" && element.value.indexOf("http") == -1) {
						valid = false;
						break;
					} else
						valid = true;
					
				}
			}
			console.log("break from loop");
			console.log("Valid is: " + valid);
			
			if (valid) {
				console.log("SUBMIT NEW USER");
				button.className = "button small"; // Submit button becomes enabled
			} else  { // Changed from if (button.className.indexOf("disabled") == -1)
				
				button.className = "button small disabled";
			}
		};
		
		if (form) {
					
					var pattern = /^[0-9a-zA-Z\s:\/._]+$/;
					var button = document.getElementById("addUserButton");
					console.log("Form length " + form.elements.length);
					form.addEventListener("keyup", validate, false);
		}
		console.log("Validity: " + valid);
		return valid;
	};
	
	// Submits new user to database via form
	multiSol.form.submitNewUser = function () {
		var button = document.getElementById("addUserButton");
		var serialize = function () {
			console.log(button.className);
			
			// Serialize data if sumbit button is actived or not disabled
			if (button.className.indexOf("disabled") != -1)
				console.log("button is disabled");
			else {
				multiSol.form.serializeFormData();
				console.log("Submit button not disabled");
			}
			button.removeEventListener("click", serialize, false);
		}
		if (button) {
				button.addEventListener("click", serialize, false);
		}
		
	};
	
// Message Router
	
	multiSol.form.serializeFormData = function () {
		var form = document.getElementById("addUserForm");
		var formData = {};
		var social = []; // Holds the social media url links
		var socialHandle = [];
		for (var i = 0; i < form.length; i++) {
			if (form.elements[i].type == "text") {
				/*console.log(form.elements[i].name);*/
				
				console.log("serialize " + form.elements[i].name);
				var str = form.elements[i].value;
				if (form.elements[i].name == "name")
					formData.accordionID = str;
				
				if (form.elements[i].name == "socialMedia") {
					social.push(str);
					console.log("social media data added to array");
				}
				
				// Data in tags form field is split by space/comma and added to Json Object
				if (/*form.elements[i].name == "socialMedia" ||*/ form.elements[i].name == "tags") {
					var pattern = /[,\s]+/;
					formData[form.elements[i].name] = str.split(pattern);
				} else {
					// Add the other form fields
					formData[form.elements[i].name] = str;
				}
					
			}
		};
		
		formData.socialMedia = social;
		formData.socialMediaURL = social;
//		formData.socialMediaHandle = form.elements[0].value; //name
		console.log(formData);
		var data = JSON.stringify(formData, null, 3);
		console.log(data);
		multiSol.form.clearInput(document.getElementById("addUserForm"));
		multiSol.MsgRouter.addUserToDatabase(data);
	};
	
	
})();