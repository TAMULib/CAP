cap.directive("breadcrumbs", function($rootScope, $q, $route, $filter, IRRepo, BreadcrumbService) {
    return {
        templateUrl: "views/directives/breadcrumbs.html",
        restrict: "E",
        scope: {
            context: "=",
            nav: "&"
        },
        link: function($scope, attr, elem) {
            $scope.breadcrumbs = BreadcrumbService.getBreadcrumbs();
            BreadcrumbService.registerBreadcrumbView($route.current.$$route.templateUrl);
            
            var currentContext = {};

            $scope.trimCrumb = function(crumb) {
                return BreadcrumbService.trimCrumb(crumb);
            };

            var un = $scope.$watch("context", function() {
                if($scope.context) {
                    un();
                    $q.all([
                        IRRepo.ready(),
                        $scope.context.ready()
                    ]).then(function(){
                        currentContext = angular.copy($scope.context);
                        var un = $rootScope.$on("$routeChangeStart", function(e, next, current) {
                            if(BreadcrumbService.isABreadcrumbView(next.$$route.templateUrl)) {
                                BreadcrumbService.updateBreadcrumb(currentContext, next.params.context);
                            } else {
                                BreadcrumbService.clearBreadcrumbs();
                            }   
                            un();
                        });
                    });
                }
            });

        }
    };
  });

  cap.service("BreadcrumbService", function($rootScope, StorageService) {

    var breadcrumbService = this;

    var registeredBreadcrumbView = [];

    var storedCrumbs = angular.fromJson(StorageService.get("breadcrumbs"));

    var lastCrumbUri;
    
    var sendToStorage = function() {
        StorageService.set("breadcrumbs",angular.toJson(breadcrumbs));
    };

    if(!storedCrumbs) {
        storedCrumbs = [];
    }
    var breadcrumbs = storedCrumbs;
    sendToStorage();

    breadcrumbService.trimCrumb = function(crumb) {
        return lastCrumbUri&&crumb&&crumb.includes(lastCrumbUri)?crumb.replace(lastCrumbUri, "..."):crumb;
    }

    breadcrumbService.updateBreadcrumb = function(context, nextContextUri) {
        
        var crumb = {
            name: breadcrumbService.trimCrumb(context.name),
            uri: context.uri
        };

        lastCrumbUri = context.uri;

        var containedIndex = -1;
        for(var i in breadcrumbs) {
            var c = breadcrumbs[i];
            if(c.uri===nextContextUri) {
                containedIndex = i;
                break;
            }
        }
        
        console.log(containedIndex);
        if(containedIndex===-1) {
            breadcrumbs.push(crumb);
            sendToStorage();
        } else {
            breadcrumbs.length = containedIndex;
            sendToStorage();
        }

    };

    breadcrumbService.clearBreadcrumbs = function() {
        breadcrumbs.length = 0;
        sendToStorage();
    };

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