cap.filter("metadataByNamespace", function() {
  return function (metadata, schemas) {
    if (schemas && angular.isArray(schemas)) {
      var uniqueByNamespace = [];
      var namespaces = [];
      angular.forEach(metadata, function (metadatum) {
        var isDuplicate = false;

        var namespace = metadatum.predicate;
        for (var i = 0; i < schemas.length; i++) {
          if (metadatum.predicate.indexOf(schemas[i].namespace) >= 0) {
            namespace = schemas[i].name + " (" + schemas[i].abbreviation + ")";
            break;
          }
        }

        for (var j = 0; j < namespaces.length; j++) {
          if (namespaces.indexOf(namespace) >= 0) {
            isDuplicate = true;
            break;
          }
        }
        if (!isDuplicate) {
          uniqueByNamespace.push(metadatum);
          namespaces.push(namespace);
        }

      });
      metadata = uniqueByNamespace;
    }
    return metadata;
  };
});
