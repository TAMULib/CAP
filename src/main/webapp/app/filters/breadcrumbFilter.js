cap.filter("breadcrumb", function() {
    return function(input,ir) {
        var output = input;
        if(input && ir) {
            if(output === ir.rootUri) {
                output = "Root"
            } else {
                output = output.replace(ir.rootUri, "");
            }
        }
        return output;
    }
});