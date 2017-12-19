cap.controller("IrManagementController", function($controller, $scope, $q, $location, $timeout, NgTableParams, ApiResponseActions, IRRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.irs = IRRepo.getAll();  

  $scope.irToCreate = IRRepo.getScaffold();
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
    delete $scope.irToTest;
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
    angular.extend($scope.irToCreate, IRRepo.getScaffold());
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

  $scope.testIrConnection = function(irToTest) {
    $scope.irToTest = angular.copy(irToTest);
    
    $scope.irToTest={
      testingStatus: "PENDING",
      testingPing: "PENDING",
      testingAuth: "PENDING",
      testingContent: "PENDING"
    }
    
    IRRepo.testPing($scope.irToTest).then(function(pingRes) {
      $scope.irToTest.testingPing = angular.fromJson(pingRes.body).meta.status;
      return IRRepo.testAuth($scope.irToTest).then(function(authRes) {
        $scope.irToTest.testingAuth = angular.fromJson(authRes.body).meta.status;
        return IRRepo.testContent($scope.irToTest).then(function(contentRes) { 
          $scope.irToTest.testingContent = angular.fromJson(contentRes.body).meta.status;
          $scope.irToTest.testingStatus = "FINISHED";
        });
      });
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