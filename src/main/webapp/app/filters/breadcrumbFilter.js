
cap.filter("breadcrumb", function() {

    var lastValue = null;

    return function(input,ir) {
        var output = input;
        if(input && ir) {
            if(output === ir.rootUri) {
                output = "Root"
            } else {
                output = input.replace(lastValue, "...")  ;
            } 
        }
        lastValue = input;
        return output;
    }
});