describe("model: FixityReport", function () {
  var $rootScope, $scope, WsApi, model;

  var initializeVariables = function (settings) {
    inject(function (_$rootScope_, _WsApi_) {
      $rootScope = _$rootScope_;

      WsApi = _WsApi_;
    });
  };

  var initializeModel = function (settings) {
    inject(function (_FixityReport_) {
      $scope = $rootScope.$new();

      model = angular.extend(new _FixityReport_(), dataFixityReport1);
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("mock.wsApi");

    initializeVariables();
    initializeModel();
  });

  describe("Is the model", function () {
    it("defined", function () {
      expect(model).toBeDefined();
    });
  });

  describe("Is the model method", function () {
    var methods = [
      "run"
    ];

    var modelMethodExists = function (key) {
      return function() {
        expect(model[key]).toBeDefined();
        expect(typeof model[key]).toEqual("function");
      };
    };

    for (var i in methods) {
      it(methods[i] + " defined", modelMethodExists(methods[i]));
    }
  });

  describe("Does the model method", function () {
    it("run work as expected", function () {
      // @todo
      //$scope.run();

      //$scope.$digest();
    });
  });
});
