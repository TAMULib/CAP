var dataRepositoryViewContext1 = {
  id: 1,
  children: [],
  features: [],
  metadata: [],
  name: "mock name 1",
  parent: {
    subject: "mock subject",
    predicate: "mock predicate",
    object: "mock object"
  },
  properties: [],
  repositoryView: {
    id: 1,
    curators: [],
    name: "mock name 1",
    password: "mock password 1",
    rootUri: "http://localhost/mock1",
    schemas: [],
    username: "mock username 1"
  },
  transactionDetails: {
    token: "mock token 1",
    expiration: "mock zoned datetime"
  },
  triple: {
    subject: "mock subject 1",
    predicate: "mock predicate 1",
    object: "mock object 1"
  },
  version: "mock version 1",
  versions: []
};

var dataRepositoryViewContext2 = {
  id: 2,
  children: [],
  features: [],
  metadata: [],
  name: "mock name 2",
  parent: {
    subject: "mock subject",
    predicate: "mock predicate",
    object: "mock object"
  },
  properties: [],
  repositoryView: {},
  transactionDetails: {
    token: "mock token 2",
    expiration: "mock zoned datetime"
  },
  triple: {
    subject: "mock subject 2",
    predicate: "mock predicate 2",
    object: "mock object 2"
  },
  version: "mock version 2",
  versions: []
};

var dataRepositoryViewContext3 = {
  id: 3,
  children: [],
  features: [],
  metadata: [],
  name: "mock name 3",
  parent: {
    subject: "mock subject",
    predicate: "mock predicate",
    object: "mock object"
  },
  properties: [],
  repositoryView: {},
  transactionDetails: {
    token: "mock token 3",
    expiration: "mock zoned datetime"
  },
  triple: {
    subject: "mock subject 3",
    predicate: "mock predicate 3",
    object: "mock object 3"
  },
  version: "mock version 3",
  versions: []
};

var mockRepositoryViewContext = function($q) {
  var model = mockModel("RepositoryViewContext", $q, dataRepositoryViewContext1);

  model.advancedUpdate = function (query) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.createContainer = function (metadata) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.createMetadata = function (metadataTriples) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.createResource = function (file) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.createVersion = function (form) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.deleteVersion = function (versionContext) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.getCachedChildContext = function (contextUri) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.getChildContext = function (triple) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.getQueryHelp = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.refreshContext = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.reloadContext = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.removeContainers = function (containerTriples) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.removeMetadata = function (metadataTriples) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.removeResources = function (resourceTriples) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.revertVersion = function (versionContext) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.updateMetadatum = function (metadataTriples, value) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  return model;
};

angular.module("mock.repositoryViewContext", []).service("RepositoryViewContext", mockRepositoryViewContext);
