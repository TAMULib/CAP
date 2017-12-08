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
  };

  IRRepo.ready().then(function() {
    $scope.setTable = function () {
      $scope.tableParams = new NgTableParams({
        page: 0,
        count: 10,
        sorting: {
          name: 'asc'
        }
    }, {
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