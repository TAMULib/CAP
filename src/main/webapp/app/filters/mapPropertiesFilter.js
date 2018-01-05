
cap.filter("mapProperties", function() {

  var output = {};

  return function(input) {
    var prepOutput = {};
    angular.forEach(input, function(triple) {
      if(!prepOutput[triple.predicate]) prepOutput[triple.predicate] = [];
      prepOutput[triple.predicate].push(triple.object);
    });

    if(angular.toJson(prepOutput)!==angular.toJson(output)) {
      angular.extend(output, prepOutput);
    }

    return output;
  };
});