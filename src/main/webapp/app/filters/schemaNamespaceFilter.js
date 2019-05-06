cap.filter("schemaNamespace", function() {
  return function(namespace, schemas) {
    var output = namespace;

    if (schemas) {
      for (var i = 0; i < schemas.length; i++) {
        if (schemas[i].namespace === namespace) {
          output = schemas[i].name + " (" + schemas[i].abbreviation + ")";
          break;
        }
      }
    }

    return output;
  };
});
