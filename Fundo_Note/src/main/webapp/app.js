var app = angular
		.module('keepApp', [ 'ui.router', 'ngMaterial', 'ngMessages' ]);
app.config([ '$stateProvider', '$urlRouterProvider',
		function($stateProvider, $urlRouterProvider) {
			$stateProvider

			.state('login', {
				url : '/login',
				templateUrl : 'template/login.html',
				controller : 'userController'
			})

			.state('register', {
				url : '/register',
				templateUrl : 'template/register.html',
				controller : 'userController'
			})

			.state('signupSuccess', {
				url : '/register/success',
				templateUrl : 'template/signupSuccess.html',
				controller : 'userController'
			})

			.state('forgotpassword', {
				url : '/forgotpassword',
				templateUrl : 'template/forgotPassword.html',
				controller : 'userController'
			})
			
			.state('forgotpasswordSuccess', {
				url : '/forgotpassword/success',
				templateUrl : 'template/forgotPasswordSuccess.html',
				controller : 'userController'
			})
			.state('resetpassword', {
				url : '/resetpassword',
				templateUrl : 'template/resetPassword.html',
				controller : 'userController'
			})
			.state('home', {
				url : '/home',
				templateUrl : 'template/home.html',
				controller : 'homeCtrl'
			});

			/*
			 * .state('home.dashboard', { url: '/dashboard', templateUrl:
			 * 'templates/dashboard.html', controller: 'dashboardCtrl' });
			 */

			$urlRouterProvider.otherwise('/register');

		} ]);
