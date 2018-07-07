app.controller('userController', function($scope, userService) {

	$scope.registerModel = {
		firstName : "Enter First Name ie. Ronny",
		lastName : "Enter Last Name ie. Roy",
		email : "abc@gmail.com",
		password : "",
		mobileNo : "91+ "
	};

	$scope.loginModel = {
		email : "abc@gmail.com",
		password : ""
	};

	$scope.register = function(registerModel) {
		console.log("User Details", angular.toJson(registerModel));
		userService.registerUser(registerModel);
	};

	$scope.login = function(loginModel) {
		console.log("Login Details", angular.toJson(loginModel));
		userService.loginUser(loginModel);
	};

});