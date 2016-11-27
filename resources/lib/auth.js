/**
 * 
 */

var multiSol = multiSol || {};
multiSol.auth = multiSol.auth || {};

(function () {
	multiSol.auth.getCode =  function (value) {
//		alert("hey " + value)
		window.cefQuery({
			request: "getAuth" + value,
			onSuccess: function (data) {
				alert("data " + data);
				return data;
			},
			onFailure: function (error_code, error_message) {
				alert("Error: " + error_message);
			}
		});
	}
})();