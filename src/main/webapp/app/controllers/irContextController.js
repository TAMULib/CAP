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

    $scope.context = $scope.ir.loadContext($scope.ir.contextUri);

    $scope.createContainer = function(form) {
      $scope.context.createContainer(form).then(function() {
        $scope.closeModal();
      });
    };

    $scope.advancedUpdate = function(form) {
      $scope.context.advancedUpdate(form).then(function() {
        $scope.closeModal();
      });
    };

    $scope.uploadResource = function(form) {
      $scope.context.createResource(form).then(function() {
        $scope.closeModal();
      });
    };

    $scope.resetCreateContainer = function() {
      $scope.irForm.createContainer = {
        name: ""
      };
      $scope.closeModal();
    };

    $scope.resetUploadResource = function() {
      if($scope.irForm.uploadResource) {
        delete $scope.irForm.uploadResource.file;
      }
      $scope.closeModal();
    };

    $scope.resetAdvancedUpdate = function() {
      var defaultSparql = '';
      angular.forEach($scope.ir.schemas, function(schema) {
        defaultSparql += 'PREFIX ' + schema.abbreviation + ': <' + schema.namespace + '>\n';
      });
      defaultSparql += '\n\n';
      defaultSparql += 'DELETE { }\n';
      defaultSparql += 'INSERT { }\n';
      defaultSparql += 'WHERE { }\n\n';
      $scope.irForm.advancedUpdate = {
        sparql: defaultSparql
      };
      $scope.closeModal();
    };

    $scope.addMetadata = function(form) {
      var triples = [];
      angular.forEach(form.entries, function(entry) {
        triples.push({
          subject: $scope.context.uri,
          predicate: entry.property.uri,
          object: entry.value
        });
      });

      $scope.context.createMetadata(triples).then(function() {
        $scope.closeModal();
      });

    };

    $scope.resetCreateContainer();
    $scope.resetUploadResource();

  });

});
