cap.repo("SchemaRepo", function(WsApi, HttpMethodVerbs) {
  var schemaRepo = this;

  schemaRepo.scaffold = {
    name: "Dubin Core",
    abbreviation: "DC",
    namespace: "http://purl.org/dc/terms/",
    properties: []
  }

  schemaRepo.findProperties = function(schema) {
    var findPropertiesPromise = WsApi.fetch(schemaRepo.mapping.findProperties, {
      method: HttpMethodVerbs.GET,
      query: {
        namespace: schema.namespace
      },
    });
    return findPropertiesPromise;
  };

  return schemaRepo;
});
