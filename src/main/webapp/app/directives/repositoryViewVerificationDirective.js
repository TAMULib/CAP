cap.directive("repositoryViewVerification", function ($q, RepositoryViewRepo) {
  return {
    templateUrl: "views/directives/repositoryViewVerification.html",
    restrict: "E",
    scope: {
      repositoryView: "=",
      results: "="
    },
    link: function ($scope, attr, elem) {
      $scope.$watch('repositoryView', function () {
        $scope.repositoryViewVerifications = [];
      }, true);

      var types = [];
      RepositoryViewRepo.getTypes(types).then(function () {
        $scope.disableVerify = function () {
          var typeIsVerifying = false;
          for (var i in types) {
            var type = types[i];
            if (type.value === $scope.repositoryView.type) {
              typeIsVerifying = type.verifying === true;
            }
          }
          return $scope.repositoryView.rootUri && !typeIsVerifying;
        };
      });

      $scope.verifyRepositroyViewConnection = function () {

        $scope.results.status = false;

        $scope.repositoryViewVerifications = [
          {
            name: "Pinging " + $scope.repositoryView.rootUri,
            key: "verifyingPing",
            execute: RepositoryViewRepo.verifyPing,
            status: "PENDING"
          }
        ];

        if ($scope.repositoryView.username && $scope.repositoryView.password) {
          $scope.repositoryViewVerifications.push({
            name: "Verifyinging Authentication",
            key: "verifyingAuth",
            execute: RepositoryViewRepo.verifyAuth,
            status: "PENDING"
          });
        }

        $scope.repositoryViewVerifications.push({
          name: "Retrieving Top Level Content",
          key: "VerifyingContent",
          execute: RepositoryViewRepo.verifyContent,
          status: "PENDING"
        });

        var chain = $q.when();
        angular.forEach($scope.repositoryViewVerifications, function (verification) {
          chain = chain.then(function () {
            return verification.execute($scope.repositoryView).then(function (res) {
              verification.status = angular.fromJson(res.body).meta.status;
            });
          });
        });
        chain.then(function () {
          var status = "SUCCESS";
          for (var i in $scope.repositoryViewVerifications) {
            var verification = $scope.repositoryViewVerifications[i];
            if (verification.status !== status) {
              status = verification.status;
              break;
            }
          }
          $scope.results.status = status;
        });

      };
    }
  };
});
