cap.directive("breadcrumbs", function() {
    return {
        templateUrl: "views/directives/breadcrumbs.html",
        restrict: "E",
        scope: {
            context: "="
        },
        link: function($scope, attr, elem) {
            $scope.breadcrumbs = [];

            var currentContext = angular.copy($scope.context);

            while(currentContext.hasParent) {
                currentContext = $scope.context.ir.getContext(currentContext.parent.object);
                $scope.breadcrumbs.unshift(currentContext);
            }

        }
    };
  });
