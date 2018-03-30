cap.controller("IrContextController", function ($controller, $location, $routeParams, $scope, IRRepo) {

  angular.extend(this, $controller('CoreAdminController', {
    $scope: $scope
  }));

  $scope.irForm = {};

  $scope.theaterMode = false;

  $scope.setOrToggleTheaterMode = function (mode) {
    $scope.theaterMode = mode ? mode : !$scope.theaterMode;
  };

  IRRepo.ready().then(function () {

    $scope.ir = IRRepo.findByName(decodeURI($routeParams.irName));

    if(!$scope.ir) $location.path("/error/404");

    if ($routeParams.context !== undefined) {
      $scope.ir.contextUri = decodeURI($routeParams.context);
    } else {
      $location.search("context", $scope.ir.contextUri);
    }

    $scope.context = $scope.ir.loadContext($scope.ir.contextUri);

    $scope.createContainer = function (form) {

      var triples = [];
      angular.forEach(form.entries, function (entry) {
        triples.push({
          subject: $scope.context.uri,
          predicate: entry.property.uri,
          object: entry.value
        });
      });

      $scope.context.createContainer(triples).then(function () {
        $scope.closeModal();
      });
    };

    $scope.advancedUpdate = function (sparql) {
      $scope.context.advancedUpdate(sparql).then(function () {
        $scope.closeModal();
      });
    };

    $scope.uploadResource = function (file) {
      $scope.context.createResource(file).then(function () {
        $scope.resetUploadResource();
      });
    };

    $scope.resetCreateContainer = function () {
      $scope.irForm.createContainer = {
        name: ""
      };
      $scope.closeModal();
    };

    $scope.resetUploadResource = function () {
      if ($scope.irForm.uploadResource) {
        delete $scope.irForm.uploadResource.file;
      }
      $scope.closeModal();
    };

    $scope.resetAdvancedUpdate = function () {
      $scope.irForm.advancedUpdate = $scope.context.getQueryHelp();
      $scope.closeModal();
    };

    $scope.addMetadata = function (form) {
      var triples = [];
      angular.forEach(form.entries, function (entry) {
        triples.push({
          subject: $scope.context.uri,
          predicate: entry.property.uri,
          object: entry.value
        });
      });

      $scope.context.createMetadata(triples).then(function () {
        $scope.closeModal();
      });
    };

    $scope.cancelDeleteIrContext = function (irContext) {
      $scope.irContextToDelete = {};
      $scope.closeModal();
    };

    $scope.deleteIrContext = function () {

      var ir = $scope.context.ir;
      var currentTriple = $scope.context.triple;
      var isResource = $scope.context.resource;
      
      var deleteContext = isResource ? $scope.context.removeResources : $scope.context.removeContainers;

      $scope.context = ir.loadContext($scope.context.parent.object);

      deleteContext([currentTriple]).then(function () {
        $scope.context = ir.loadContext($scope.context.uri, true);
      });

    };

    $scope.revertVersion = function () {

      var ir = $scope.context.ir;
      var currentContext = $scope.context;

      $scope.context = ir.loadContext($scope.context.parent.object);

      $scope.context.revertVersion(currentContext).then(function () {
        $scope.context = ir.loadContext($scope.context.uri, true);
      });
    };

    $scope.deleteVersion = function () {

      var ir = $scope.context.ir;
      var currentContext = $scope.context;

      $scope.context = ir.loadContext($scope.context.parent.object);

      ir.removeCachedContext($scope.context.uri);

      $scope.context.deleteVersion(currentContext).then(function () {
        $scope.context = ir.loadContext($scope.context.uri, true);
      });
    };

    $scope.startTransaction = function() {
      $scope.context.startTransaction();
    };

    $scope.copyToClipboard = function (text, target) {
      var textArea = document.createElement("textarea");
      textArea.value = text;
      document.body.appendChild(textArea);
      textArea.select();
      try {
        document.execCommand('copy');
        $scope[target + "Copied"] = true;
      } catch (e) {
        console.log("browser doesn't support copying to clipboard");
      } finally {
        document.body.removeChild(textArea);
        $timeout(function () {
          $scope[target + "Copied"] = false;
        }, 1500);
      }
    };

    $scope.srcFromFile = function (file) {
      return URL.createObjectURL(file);
    };

    $scope.resetCreateContainer();
    $scope.resetUploadResource();
    
  });

});