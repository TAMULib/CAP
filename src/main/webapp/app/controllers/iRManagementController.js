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
  });
  
  $scope.irToDelete = {};
  $scope.irToEdit = {};
  $scope.verificationResults = {};

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
    delete $scope.irToVerify;
    $scope.closeModal();    
  };

  $scope.resetIrForms(); 
  
  $scope.startCreate = function() {
    console.log($scope.iRTypes);
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