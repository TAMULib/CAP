cap.controller("DashboardController", function($controller, $scope, NgTableParams, ApiResponseActions, IRRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));


  $scope.irs = IRRepo.getAll();

  $scope.createIr = function(name, uri) {
    console.log(name, uri);
    IRRepo.create({
      name: name,
      uri: uri
    });
    $scope.closeModal();
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
    console.log($scope.irs);
    $scope.setTable();
  });

  IRRepo.listen([ApiResponseActions.CREATE, ApiResponseActions.DELETE], function (arg) {
    console.log("HEARD IT", arg);
    $scope.setTable();
  });

});