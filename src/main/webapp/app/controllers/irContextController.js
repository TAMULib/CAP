cap.controller("IrContextController", function($controller, $scope, IRRepo, $routeParams, $location, $route, IRContext) {
  
  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  var contextUri = $routeParams.context;

  $scope.irForm = {};

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

    $scope.createContainer = function(form) {
      $scope.context.createContainer(form)
        .then(function() {
          $scope.closeModal();
        });
    };

    $scope.resetCreateContainer = function() {
      $scope.irForm.createContainer = {
        name: ""
      };
      $scope.closeModal();
    };

    $scope.resetCreateContainer();

    $scope.uploadResource = function(form) {
      $scope.context.createResource(form)
        .then(function() {
          $scope.closeModal();
        });
    };

    $scope.resetUploadResource = function() {
      if($scope.irForm.uploadResource) {
        delete $scope.irForm.uploadResource.file;
      }
      $scope.closeModal();
    };

    $scope.resetCreateContainer();
    $scope.resetUploadResource();

  });
  
  $scope.loadContainer = function(containerUri) {
    $scope.context = new IRContext({
      ir: IRRepo.findByName(decodeURI($routeParams.irName)),
      uri: containerUri
    });
    $location.search("context", containerUri);
  };

});