cap.controller("IrManagementController", function($controller, $scope, $q, $location, $timeout, NgTableParams, ApiResponseActions, IRRepo, SchemaRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.irs = IRRepo.getAll();  
  $scope.iRTypes = [];
  
  IRRepo.getTypes($scope.iRTypes).then(function() {
    $scope.irToCreate = IRRepo.getScaffold({
      type: $scope.iRTypes[0].value
    });

    $scope.$watch('irToCreate',function() {
      resetVerification();
    },true);

    $scope.$watch('irToEdit',function() {
      resetVerification();
    },true);

    $scope.disableVerify = function(activeIr) {
      var typeIsVerifying = false;
      for(var i in $scope.iRTypes) {
        var type = $scope.iRTypes[i];
        if(type.value===activeIr.type) {
          typeIsVerifying = type.verifying===true;
        }
      }
      return activeIr.rootUri && !typeIsVerifying;
    };
  });

  var resetVerification = function(force) {
    if (force || $scope.verificationResults.status === 'SUCCESS') {
      $scope.verificationResults = {};
    }
  };  
  
  $scope.irToDelete = {};
  $scope.irToEdit = {};
  resetVerification(true);

  $scope.irForms = {
    validations: IRRepo.getValidations(),
    getResults: IRRepo.getValidationResults
  };

  $scope.resetIrForms = function() {
    IRRepo.clearValidationResults();
    for (var key in $scope.irForms) {
      if ($scope.irForms[key] !== undefined && !$scope.irForms[key].$pristine && $scope.irForms[key].$setPristine) {
        $scope.irForms[key].$setPristine();
      }
    }
    resetVerification(true);
    delete $scope.irToVerify;
    $scope.closeModal();    
  };

  $scope.resetIrForms(); 
  
  $scope.startCreate = function() {
    $scope.schemas = SchemaRepo.getAll();
    $scope.openModal("#createIRModal");
  };

  $scope.createIr = function() {
    IRRepo.create($scope.irToCreate).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.cancelCreateIr();
      }
    });
  };
  
  $scope.cancelCreateIr = function() {
    angular.extend($scope.irToCreate, IRRepo.getScaffold());
    $scope.resetIrForms();
  };

  $scope.editIr = function(ir) {
    $scope.schemas = SchemaRepo.getAll();
    $scope.irToEdit = ir;
    $scope.openModal('#irEditModal');
  };

  $scope.updateIr = function() {
    $scope.irToEdit.dirty(true);
    $scope.irToEdit.save().then(function() {
      $scope.cancelEditIr();
    });
  };

  $scope.cancelEditIr = function(ir) {
    $scope.irToEdit.refresh();
    $scope.irToEdit = {};        
    $scope.resetIrForms();
  };

  $scope.confirmDeleteIr = function(ir) {
    $scope.irToDelete = ir;
    $scope.openModal('#irDeleteModal');
  };

  $scope.cancelDeleteIr = function(ir) {
    $scope.irToDelete = {};        
    $scope.closeModal();
  };

  $scope.deleteIr = function(ir) {
    IRRepo.delete(ir).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.cancelDeleteIr();
      }
    });
  };

  $scope.showSchemas = function(schemas) {
    $scope.schemasToShow = schemas;
    $scope.openModal("#showSchemasModal");
  };

  $q.all([
    IRRepo.ready(),
    SchemaRepo.ready()
  ]).then(function() {
    $scope.setTable = function () {
      $scope.tableParams = new NgTableParams({
        count: $scope.irs.length,
        sorting: {
          name: 'asc'
        }
    }, {
      counts: [],
      total: 0,
      getData: function (params) {
        return $scope.irs;
        }
      });
    };
    $scope.setTable();
  });

  IRRepo.listen([ApiResponseActions.CREATE, ApiResponseActions.DELETE], function (arg) {
    $scope.setTable();
  });

});