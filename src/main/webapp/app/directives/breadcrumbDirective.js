cap.directive("breadcrumbs", function() {
    return {
        templateUrl: "views/directives/breadcrumbs.html",
        restrict: "E",
        scope: {
            context: "="
        },
        link: function($scope, attr, elem) {
            $scope.breadcrumbs = [];

            $scope.trimName = function(context, index) {
              var name = context.name;
              if (context.name.startsWith(context.ir.rootUri)) {
                name = name.replace(context.ir.rootUri, '...');
              }
              if(index) {
                var prev = $scope.breadcrumbs.length > 0 ? $scope.breadcrumbs[index - 1] : undefined;
                if(prev) {
                  var prevName = prev.name.replace(context.ir.rootUri, '...');
                  name = name.replace(prevName, '...');
                }
                if(index === $scope.breadcrumbs.length) {
                  if(name.startsWith('...')) {
                    name = name.replace('...', '');
                  }
                  if(name.startsWith('/')) {
                    name = name.replace('/', '');
                  }
                }
              }
              return name;
            };

            var getParent = function(context) {
                var parentContext = $scope.context.ir.getContext(context.parent.object);
                if(parentContext.parent) {
                    if(parentContext.hasParent) {
                        getParent(parentContext);
                    }
                    $scope.breadcrumbs.push(parentContext);
                 } else {
                    parentContext.ready().then(function(contexts) {
                        // TODO: figure out why there are three promises resolved here
                        var nextContext = contexts[0];
                        if (nextContext.hasParent) {
                            getParent(nextContext);
                        }
                        $scope.breadcrumbs.unshift(nextContext);
                    });
                }
                return parentContext;
            };

            var currentContext = angular.copy($scope.context);

            if (currentContext.hasParent) {
                getParent(currentContext);
            }

        }
    };
  });
