cap.directive("contentviewer", function() {
    var viewerMap = appConfig.contentMap;

    return {
        templateUrl: "views/directives/viewers/viewerWrapper.html",
        scope: {
          contentType: "=",
          resource: "="
        },
        link: function($scope) {
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
            $scope.includeTemplateUrl = "views/directives/viewers/"+viewerTemplate+"Viewer.html";

        },
        restrict: "E",
        transclude: true
    };
});