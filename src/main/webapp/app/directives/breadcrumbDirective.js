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
              if (context.name.indexOf(context.repositoryView.rootUri) === 0) {
                name = name.replace(context.repositoryView.rootUri, '...').replace(/tx:.*\//, '');
              }
              if(index) {
                var prev = $scope.breadcrumbs.length > 0 ? $scope.breadcrumbs[index - 1] : undefined;
                
                if(prev && !context.isVersion) {
                  var prevName = prev.name.replace(context.repositoryView.rootUri, '...');
                  name = name.replace(prevName, '...');
                }
                if(index === $scope.breadcrumbs.length) {
                  if(name.indexOf('...') === 0) {
                    name = name.replace('...', '');
                  }
                  if(name.indexOf('/') === 0) {
                    name = name.replace('/', '');
                  }
                }
              }
              return name;
            };

            var getParent = function(context) {
                var parentContext = $scope.context.repositoryView.getContext(context.parent.object);
                if(parentContext.hasParent) {
                    getParent(parentContext);
                    $scope.breadcrumbs.push(parentContext);
                 } else {
                    parentContext.ready().then(function(nextContext) {
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
