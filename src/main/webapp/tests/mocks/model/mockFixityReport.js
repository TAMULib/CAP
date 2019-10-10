var dataFixityReport1 = {
  id: 1,
  messageDigest: "mock messageDigest 1",
  size: "mock size 1",
  status: "mock status 1"
};

var dataFixityReport2 = {
  id: 2,
  messageDigest: "mock messageDigest 2",
  size: "mock size 2",
  status: "mock status 2"
};

var dataFixityReport3 = {
  id: 3,
  messageDigest: "mock messageDigest 3",
  size: "mock size 3",
  status: "mock status 3"
};

var mockFixityReport = function($q) {
  var model = mockModel("FixityReport", $q, dataFixityReport1);

  model.run = function() {
    // @todo
  };

  return model;
};

angular.module("mock.fixityReport", []).service("FixityReport", mockFixityReport);
