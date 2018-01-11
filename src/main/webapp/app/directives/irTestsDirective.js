cap.directive("irtests", function (IRRepo, $q) {
    return {
        templateUrl: "views/directives/irTests.html",
        restrict: "E",
        scope: {
            ir: "=",
            results: "="
        },
        link: function ($scope, attr, elem) {
            $scope.testIrConnection = function () {

                $scope.results.status = false;

                $scope.irTests = [{
                    name: "Pinging " + $scope.ir.rootUri,
                    key: "testingPing",
                    execute: IRRepo.testPing,
                    status: "PENDING"
                }];

                if ($scope.ir.username && $scope.ir.password) {
                    $scope.irTests.push({
                        name: "Testing Authentication",
                        key: "testingAuth",
                        execute: IRRepo.testAuth,
                        status: "PENDING"
                    });
                }

                $scope.irTests.push({
                    name: "Retrieving Top Level Content",
                    key: "testingContent",
                    execute: IRRepo.testContent,
                    status: "PENDING"
                });

                var chain = $q.when();
                angular.forEach($scope.irTests, function (test) {
                    chain = chain.then(function () {
                        return test.execute($scope.ir).then(function (res) {
                            test.status = angular.fromJson(res.body).meta.status;
                        });
                    });
                });
                chain.then(function () {
                    var status = "SUCCESS";
                    for (var i in $scope.irTests) {
                        var test = $scope.irTests[i];
                        if (test.status !== status) {
                            status = test.status;
                            break;
                        }
                    }
                    $scope.results.status = status;
                });

            };
        }
    };
});