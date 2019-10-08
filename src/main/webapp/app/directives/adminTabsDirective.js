cap.directive("admintabs", function ($location) {
  return {
    templateUrl: "views/directives/adminTabs.html",
    restrict: "E",
    scope: false,
    link: function ($scope) {
      $scope.activeTab = $location.path();
    }
  };
});