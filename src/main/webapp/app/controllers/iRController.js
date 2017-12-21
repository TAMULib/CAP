cap.controller("IrController", function($controller, $scope, IRRepo,  $routeParams) {
  
  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.ir = IRRepo.findByName(decodeURI($routeParams.irName));
  

});