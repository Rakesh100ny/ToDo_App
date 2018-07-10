app.service('userService',function($http,$state)
{
  var registerUser=function(registerModel)
  {
        var request =
        {
          method: "POST",
          url: "register",
          data: angular.toJson(registerModel)
        };
       
        return $http(request);/*.then(function successCallback(response){
        	$state.go('success')
    	 console.log("successfully",response.data.message); 
       },function errorCallback(response){
     	console.log("failed",response.data);  
       })*/
      };

 var loginUser=function(loginModel)
 {
  var request=
	  {
	   method:"POST",
	   url:"login",
	   headers: {
	        'tokenLogin':localStorage.getItem("tokenLogin")
	      },
	   data:angular.toJson(loginModel)
	  };
     return $http(request);
 };
      
 var forgotPasswordUser=function(forgotPasswordModel)
 {
  var request=
	  {
	   method : "POST",
	   url:"forgotpassword",
	   data:angular.toJson(forgotPasswordModel)
	  };
     
      return $http(request);
 }

 
 var resetPasswordUser=function(passwordModel)
 {
  var request=
	  {
	   method : "POST",
	   url:"resetpassword",
	   headers: {
	        'tokenForgotPassword': localStorage.getItem("tokenForgotPassword"),
	      },
	   data:angular.toJson(passwordModel)
	  };
     
      return $http(request);
 }
 
      return {
        registerUser: registerUser,
        loginUser:loginUser,
        forgotPasswordUser:forgotPasswordUser,
        resetPasswordUser:resetPasswordUser
      };
});