cap.filter("metadataLabel", function () {
    return function (input) {

        var output = input;

        if (typeof output === 'string') {
            var index = output.lastIndexOf("#") !== -1 ? output.lastIndexOf("#") : output.lastIndexOf("/");
            output = output.substring(index + 1, output.length);
        }

        return output;
    };
});