cap.filter("schemaNamespace", function () {
  return function (predicate, schemas) {
    var output = predicate;

    if (schemas) {
      for (var i = 0; i < schemas.length; i++) {
        if (predicate.indexOf(schemas[i].namespace) >= 0) {
          output = schemas[i].name + " (" + schemas[i].abbreviation + ")";
          break;
        }
      }
    }

    return output;
  };
});
