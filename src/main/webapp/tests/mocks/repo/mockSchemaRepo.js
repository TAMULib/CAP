var dataSchemaRepo1 = [
  dataSchema1,
  dataSchema2,
  dataSchema3
];

var dataSchemaRepo2 = [
  dataSchema3,
  dataSchema2,
  dataSchema1
];

var dataSchemaRepo3 = [
  dataSchema1,
  dataSchema3,
  dataSchema2
];

angular.module("mock.schemaRepo", []).service("SchemaRepo", function($q) {
  var repo = mockRepo("SchemaRepo", $q, mockSchema, dataSchemaRepo1);

  repo.scaffold = {
    abbreviation: "",
    name: "",
    namespace: "",
    properties: []
  };

  repo.findProperties = function(schema) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  return repo;
});
