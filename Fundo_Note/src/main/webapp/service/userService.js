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
	   data:angular.toJson(loginModel)
	  };
      $http(request).then(function successCallback(response)
      {
    	  $state.go('home');
    	   console.log("successfully",response.data.message); 
      },function errorCallback(response){
    	console.log("failed",response);  
      })
 };
      
      
      return {
        registerUser: registerUser,
        loginUser:loginUser
      };
});