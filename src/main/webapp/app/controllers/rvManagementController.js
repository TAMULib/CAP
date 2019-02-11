cap.controller("RvManagementController", function($controller, $scope, $q, $location, $timeout, NgTableParams, ApiResponseActions, RVRepo, SchemaRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.rvs = RVRepo.getAll();
  $scope.rvTypes = [];

  $scope.submitClicked = false;

  RVRepo.getTypes($scope.rvTypes).then(function() {
    $scope.rvToCreate = RVRepo.getScaffold({
      type: $scope.rvTypes[0].value
    });

    $scope.$watch('rvToCreate',function() {
      resetVerification();
    },true);

    $scope.$watch('rvToEdit',function() {
      resetVerification();
    },true);

    $scope.disableVerify = function(activeRv) {
      var typeIsVerifying = false;
      for(var i in $scope.rvTypes) {
        var type = $scope.rvTypes[i];
        if(type.value===activeRv.type) {
          typeIsVerifying = type.verifying===true;
        }
      }
      return activeRv.rootUri && !typeIsVerifying;
    };
  });

  var resetVerification = function(force) {
    if (force || $scope.verificationResults.status === 'SUCCESS') {
      $scope.verificationResults = {};
    }
  };

  $scope.rvToDelete = {};
  $scope.rvToEdit = {};
  resetVerification(true);

  $scope.rvForms = {
    validations: RVRepo.getValidations(),
    getResults: RVRepo.getValidationResults
  };

  $scope.resetRvForms = function() {
    RVRepo.clearValidationResults();
    for (var key in $scope.rvForms) {
      if ($scope.rvForms[key] !== undefined && !$scope.rvForms[key].$pristine && $scope.rvForms[key].$setPristine) {
        $scope.rvForms[key].$setPristine();
      }
    }
    resetVerification(true);
    delete $scope.rvToVerify;
    $scope.closeModal();
  };

  $scope.resetRvForms();

  $scope.startCreate = function() {
    $scope.schemas = SchemaRepo.getAll();
    $scope.openModal("#createRVModal");
  };

  $scope.createRv = function() {
    $scope.submitClicked = true;
    RVRepo.create($scope.rvToCreate).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.cancelCreateRv();
      }
      $scope.submitClicked = false;
    });
  };

  $scope.cancelCreateRv = function() {
    angular.extend($scope.rvToCreate, RVRepo.getScaffold());
    $scope.resetRvForms();
  };

  $scope.editRv = function(rv) {
    $scope.schemas = SchemaRepo.getAll();
    $scope.rvToEdit = rv;
    $scope.openModal('#rvEditModal');
  };

  $scope.updateRv = function() {
    $scope.submitClicked = true;
    $scope.rvToEdit.dirty(true);
    $scope.rvToEdit.save().then(function() {
      $scope.cancelEditRv();
      $scope.submitClicked = false;
    });
  };

  $scope.cancelEditRv = function(rv) {
    $scope.rvToEdit.refresh();
    $scope.rvToEdit = {};
    $scope.resetRvForms();
  };

  $scope.confirmDeleteRv = function(rv) {
    $scope.rvToDelete = rv;
    $scope.openModal('#rvDeleteModal');
  };

  $scope.cancelDeleteRv = function(rv) {
    $scope.rvToDelete = {};
    $scope.closeModal();
  };

  $scope.deleteRv = function(rv) {
    $scope.submitClicked = true;
    RVRepo.delete(rv).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.cancelDeleteRv();
        $scope.submitClicked = false;
      }
    });
  };

  $scope.showSchemas = function(schemas) {
    $scope.schemasToShow = schemas;
    $scope.openModal("#showSchemasModal");
  };

  $q.all([
    RVRepo.ready(),
    SchemaRepo.ready()
  ]).then(function() {
    $scope.setTable = function () {
      $scope.tableParams = new NgTableParams({
        count: $scope.rvs.length,
        sorting: {
          name: 'asc'
        }
    }, {
      counts: [],
      total: 0,
      getData: function (params) {
        return $scope.rvs;
        }
      });
    };
    $scope.setTable();
  });

  RVRepo.listen([ApiResponseActions.CREATE, ApiResponseActions.DELETE], function (arg) {
    $scope.setTable();
  });

});
