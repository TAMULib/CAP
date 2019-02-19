cap.controller('AppLoginController', function ($controller, $location, $scope, UserService, StorageService) {

    angular.extend(this, $controller('LoginController', {
        $scope: $scope
    }));

    $scope.checkAuthStrategy = function(strategy) {
        return (appConfig.authStrategies.indexOf(strategy) > -1);
    };

    $scope.isShibEnabled = function() {
        return $scope.checkAuthStrategy('shibboleth');
    };

    $scope.isEmailEnabled = function() {
        return $scope.checkAuthStrategy('emailRegistration');
    };

});