cap.controller("IrContextController", function($controller, $scope, IRRepo, $routeParams, $location, $route, IRContext) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.irForm = {};

  IRRepo.ready().then(function() {

    $scope.ir = IRRepo.findByName(decodeURI($routeParams.irName));

    if($routeParams.context !== undefined) {
      $scope.ir.contextUri = decodeURI($routeParams.context);
    } else {
      $location.search("context", $scope.ir.contextUri);
    }

    $scope.createContainer = function(form) {
      $scope.context.createContainer(form).then(function() {
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

    $scope.loadContainer = function(containerUri) {
      var cachedContext = $scope.ir.getCachedContext(containerUri);
      if(cachedContext) {
        $scope.context = cachedContext;
      } else {
        $scope.context = new IRContext({
          ir: $scope.ir,
          uri: containerUri,
          fetch: true
        });
      }
      $location.search("context", containerUri);
    };

    $scope.loadContainer($scope.ir.contextUri);

  });

});
