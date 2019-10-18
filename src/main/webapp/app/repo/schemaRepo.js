cap.repo("SchemaRepo", function (WsApi, HttpMethodVerbs) {
  var schemaRepo = this;

  schemaRepo.scaffold = {
    name: "Dublin Core",
    abbreviation: "dc",
    namespace: "http://purl.org/dc/elements/1.1/",
    properties: []
  };

  schemaRepo.findProperties = function (schema) {
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
