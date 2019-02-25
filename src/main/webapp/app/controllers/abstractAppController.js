cap.controller("AbstractAppController", function($controller, $scope) {
  angular.extend(this, $controller('AbstractController', {
        $scope: $scope
  }));

  $scope.isCurator = function () {
      return (sessionStorage.role === "ROLE_CURATOR");
  };
});