cap.controller("IrController", function($controller, $scope, IRRepo, $routeParams, $location, $route) {
  
  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  var contextUri = $routeParams.context;

  IRRepo.ready().then(function() {
    
    $scope.ir = IRRepo.findByName(decodeURI($routeParams.irName));  
    
    if(contextUri !== undefined) {
      $scope.ir.contextUri = decodeURI(contextUri);
    } else {
      $location.search("context", $scope.ir.contextUri);     
    }

  });

  $scope.createContainer = function(form) {
    $scope.ir.createContainer(form)
      .then(function() {
        $scope.closeModal();
      });
  };
  
  $scope.loadContainer = function(containerUri) {
    $scope.ir.contextUri = containerUri;
    $location.search("context", containerUri);
  };

});