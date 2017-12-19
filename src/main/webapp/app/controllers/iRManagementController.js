cap.controller("IrManagementController", function($controller, $scope, $location, NgTableParams, ApiResponseActions, IRRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.irs = IRRepo.getAll();  

  var blankIr = {
    name: "",
    uri: ""
  }
  $scope.irToCreate = angular.copy(blankIr);
  $scope.irToDelete = {};
  $scope.irToEdit = {};

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
    $scope.closeModal();    
  };

  $scope.resetIrForms();  

  $scope.createIr = function() {
    IRRepo.create($scope.irToCreate).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.cancelCreateIr();
      }
    });
  };
  
  $scope.cancelCreateIr = function() {
    angular.extend($scope.irToCreate, blankIr);
    $scope.resetIrForms();
  }

  $scope.viewIr = function(ir) {
    $location.path("ir/"+ir.name);
  };

  $scope.editIr = function(ir) {
    $scope.irToEdit = ir;
    $scope.openModal('#irEditModal');
  };

  $scope.updateIr = function() {
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

  IRRepo.ready().then(function() {
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