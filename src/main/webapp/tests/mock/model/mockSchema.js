var dataSchema1 = {
  id: 1,
  abbreviation: "mock abbreviation 1",
  name: "mock schema 1",
  namespace: "mock namespace 1",
  properties: []
};

var dataSchema2 = {
  id: 2,
  abbreviation: "mock abbreviation 2",
  name: "mock schema 2",
  namespace: "mock namespace 2",
  properties: []
};

var dataSchema3 = {
  id: 3,
  abbreviation: "mock abbreviation 3",
  name: "mock schema 3",
  namespace: "mock namespace 3",
  properties: []
};

var mockSchema = function($q) {
  var model = mockModel("Schema", $q, dataSchema1);

  return model;
};

angular.module("mock.schema", []).service("Schema", mockSchema);
