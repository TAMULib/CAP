cap.directive('removeQuotes', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, ngModelController) {
            ngModelController.$parsers.push(function(fromView) {

                var toModel = "\""+fromView+"\"";

                return toModel; //converted

            });

            ngModelController.$formatters.push(function(fromModel) {
                var toView = "";
                if(fromModel&&fromModel[0] === "\"" && fromModel[fromModel.length-1]==="\"") {
                    toView = fromModel.substring(1, fromModel.length - 1);
                }
                
                return toView; //converted

            });
        }
    };
});