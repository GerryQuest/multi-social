/**
 * Callback.js
 * These callbacks are used by JSONP when returning data
 * Created by Eissek
 */
var multiSol = multiSol || {};
multiSol.callback = multiSol.callback || {};

(function () {
	// Callbacks for Instagram
	multiSol.callback.getJsonpCallback = function (data) {
		/*var mediaObj = */multiSol.getSocialMediaObj().jsonpCallback(data);
	};
	
	multiSol.callback.getImageLinksCallback = function (data) {
		multiSol.getSocialMediaObj().getImageLinks(data);
	};
	
	multiSol.callback.getEmbedClbk = function (data) {
		multiSol.getSocialMediaObj().embedClbk(data);
		
	};
	
	// Callbacks for Youtube JsonP
	multiSol.callback.pListIdCallback = function (data) {
		multiSol.getSocialMediaObj().pListIdCallback(data);
	};
	
	multiSol.callback.getVideosCallback = function (data) {
		multiSol.getSocialMediaObj().getVideosCallback(data);
	};
})();