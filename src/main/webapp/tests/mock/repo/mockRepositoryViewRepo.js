var dataRepositoryViewRepo1 = [
  dataRepositoryView1,
  dataRepositoryView2,
  dataRepositoryView3
];

var dataRepositoryViewRepo2 = [
  dataRepositoryView3,
  dataRepositoryView2,
  dataRepositoryView1
];

var dataRepositoryViewRepo3 = [
  dataRepositoryView1,
  dataRepositoryView3,
  dataRepositoryView2
];

angular.module("mock.repositoryViewRepo", []).service("RepositoryViewRepo", function($q, RepositoryView) {
  var repo = mockRepo("RepositoryViewRepo", $q, mockRepositoryView, dataRepositoryViewRepo1);

  repo.scaffold = {
    curators: [],
    name: "",
    password: "",
    rootUri: "",
    schemas: [],
    type: {
      gloss: "",
      values: []
    },
    username: ""
  };

  repo.findByName = function (name) {
    // @todo
    return new RepositoryView();
  };

  repo.getTypes = function (types) {
    var mockedTypes = [{
      gloss: "mocked type",
      values: []
    }];
    var payload = {
      types: mockedTypes
    };
    var defer = $q.defer();

    defer.promise.then(function (res) {
      angular.extend(types, mockedTypes);
    });

    // @todo
    return payloadPromise(defer, payload);
  };

  repo.verifyAuth = function (repositoryView) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  repo.verifyContent = function (repositoryView) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  repo.verifyPing = function (repositoryView) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  return repo;
});
