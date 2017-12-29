cap.directive("irsection", function() {
    return {
        templateUrl: "views/directives/irSection.html",
        restrict: "E",
        scope: {
            ir: "=",
            title: "=",
            type: "=",
            list: "=",
            listElementAction: "&",
            addAction: "&",
            removeAction: "&"
        },
        link: function($scope, attr, elem) {
            $scope.selectedListElements = [];
            $scope.confirmDelete = function() {
                $scope.removeAction({"items": $scope.selectedListElements})
                $scope.removeListElements=false;
                $scope.selectedListElements.length=0;          
            }

            var un = $scope.$watch("list.length", function(newLength, oldLength) {
                if(newLength>0) {
                    $scope.contentExpanded = true;
                    un();
                }
            });

        }
    }
});