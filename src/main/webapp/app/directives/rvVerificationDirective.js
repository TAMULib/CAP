cap.directive("rvverification", function(RVRepo, $q) {
  return {
    templateUrl: "views/directives/rvVerification.html",
    restrict: "E",
    scope: {
        rv: "=",
        results: "="
    },
    link: function($scope, attr, elem) {
      $scope.$watch('ir',function() {
        $scope.rvVerifications = [];
      },true);

      var types = [];
      RVRepo.getTypes(types).then(function() {
        $scope.disableVerify = function() {
          var typeIsVerifying = false;
          for(var i in types) {
            var type = types[i];
            if(type.value===$scope.rv.type) {
              typeIsVerifying = type.verifying===true;
            }
          }
          return $scope.rv.rootUri && !typeIsVerifying;
        };
      });

      $scope.verifyRvConnection = function() {

        $scope.results.status = false;

        $scope.rvVerifications = [
          {
            name: "Pinging "+$scope.rv.rootUri,
            key: "verifyingPing",
            execute: RVRepo.verifyPing,
            status: "PENDING"
          }
        ];

        if($scope.rv.username && $scope.rv.password) {
          $scope.rvVerifications.push({
            name: "Verifyinging Authentication",
            key: "verifyingAuth",
            execute: RVRepo.verifyAuth,
            status: "PENDING"
          });
        }

        $scope.rvVerifications.push({
          name: "Retrieving Top Level Content",
          key: "VerifyingContent",
          execute: RVRepo.verifyContent,
          status: "PENDING"
        });

        var chain = $q.when();
        angular.forEach($scope.rvVerifications, function (verification) {
          chain = chain.then(function() {
            return verification.execute($scope.rv).then(function(res) {
              verification.status = angular.fromJson(res.body).meta.status;
            });
          });
        });
        chain.then(function() {
          var status = "SUCCESS";
          for(var i in $scope.rvVerifications) {
            var verification = $scope.rvVerifications[i];
            if(verification.status!==status) {
              status=verification.status;
              break;
            }
          }
          $scope.results.status = status;
        });

      };
    }
  };
});
