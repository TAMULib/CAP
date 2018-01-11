cap.filter("breadcrumb", function (IRRepo) {

    var lastValue = null;

    var titles = {};

    return function (input, ir) {
        var output = input;

        if (!titles[input]) {
            titles[input] = true;
            if (IRRepo.getProperties)
                IRRepo.getProperties(ir, input).then(function (res) {
                    var payload = angular.fromJson(res.body).payload;
                    var metadataMap = payload ? payload.HashMap : {};

                    angular.forEach(metadataMap, function (v, k) {
                        if (k === "http://purl.org/dc/elements/1.1/title") {
                            titles[input] = v[0].replace(/"/g, "");
                        }
                    });
                });
        }

        if (input && ir) {
            if (output === ir.rootUri) {
                output = "Root";
            } else if (typeof titles[input] === "string") {
                output = titles[input];
            } else {
                output = input.replace(lastValue, "...");
            }
        }
        lastValue = input;
        return output;
    };
});