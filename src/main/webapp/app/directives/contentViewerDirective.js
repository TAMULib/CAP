cap.directive("contentviewer", function ($filter, $sce) {
  var viewerMap = appConfig.contentMap;

  return {
    templateUrl: "views/directives/viewers/viewerWrapper.html",
    scope: {
      contentType: "=",
      resource: "="
    },
    link:
      function ($scope) {
        var viewerTemplate = "default";

        typeLoop:
        for (var type in viewerMap) {
          for (var supportedType in viewerMap[type]) {
            if ($scope.contentType === viewerMap[type][supportedType]) {
              viewerTemplate = type;
              break typeLoop;
            }
          }
        }

        if (viewerTemplate == 'seadragon') {
          $scope.options = {};
          $scope.options.prefixUrl = 'resources/images/';
          $scope.options.tileSources = [$filter("cantaloupeUrl")($scope.resource)];
        }
        $scope.includeTemplateUrl = "views/directives/viewers/" + viewerTemplate + "Viewer.html";

        $scope.getTrustedResource = function (src) {
          return $sce.trustAsResourceUrl("./resource-proxy/?uri=" + src);
        };
      },
    restrict: "E",
    transclude: true
  };
});