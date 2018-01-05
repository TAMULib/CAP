cap.directive("breadcrumbs", function(IRRepo, $q, $route, BreadcrumbService) {
    return {
      templateUrl: "views/directives/breadcrumbs.html",
      restrict: "E",
      scope: {
          ir: "=",
          nav: "&"
      },
      link: function($scope, attr, elem) {
        $scope.breadcrumbs = BreadcrumbService.getBreadcrumbs();
        BreadcrumbService.registerBreadcrumbView($route.current.$$route.templateUrl);
      }
    };
  });

  cap.service("BreadcrumbService", function($rootScope, StorageService) {

    var breadcrumbService = this;

    var registeredBreadcrumbView = [];

    var storedCrumbs = angular.fromJson(StorageService.get("breadcrumbs"));
    
    var sendToStorage = function() {
        StorageService.set("breadcrumbs",angular.toJson(breadcrumbs));
    };

    if(!storedCrumbs) {
        storedCrumbs = [];
    }
    var breadcrumbs = storedCrumbs;
    sendToStorage();

    $rootScope.$on("$routeChangeStart", function(e, next, current) {
        if(breadcrumbService.isABreadcrumbView(next.$$route.templateUrl)) {
            var context = current.params.context;
            var nextContext = next.params.context;
            var containedIndex = breadcrumbs.indexOf(nextContext);
            if(context && containedIndex===-1) {
                breadcrumbs.push(context);
                sendToStorage();
            } else if(context) {
                breadcrumbs.length = containedIndex;
                sendToStorage();
            }
        } else {
            breadcrumbs.length = 0;
            sendToStorage();
        }
    });

    breadcrumbService.getBreadcrumbs = function() {
        return breadcrumbs;
    };

    breadcrumbService.registerBreadcrumbView = function(view) {
        registeredBreadcrumbView.push(view);
    };

    breadcrumbService.isABreadcrumbView = function(view) {
        var found = false;
        for(var i in registeredBreadcrumbView) {
            var registeredView = registeredBreadcrumbView[i];
            if(registeredView===view) {
                found = true;
                break;
            }
        }
        return found;
    };

  });