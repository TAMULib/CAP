cap.controller("DashboardController", function($controller, $scope, $location, NgTableParams, ApiResponseActions, IRRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.newIr = {};
  $scope.irToDelete = {};
  $scope.irToEdit = {};

  $scope.irs = IRRepo.getAll();

  $scope.irForm = {
    validations: IRRepo.getValidations(),
    getResults: IRRepo.getValidationResults
  };

  $scope.createIr = function() {
    IRRepo.create($scope.newIr).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.resetCreateForm();
      }
    });
  };

  $scope.viewIr = function(ir) {
    $location.path("ir/"+ir.name);
  };

  $scope.editIr = function(ir) {
    console.log("edit");
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

  $scope.resetCreateForm = function() {

    angular.extend($scope.newIr, {
      name: "",
      uri: ""
    });
    IRRepo.clearValidationResults();    
    for (var key in $scope.irForm) {
      if ($scope.irForm[key] !== undefined && !$scope.irForm[key].$pristine && $scope.irForm[key].$setPristine) {
        $scope.irForm[key].$setPristine();
      }
    }

    $scope.closeModal();
  };

  $scope.resetCreateForm();

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