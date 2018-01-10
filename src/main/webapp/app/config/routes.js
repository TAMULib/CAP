cap.config(function ($locationProvider, $routeProvider) {

      $locationProvider.html5Mode(true);

      $routeProvider.
      when('/', {
        templateUrl: 'views/splash.html'
      }).
      when('/home', {
        redirectTo: '/',
      }).
      when('/admin/ir', {
        templateUrl: 'views/admin/irManagement.html',
        controller: 'IrManagementController'
      }).
      when('/admin/schema', {
        templateUrl: 'views/admin/schemaManagement.html',
        controller: 'SchemaManagementController'
      }).
      when('/ir/:irName', {
        templateUrl: 'views/irContext.html',
        controller: 'IrContextController',
        reloadOnSearch: false
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
