cap.filter("propertyValue", function() {
    return function(input) {

        var output = input;

        if(typeof output  === 'string') {
            var output = output.split("^^")[0];
            //output.lastIndexOf("#") !== -1 ? output.lastIndexOf("#") : output.lastIndexOf("/");

            if(output[0]==="\"" && output[output.length-1] === "\"") {
                output = output.substring(1, output.length-1);
            }

        }

        return output;
    };
});