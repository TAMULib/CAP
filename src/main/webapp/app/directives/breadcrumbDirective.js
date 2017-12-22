cap.directive("breadcrumbs", function(IRRepo, $q, breadcrumbService) {
    return {
      templateUrl: "views/directives/breadcrumbs.html",
      restrict: "E",
      scope: {
          ir: "=",
          nav: "&"
      },
      link: function($scope, attr, elem) {
        $scope.breadcrumbs = breadcrumbService.getBreadcrumbs();

      }
    };
  });

  cap.service("breadcrumbService", function($rootScope, StorageService) {

    var storedCrumbs = angular.fromJson(StorageService.get("breadcrumbs"));
    console.log(storedCrumbs);
    var sendToStorage = function() {
        StorageService.set("breadcrumbs",angular.toJson(breadcrumbs));
    }

    if(!storedCrumbs) {
        storedCrumbs = [];
    }
    var breadcrumbs = storedCrumbs;
    sendToStorage();

    var un = $rootScope.$on("$routeChangeStart", function(e, next, current) {
        
        var context = current.params.context;
        var nextContext = next.params.context;
        var containedIndex = breadcrumbs.indexOf(nextContext);
        if(containedIndex===-1) {
            breadcrumbs.push(context);
            sendToStorage();
        } else {
            breadcrumbs.length = containedIndex;
            sendToStorage();
        }

        
            
    });

    this.getBreadcrumbs = function() {
        return breadcrumbs;
    }

  });