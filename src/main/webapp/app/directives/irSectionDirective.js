cap.directive("irsection", function(IrSectionService) {
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

          $scope.manuallyCollapse = function() {
            $scope.contentExpanded = false;
            IrSectionService.setManuallyCollapsed($scope.title, true);
          };

          $scope.manuallyExpande = function() {
            $scope.contentExpanded = true;
            IrSectionService.setManuallyCollapsed($scope.title, false);
          };

          $scope.confirmDelete = function() {
            $scope.removeAction({"items": $scope.selectedListElements}).then(function() {
              $scope.removeListElements=false;
              $scope.selectedListElements.length=0;          
            });
          };

          var un = $scope.$watch("list.length", function(newLength, oldLength) {
            if(newLength>0) {
              $scope.contentExpanded = IrSectionService.getManuallyCollapsed($scope.title) ? false : true;
              un();
            }
          });

        }
    };
});

cap.service("IrSectionService", function() {

    var irSectionServ = this;

    var manuallyCollapsed = {};

    irSectionServ.setManuallyCollapsed = function(title, collapsed) {
      manuallyCollapsed[title] = collapsed;
    };

    irSectionServ.getManuallyCollapsed = function(title) {
      return manuallyCollapsed[title];
    };

});