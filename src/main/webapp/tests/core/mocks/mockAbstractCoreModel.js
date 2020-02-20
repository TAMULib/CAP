var mockModel = function (modelName, $q, mockDataObj) {
  var model = {};
  var shadow = {};
  var combinationOperation = "";

  model.isDirty = false;
  model.isValid = false;

  model.mock = function (toMock) {
    if (typeof toMock === "object") {
      var keys = Object.keys(toMock);
      for (var i in keys) {
        model[keys[i]] = toMock[keys[i]];
      }
    } else if (toMock === undefined || toMock === null) {
      model = null;
    }
  };

  model.mockShadow = function (toMock) {
    shadow = toMock;
  };

  model.acceptPendingUpdate = function () {
  };

  model.acceptPendingDelete = function () {
  };

  model.before = function () {
  };

  model.clearListens = function () {
    var payload = {};
    return payloadPromise($q.defer(), payload);
  };

  model.clearValidationResults = function () {
  };

  model.delete = function () {
    return payloadPromise($q.defer(), true);
  };

  model.dirty = function (boolean) {
    if (boolean !== undefined) {
      model.isDirty = boolean;
    }

    return model.isDirty;
  };

  model.enableMergeCombinationOperation = function () {
    combinationOperation = "merge";
  };

  model.enableExtendCombinationOperation = function () {
    combinationOperation = "extend";
  };

  model.fetch = function () {
    return payloadPromise($q.defer(), mockDataObj);
  };

  model.getCombinationOperation = function () {
    return combinationOperation;
  };

  model.getEntityName = function () {
    return modelName;
  };

  model.getValidations = function () {
    return null;
  };

  model.init = function (data, apiMapping) {
  };

  model.listen = function () {
  };

  model.ready = function () {
    var defer = $q.defer();
    defer.resolve();
    return defer.promise;
  };

  model.refresh = function () {
    angular.extend(model, shadow);
  };

  model.reload = function () {
    var defer = $q.defer();
    defer.resolve(model);
    return defer.promise;
  };

  model.save = function () {
    return payloadPromise($q.defer(), true);
  };

  model._syncShadow = function () {
    model.isDirty = false;
    shadow = angular.copy(model);
  };

  model.mock(mockDataObj);
  model._syncShadow();

  return model;
};
