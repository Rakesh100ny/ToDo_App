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

	$scope.forgotPasswordModel = {
			email : "abc@gmail.com",
			
		};

	$scope.passwordModel = {
			newPassword : "",
			confirmPassword:""
			
		};
	
	$scope.result="";
		
	$scope.register = function(registerModel) {
		$scope.result=registerModel.email;
		console.log("result inside method",$scope.result);
		console.log("User Details", angular.toJson(registerModel));
		userService.registerUser(registerModel);
	};

	console.log("result outside method",$scope.result);
	
	$scope.login = function(loginModel) {
		console.log("Login Details", angular.toJson(loginModel));
		userService.loginUser(loginModel);
	};

	$scope.forgotPassword = function(forgotPasswordModel) {
		console.log("ForgotPassword Details", angular.toJson(forgotPasswordModel));
		userService.forgotPasswordUser(forgotPasswordModel);
	};

	$scope.resetPassword = function(passwordModel) {
		console.log("ResetPassword Details", angular.toJson(passwordModel));
		userService.resetPasswordUser(passwordModel);
	};
	
});