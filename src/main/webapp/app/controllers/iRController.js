cap.controller("IrController", function($controller, $scope, IRRepo,  $routeParams) {
  
  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.name =  $routeParams.irName;

});