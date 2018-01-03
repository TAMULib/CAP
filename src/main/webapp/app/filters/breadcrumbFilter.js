
cap.filter("breadcrumb", function(IRRepo) {

    var lastValue = null;

    var titles = {};

    return function(input,ir) {
        var output = input;
        // console.log(!titles[input]);
        if(!titles[input]) {
            titles[input] = true;
            IRRepo.getProperties(ir, input);
        }

        if(input && ir) {
            if(output === ir.rootUri) {
                output = "Root";
            } else {
                output = input.replace(lastValue, "...")  ;
            } 
        }
        lastValue = input;
        return output;
    };
});