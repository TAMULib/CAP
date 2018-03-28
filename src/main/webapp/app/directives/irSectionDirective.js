cap.directive("irsection", function($controller, $timeout, IrSectionService) {
    return {
        templateUrl: "views/directives/irSection.html",
        restrict: "E",
        transclude: true,
        scope: {
          context: "=",
          title: "=",
          type: "=",
          list: "=",
          listElementAction: "&",
          addAction: "&",
          removeAction: "&",
          editAction: "&"
        },
        link: function($scope, elem, attr, ctrl, transclude) {
          
          angular.extend(this, $controller('CoreAdminController', {
              $scope: $scope
          }));

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

          $scope.selectAll = function(array) {
            angular.extend($scope.selectedListElements, array);
          };

          $scope.confirmDelete = function() {
            $scope.removeAction({"items": $scope.selectedListElements}).then(function() {
              $scope.removeListElements=false;
              $scope.selectedListElements.length=0;          
            });
          };

          $scope.editItem = function(argObj,editValue) {
            $scope.editWorking = true;
            $scope.editAction(argObj).then(function() {
              $scope.editWorking = false;
            });
          };

          $scope.getListLength = function() {
            var l= 0;
            var list = $scope.filteredList||$scope.list;
            if(list) {
              l = Array.isArray(list) ? list.length : Object.keys(list).length;
            }
            return l;
          };

          var un = $scope.$watchCollection("filteredList||list", function() {
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