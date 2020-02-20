var dataRepositoryView1 = {
  id: 1,
  curators: [],
  name: "mock name 1",
  password: "mock password 1",
  rootUri: "http://localhost/mock1",
  schemas: [],
  username: "mock username 1"
};

var dataRepositoryView2 = {
  id: 2,
  curators: [],
  name: "mock name 2",
  password: "mock password 2",
  rootUri: "http://localhost/mock2",
  schemas: [],
  username: "mock username 2"
};

var dataRepositoryView3 = {
  id: 3,
  curators: [],
  name: "mock name 3",
  password: "mock password 3",
  rootUri: "http://localhost/mock3",
  schemas: [],
  username: "mock username 3"
};

var mockRepositoryView = function ($q) {
  var model = mockModel("RepositoryView", $q, dataRepositoryView1);

  model.cacheContext = function (context) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.clearCache = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.commitTransaction = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.getCachedContext = function (contextUri) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.getContext = function (contextUri, reload) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.getTransaction = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.getTransactionSecondsRemaining = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.isTransactionAboutToExpire = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.inTransaction = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.loadContext = function (contextUri, reload) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.performRequest = function (endpoint, options) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.refreshTransaction = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.removeCachedContext = function (contextUri) {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.rollbackTransaction = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.startTransaction = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.stopTransactionTimer = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  model.transactionTimer = function () {
    var payload = {};
    // @todo
    return payloadPromise($q.defer(), payload);
  };

  return model;
};

angular.module("mock.repositoryView", []).service("RepositoryView", mockRepositoryView);
