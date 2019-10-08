cap.filter("metadataByNamespace", function () {
  return function (metadata, schemas) {
    if (schemas && angular.isArray(schemas)) {
      var uniqueByNamespace = [];
      var namespaces = [];
      angular.forEach(metadata, function (metadatum) {
        var namespace = metadatum.predicate;
        for (var i = 0; i < schemas.length; i++) {
          if (metadatum.predicate.indexOf(schemas[i].namespace) >= 0) {
            namespace = schemas[i].name + " (" + schemas[i].abbreviation + ")";
            break;
          }
        }
        if (namespaces.indexOf(namespace) < 0) {
          uniqueByNamespace.push(metadatum);
          namespaces.push(namespace);
        }
      });
      metadata = uniqueByNamespace;
    }
    return metadata;
  };
});
