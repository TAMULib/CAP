cap.controller("DashboardController", function($controller, $scope) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  


});