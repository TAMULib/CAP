cap.filter("propertyValue", function ($filter) {
    return function (input) {

        var output = input;

        if (typeof output === 'string') {
            var splitOutput = output.split("^^");
            output = splitOutput[0];
            var typePart = splitOutput[1];
      
            if (output[0] === "\"" && output[output.length - 1] === "\"") {
                output = output.substring(1, output.length - 1);
            }

            if(typePart) {
              var typeIndex = typePart.lastIndexOf("#") !== -1 ? typePart.lastIndexOf("#") : typePart.lastIndexOf("/");
              var type = typePart.substring(typeIndex+1, typePart.length);
              switch(type) {
                case "dateTime": 
                  output = $filter('date')(output, "MM-dd-yy h:mm a");
                break;
              }
            }

        }

        return output;
    };
});