cap.service("AbstractAppRepo", function () {
  
    return function AbstractAppRepo() {

        this.scaffold = {}
        
        this.getScaffold = function() {
            return angular.copy(this.scaffold);
        }

        this.isInScaffold = function(property) {
            var propertyFound = false;
            var scaffoldKeys = Object.keys(this.getScaffold());
            for(var i in scaffoldKeys) {
                var scaffoldProperty = scaffoldKeys[i];
                if(scaffoldProperty === property) {
                    propertyFound= true;
                    break;
                }
            }
            return propertyFound;
        }

        return this;
    };

});