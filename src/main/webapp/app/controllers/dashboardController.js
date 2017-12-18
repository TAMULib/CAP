cap.controller("DashboardController", function($controller, $scope, NgTableParams, ApiResponseActions, IRRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.newIr = {};

  $scope.irs = IRRepo.getAll();

  $scope.irForm = {
    validations: IRRepo.getValidations(),
    getResults: IRRepo.getValidationResults
  };

  $scope.createIr = function() {
    console.log($scope.newIr);
    IRRepo.create($scope.newIr).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.resetCreateForm();
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