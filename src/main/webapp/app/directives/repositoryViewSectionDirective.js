cap.directive("repositoryViewSection", function($controller, RepositoryViewSectionService) {
    return {
        templateUrl: "views/directives/repositoryViewSection.html",
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

          angular.extend(this, $controller('AbstractAppController', {
              $scope: $scope
          }));

          $scope.contextScope = $scope.$parent.$parent;

          transclude($scope, function(clone, $scope) {
            elem.find('.transclude').replaceWith(clone);
          });

          $scope.checkedPredicates = {};

          $scope.selectedListElements = [];

          $scope.manuallyCollapse = function() {
            $scope.contentExpanded = false;
            RepositoryViewSectionService.setManuallyCollapsed($scope.title, true);
          };

          $scope.manuallyExpand = function() {
            $scope.contentExpanded = true;
            RepositoryViewSectionService.setManuallyCollapsed($scope.title, false);
          };

          $scope.selectAll = function(array) {
            angular.extend($scope.selectedListElements, array);
          };

          $scope.findByPredicate = function(predicate) {
            var selectedList = [];

            angular.forEach($scope.list, function(triple, key) {
              if (triple.predicate === predicate) {
                selectedList.push($scope.list[key]);
              }
            });

            return selectedList;
          };

          $scope.checkPredicate = function(predicate) {
            if ($scope.checkedPredicates[predicate]) {
              $scope.checkedPredicates[predicate] = false;
              $scope.unselectList();
            } else {
              $scope.checkedPredicates[predicate] = true;
              $scope.selectByPredicate(predicate);
            }

            return $scope.checkedPredicates[predicate];
          };

          $scope.isPredicateChecked = function(predicate) {
            return $scope.checkedPredicates[predicate] === true;
          };

          $scope.selectByPredicate = function(predicate) {
            var selectedList = $scope.findByPredicate(predicate);

            if (selectedList.length > 0) {
              angular.extend($scope.selectedListElements, selectedList);
              $scope.checkedPredicates[predicate] = true;
            }
          };

          $scope.unselectList = function() {
            $scope.checkedPredicates = {};
            $scope.selectedListElements.length = 0;
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
            }, function(reason) {
              console.warn(reason);
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

          // TODO: provide a better solution than using a watch or remove entirely if adding manual refresh buttons.
          $scope.$watch("list", function(newList, oldList) {
            if (oldList !== undefined) {
              if (newList.length == oldList.length) {
                angular.forEach(oldList, function (value, key) {
                  if (value != newList[key]) {
                    $scope.unselectList();
                  }
                });
              } else {
                $scope.unselectList();
              }
            }
          }, true);

          var un = $scope.$watchCollection("filteredList||list", function() {
            if($scope.getListLength()) {
              $scope.isArray = Array.isArray($scope.list);
              $scope.contentExpanded = RepositoryViewSectionService.getManuallyCollapsed($scope.title) ? false : true;
              un();
            }
          });
        }
    };
});

cap.service("RepositoryViewSectionService", function() {

    var repositoryViewSectionService = this;

    var manuallyCollapsed = {};

    repositoryViewSectionService.setManuallyCollapsed = function(title, collapsed) {
      manuallyCollapsed[title] = collapsed;
    };

    repositoryViewSectionService.getManuallyCollapsed = function(title) {
      return manuallyCollapsed[title];
    };

});
