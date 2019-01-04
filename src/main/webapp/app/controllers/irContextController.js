cap.controller("IrContextController", function ($controller, $location, $routeParams, $scope, $timeout, $filter, IRRepo, FixityReport) {

  angular.extend(this, $controller('CoreAdminController', {
    $scope: $scope
  }));

  $scope.irForm = {};

  $scope.submitClicked = false;

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
      $scope.submitClicked = true;
      var subject = $scope.context.uri;
      var triples = [];
      angular.forEach(form.entries, function (entry) {
        if(entry.property&&entry.value) {
          triples.push({
            subject: subject,
            predicate: entry.property.uri,
            object: entry.value
          });
        }
      });

      $scope.context.createContainer(triples).then(function () {
        $scope.closeModal();
        $scope.submitClicked = false;
      });
    };

    $scope.advancedUpdate = function (sparql) {
      $scope.submitClicked = true;
      $scope.context.advancedUpdate(sparql).then(function () {
        $scope.closeModal();
        $scope.submitClicked = false;
      });
    };

    $scope.uploadResource = function (file) {
      $scope.submitClicked = true;
      $scope.context.createResource(file).then(function () {
        $scope.resetUploadResource();
        $scope.submitClicked = false;
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

    $scope.resetAddMetadataModal = function() {
      $scope.irForm.addMetadata.$setPristine();
      $scope.irForm.addMetadata.entries.length = 0;
      $scope.irForm.addMetadata.entries.push({});
      $scope.closeModal();
    };

    $scope.addMetadata = function (form) {
      $scope.submitClicked = true;

      var triples = [];
      angular.forEach(form.entries, function (entry) {
        triples.push({
          subject: $scope.context.uri,
          predicate: entry.property.uri,
          object: entry.value
        });
      });

      $scope.context.createMetadata(triples).then(function () {
        $scope.resetAddMetadataModal();
        $scope.submitClicked = false;
      });

    };

    $scope.cancelDeleteIrContext = function (irContext) {
      $scope.irContextToDelete = {};
      $scope.closeModal();
    };

    $scope.deleteIrContext = function () {
      $scope.submitClicked = true;

      var ir = $scope.context.ir;
      var currentTriple = $scope.context.triple;
      var isResource = $scope.context.resource;

      var deleteContext = isResource ? $scope.context.removeResources : $scope.context.removeContainers;

      $scope.context = ir.loadContext($scope.context.parent.object);

      deleteContext([currentTriple]).then(function () {
        $scope.context = ir.loadContext($scope.context.uri, true);
        $scope.submitClicked = false;
      });

    };

    $scope.revertVersion = function () {
      $scope.submitClicked = true;

      var ir = $scope.context.ir;
      var currentContext = $scope.context;

      $scope.context = ir.loadContext($scope.context.parent.object);

      $scope.context.revertVersion(currentContext).then(function () {
        $scope.context = ir.loadContext($scope.context.uri, true);
        $scope.submitClicked = false;
      });
    };

    $scope.deleteVersion = function () {
      $scope.submitClicked = true;

      var ir = $scope.context.ir;
      var currentContext = $scope.context;

      $scope.context = ir.loadContext($scope.context.parent.object);

      ir.removeCachedContext($scope.context.uri);

      $scope.context.deleteVersion(currentContext).then(function () {
        $scope.context = ir.loadContext($scope.context.uri, true);
        $scope.submitClicked = false;
      });
    };

    $scope.openFixity = function(uriOfContextToCheck) {
      $scope.fixityReport = new FixityReport({
        ir: $scope.context.ir,
        contextUri: uriOfContextToCheck
      });
      $scope.openModal('#fixityModalButton');
    };

    $scope.cancelFixity = function() {
      $scope.fixityReport = {};
      $scope.closeModal();
    };

    $scope.startTransaction = function() {
      $scope.context.ir.startTransaction();
    };

    $scope.commitTransaction = function() {
      $scope.submitClicked = true;
      $scope.context.ir.commitTransaction().then(function() {
        $scope.closeModal();
        $scope.submitClicked = false;
      });
    };

    $scope.rollbackTransaction = function() {
      $scope.submitClicked = true;
      $scope.context.ir.rollbackTransaction().then(function() {
        $scope.closeModal();
        $scope.submitClicked = false;
      });
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

    $scope.getContentType = function() {
      var contentType = null;
      var backupContentType = null;
      var typeMap = {"jpg":"image/jpeg","png":"image/png","pdf":"application/pdf"};

      for (var i in $scope.context.properties) {
        var triple = $scope.context.properties[i];
        if (triple.predicate.indexOf("#hasMimeType") !== -1) {
          contentType = $filter("removeQuotes")(triple.object);
          break;
        }

        if (triple.predicate.indexOf("#filename") !== -1) {
          var temp = triple.object.split(".");
          temp = temp[temp.length-1];
          backupContentType = temp.substring(0,temp.length-1);
          if (typeMap[backupContentType] !== undefined) {
            backupContentType = typeMap[backupContentType];
          }
        }
      }

      if (!contentType && backupContentType) {
          contentType = backupContentType;
      }
      return contentType;
    };

    $scope.canPreview = function(fileType) {
        var previewable = ['image/png','image/jpeg','image/gif','image/bmp'];
        return (previewable.indexOf(fileType) !== -1);
    };

    $scope.getIIIFUrl = function() {
      if (typeof appConfig.iiifServiceUrl !== 'undefined') {
        var contextProperties = [];
        var iiifUri = null;

        if ($scope.context.resource && $scope.context.hasParent) {
          var parentContext = $scope.context.ir.getContext($scope.context.parent.object);
          if (parentContext.hasParent) {
            var grandParentContext = $scope.context.ir.getContext(parentContext.parent.object);
            contextProperties = grandParentContext.properties;
            iiifUri = grandParentContext.uri;
          }
        } else {
          contextProperties = $scope.context.properties;
          iiifUri = $scope.context.uri;
        }

        if (iiifUri && contextProperties) {
          var hasManifest = false;
          var hasFile = false;
          var isPCDM = false;
          for (var i in contextProperties) {
            var triple = contextProperties[i];
            if (!hasFile) {
              if (triple.predicate.indexOf("#hasFile") !== -1) {
                hasFile = true;
              }
            }
            if (!isPCDM) {
              if (triple.predicate.indexOf("#type") !== -1) {
                  if (triple.object === "http://pcdm.org/models#Object") {
                    isPCDM = true;
                  }
              }
            }
            if (isPCDM && hasFile) {
              hasManifest = true;
              break;
            }
          }
          if (hasManifest) {
            return appConfig.iiifServiceUrl+$scope.ir.type.toLowerCase()+"/presentation/"+iiifUri.replace($scope.ir.rootUri,"");
          }
        }
      }
      return null;
    };

    $scope.resetCreateContainer();
    $scope.resetUploadResource();

  });

});
