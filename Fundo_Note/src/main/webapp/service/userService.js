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
       
        $http(request).then(function successCallback(response){
        	$state.go('success')
        	$scope.result=response.data.email;
        	console.log("result",$scope.result);
     	   console.log("successfully",response.data.message); 
       },function errorCallback(response){
     	console.log("failed",response.data);  
       })
      };

 var loginUser=function(loginModel)
 {
  var request=
	  {
	   method:"POST",
	   url:"login",
	   headers: {
	        'Content-Type': "application/json",
	        'tokenLogin':localStorage.getItem("tokenLogin")
	      },
	   data:angular.toJson(loginModel)
	  };
      $http(request).then(function successCallback(response)
      {
    	  $state.go('home');
    	  localStorage.setItem("tokenLogin",response.data.message);
    	  console.log("successfully",response.data.message); 
      },function errorCallback(response){
    	console.log("failed",response.data);  
      })
 };
      
 var forgotPasswordUser=function(forgotPasswordModel)
 {
  var request=
	  {
	   method : "POST",
	   url:"forgotpassword",
	   data:angular.toJson(forgotPasswordModel)
	  };
     
      $http(request).then(function successCallback(response){
    	  localStorage.setItem("tokenForgotPassword",response.data.message);
    	  console.log("successfully",response.data.message); 
 
      },function errorCallback(response){
    	  console.log("failed",response.data); 
      });
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
     
      $http(request).then(function successCallback(response){
    	  console.log("successfully",response.data.message); 
    	  
      },function errorCallback(response){
    	  console.log("failed",response.data); 
      });
 }
 
      return {
        registerUser: registerUser,
        loginUser:loginUser,
        forgotPasswordUser:forgotPasswordUser,
        resetPasswordUser:resetPasswordUser
      };
});