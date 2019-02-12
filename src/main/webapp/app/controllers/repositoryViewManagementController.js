cap.controller("RepositoryViewManagementController", function($controller, $scope, $q, $location, $timeout, NgTableParams, ApiResponseActions, RepositoryViewRepo, SchemaRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.repositoryViews = RepositoryViewRepo.getAll();
  $scope.repositoryViewTypes = [];

  $scope.submitClicked = false;

  RepositoryViewRepo.getTypes($scope.repositoryViewTypes).then(function() {
    $scope.repositoryViewToCreate = RepositoryViewRepo.getScaffold({
      type: $scope.repositoryViewTypes[0].value
    });

    $scope.$watch('repositoryViewToCreate',function() {
      resetVerification();
    },true);

    $scope.$watch('repositoryViewToEdit',function() {
      resetVerification();
    },true);

    $scope.disableVerify = function(activeRepositoryView) {
      var typeIsVerifying = false;
      for(var i in $scope.repositoryViewTypes) {
        var type = $scope.repositoryViewTypes[i];
        if(type.value===activeRepositoryView.type) {
          typeIsVerifying = type.verifying===true;
        }
      }
      return activeRepositoryView.rootUri && !typeIsVerifying;
    };
  });

  var resetVerification = function(force) {
    if (force || $scope.verificationResults.status === 'SUCCESS') {
      $scope.verificationResults = {};
    }
  };

  $scope.repositoryViewToDelete = {};
  $scope.repositoryViewToEdit = {};
  resetVerification(true);

  $scope.repositoryViewForms = {
    validations: RepositoryViewRepo.getValidations(),
    getResults: RepositoryViewRepo.getValidationResults
  };

  $scope.resetRepositoryViewForms = function() {
    RepositoryViewRepo.clearValidationResults();
    for (var key in $scope.repositoryViewForms) {
      if ($scope.repositoryViewForms[key] !== undefined && !$scope.repositoryViewForms[key].$pristine && $scope.repositoryViewForms[key].$setPristine) {
        $scope.repositoryViewForms[key].$setPristine();
      }
    }
    resetVerification(true);
    delete $scope.repositoryViewToVerify;
    $scope.closeModal();
  };

  $scope.resetRepositoryViewForms();

  $scope.startCreate = function() {
    $scope.schemas = SchemaRepo.getAll();
    $scope.openModal("#createRepositoryViewModal");
  };

  $scope.createRepositoryView = function() {
    $scope.submitClicked = true;
    RepositoryViewRepo.create($scope.repositoryViewToCreate).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.cancelCreateRepositoryView();
      }
      $scope.submitClicked = false;
    });
  };

  $scope.cancelCreateRepositoryView = function() {
    angular.extend($scope.repositoryViewToCreate, RepositoryViewRepo.getScaffold());
    $scope.resetRepositoryViewForms();
  };

  $scope.editRepositoryView = function(repositoryView) {
    $scope.schemas = SchemaRepo.getAll();
    $scope.repositoryViewToEdit = repositoryView;
    $scope.openModal('#repositoryViewEditModal;');
  };

  $scope.updateRepositoryView = function() {
    $scope.submitClicked = true;
    $scope.repositoryViewToEdit.dirty(true);
    $scope.repositoryViewToEdit.save().then(function() {
      $scope.cancelEditRepositoryView();
      $scope.submitClicked = false;
    });
  };

  $scope.cancelEditRepositoryView = function(repositoryView) {
    $scope.repositoryViewToEdit.refresh();
    $scope.repositoryViewToEdit = {};
    $scope.resetRepositoryViewForms();
  };

  $scope.confirmDeleteRepositoryView = function(repositoryView) {
    $scope.repositoryViewToDelete = repositoryView;
    $scope.openModal('#repositoryViewDeleteModal');
  };

  $scope.cancelDeleteRepositoryView = function(repositoryView) {
    $scope.repositoryViewToDelete = {};
    $scope.closeModal();
  };

  $scope.deleteRepositoryView = function(repositoryView) {
    $scope.submitClicked = true;
    RepositoryViewRepo.delete(repositoryView).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.cancelDeleteRepositoryView();
        $scope.submitClicked = false;
      }
    });
  };

  $scope.showSchemas = function(schemas) {
    $scope.schemasToShow = schemas;
    $scope.openModal("#showSchemasModal");
  };

  $q.all([
    RepositoryViewRepo.ready(),
    SchemaRepo.ready()
  ]).then(function() {
    $scope.setTable = function () {
      $scope.tableParams = new NgTableParams({
        count: $scope.repositoryViews.length,
        sorting: {
          name: 'asc'
        }
    }, {
      counts: [],
      total: 0,
      getData: function (params) {
        return $scope.repositoryViews;
        }
      });
    };
    $scope.setTable();
  });

  RepositoryViewRepo.listen([ApiResponseActions.CREATE, ApiResponseActions.DELETE], function (arg) {
    $scope.setTable();
  });

});
