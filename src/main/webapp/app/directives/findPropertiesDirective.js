cap.directive("findproperties", function(SchemaRepo) {
    return  {
        templateUrl: "views/directives/findProperties.html",
        restrict: "E",
        scope: {
            schema: "=",
            mode: "=",
        },
        link: function($scope) {

            $scope.properties = [];

            $scope.getProperties = function() {
                $scope.gettingProperties = true;
                var getPropertiesPromise = SchemaRepo.findProperties($scope.schema);
                
                getPropertiesPromise.then(function(res) {

                    var un =  $scope.$watch("schema.namespace", function(current, next) {
                        if(current!==next) {
                            $scope.properties.length=0;
                            un();
                        }
                    });

                    var properties = angular.fromJson(res.body).payload['ArrayList<Property>'];
                    angular.extend($scope.properties, properties);
                    $scope.gettingProperties = false;
                });

                return getPropertiesPromise;
            };

            $scope.toggleProp = function(prop) {
                var propIndex = $scope.indexOfProp(prop);
                if(propIndex === -1) {
                    $scope.schema.properties.push(prop);
                } else {
                    $scope.schema.properties.splice(propIndex, 1);
                }
            };

            $scope.toggleAllProps = function() {
                if($scope.schema.properties.length === $scope.properties.length) {
                    $scope.schema.properties.length = 0;
                } else {
                    angular.extend($scope.schema.properties, $scope.properties);
                }
            };

            $scope.indexOfProp = function(prop) {
                var indx = -1;
                for(var i in $scope.schema.properties) {
                    var p = $scope.schema.properties[i];
                    if(prop.uri===p.uri) {
                        indx = i;
                        break;
                    }
                }
                return indx;
            };

            if($scope.mode==="edit") {
                $scope.getProperties();
            }

        }

    };
});