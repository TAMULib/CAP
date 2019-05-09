cap.controller("AbstractAppController", function($controller, $scope) {

  angular.extend(this, $controller('AbstractController', {
        $scope: $scope
  }));

  $scope.isCurator = function () {
      return (sessionStorage.role === "ROLE_CURATOR");
  };

  $scope.isCollapsable = function(triples, predicate) {
    var matches = 0;
    for(var triple of triples) {
      if(triple.predicate === predicate) {
        matches++;
        if(matches > 1) {
          return true;
        }
      }
    }
    return false;
  };

});