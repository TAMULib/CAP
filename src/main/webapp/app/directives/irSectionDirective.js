cap.directive("irsection", function(IrSectionService) {
    return {
        templateUrl: "views/directives/irSection.html",
        restrict: "E",
        transclude: true,
        scope: {
          ir: "=",
          title: "=",
          type: "=",
          list: "=",
          listElementAction: "&",
          addAction: "&",
          removeAction: "&"
        },
        link: function($scope, elem, attr, ctrl, transclude) {

          transclude($scope, function(clone, $scope) {
            elem.find('.transclude').replaceWith(clone);
          });
            
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

          $scope.getListLength = function() {
            var l= 0;
            if($scope.list) {
              l = Array.isArray($scope.list) ? $scope.list.length : Object.keys($scope.list).length;
            }
            return l;
          };

          var un = $scope.$watchCollection("list", function() {
            if($scope.getListLength()) {
              $scope.isArray = Array.isArray($scope.list);
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