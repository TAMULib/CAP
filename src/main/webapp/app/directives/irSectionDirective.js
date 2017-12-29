cap.directive("irsection", function() {
    return {
        templateUrl: "views/directives/irSection.html",
        restrict: "E",
        scope: {
            ir: "=",
            title: "=",
            type: "=",
            list: "=",
            listElementAction: "&"
        },
        link: function($scope, attr, elem) {
            $scope.selectedListElements = [];
        }
    }
});