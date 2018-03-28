cap.directive("irverification", function(IRRepo, $q) {
  return {
    templateUrl: "views/directives/irVerification.html",
    restrict: "E",
    scope: {
        ir: "=",
        results: "="
    },
    link: function($scope, attr, elem) {
      $scope.$watch('ir',function() {
        $scope.irVerifications = [];
      },true);

      var types = [];
      IRRepo.getTypes(types).then(function() {
        $scope.disableVerify = function() {
          var typeIsVerifying = false;
          for(var i in types) {
            var type = types[i];
            if(type.value===$scope.ir.type) {
              typeIsVerifying = type.verifying===true;
            }
          }
          return $scope.ir.rootUri && !typeIsVerifying;
        };
      });

      $scope.verifyIrConnection = function() {

        $scope.results.status = false;

        $scope.irVerifications = [
          {
            name: "Pinging "+$scope.ir.rootUri,
            key: "verifyingPing",
            execute: IRRepo.verifyPing,
            status: "PENDING"
          }
        ];

        if($scope.ir.username && $scope.ir.password) {
          $scope.irVerifications.push({
            name: "Verifyinging Authentication",
            key: "verifyingAuth",
            execute: IRRepo.verifyAuth,
            status: "PENDING"
          });
        }

        $scope.irVerifications.push({
          name: "Retrieving Top Level Content",
          key: "VerifyingContent",
          execute: IRRepo.verifyContent,
          status: "PENDING"
        });

        var chain = $q.when();
        angular.forEach($scope.irVerifications, function (verification) {
          chain = chain.then(function() {
            return verification.execute($scope.ir).then(function(res) {
              verification.status = angular.fromJson(res.body).meta.status;
            });
          });
        });
        chain.then(function() {
          var status = "SUCCESS";
          for(var i in $scope.irVerifications) {
            var verification = $scope.irVerifications[i];
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
