cap.controller("IrContextController", function($controller, $scope, IRRepo, $routeParams, $location, $route, IRContext) {
  
  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  var contextUri = $routeParams.context;

  IRRepo.ready().then(function() {
    
    $scope.ir = IRRepo.findByName(decodeURI($routeParams.irName));  
    
    $scope.context = new IRContext({
      ir: IRRepo.findByName(decodeURI($routeParams.irName)),
      uri: $routeParams.context
    });
    
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
    $scope.context = new IRContext({
      ir: IRRepo.findByName(decodeURI($routeParams.irName)),
      uri: containerUri
    });
    $location.search("context", containerUri);
  };

});