cap.config(function ($locationProvider, $routeProvider) {
  
      $locationProvider.html5Mode(true);
  
      $routeProvider.
      when('/', {
        templateUrl: 'views/irManagement.html',
        controller: 'IrManagementController'
      }).
      when('/home', {
        redirectTo: '/',        
      }).
      when('/ir/:irName', {
          templateUrl: 'views/ir.html',
          controller: 'IrContextController'
      }).
  
      // Error Routes
      when('/error/403', {
          templateUrl: 'views/errors/403.html',
          controller: 'ErrorPageController'
      }).
      when('/error/404', {
          templateUrl: 'views/errors/404.html',
          controller: 'ErrorPageController'
  
      }).
      when('/error/500', {
          templateUrl: 'views/errors/500.html',
          controller: 'ErrorPageController'
      }).
      otherwise({
          redirectTo: '/error/404'
      });
  
  });