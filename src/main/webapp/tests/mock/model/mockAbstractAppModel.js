var dataAbstractAppModel1 = {
  id: 1
};

var dataAbstractAppModel2 = {
  id: 2
};

var dataAbstractAppModel3 = {
  id: 3
};

var mockAbstractAppModel = function ($q) {
  var model = mockModel("AbstractAppModel", $q, dataAbstractAppModel1);

  return model;
};

angular.module("mock.abstractAppModel", []).service("AbstractAppModel", mockAbstractAppModel);
