describe("model: FixityReport", function () {
  var $rootScope, $scope, WsApi, model;

  var initializeVariables = function(settings) {
    inject(function (_$rootScope_, _WsApi_) {
      $rootScope = _$rootScope_;

      WsApi = _WsApi_;
    });
  };

  var initializeModel = function(settings) {
    inject(function (_FixityReport_) {
      $scope = $rootScope.$new();

      model = angular.extend(new _FixityReport_(), dataFixityReport1);
    });
  };

  beforeEach(function() {
    module("core");
    module("cap");
    module("mock.wsApi");

    initializeVariables();
    initializeModel();
  });

  describe("Is the model defined", function () {
    it("should be defined", function () {
      expect(model).toBeDefined();
    });
  });

  describe("Are the model methods defined", function () {
    it("run should be defined", function () {
      expect(model.run).toBeDefined();
      expect(typeof model.run).toEqual("function");
    });
  });

  describe("Are the model methods working as expected", function () {
    it("run should work", function () {
      // @todo
      //$scope.run();

      //$scope.$digest();
    });
  });
});
