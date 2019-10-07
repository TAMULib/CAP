cap.controller("AbstractAppController", function($controller, $q, $scope, $timeout, ModalService) {

  angular.extend(this, $controller('AbstractController', {
        $scope: $scope
  }));

  $scope.closeModal = function() {
    return $q(function(resolve) {
      ModalService.closeModal();
      $timeout(function() {
          resolve();
      }, 250)
    });
  };

  $scope.isCurator = function () {
      return (sessionStorage.role === "ROLE_CURATOR");
  };

  $scope.isCollapsable = function(triples, predicate) {
    var matches = 0;
    for (var i = 0; i < triples.length; i++) {
      if (triples.hasOwnProperty(i) && triples[i].predicate === predicate) {
        matches++;
        if (matches > 1) {
          return true;
        }
      }
    }
    return false;
  };

});