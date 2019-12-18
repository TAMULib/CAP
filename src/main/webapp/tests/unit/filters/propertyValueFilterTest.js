describe("filter: propertyValue", function () {
  var $scope, filter;

  var initializeVariables = function () {
  };

  var initializeFilter = function (settings) {
    inject(function (_$filter_, _$rootScope_) {
      $scope = _$rootScope_.$new();

      filter = _$filter_("propertyValue");
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");

    installPromiseMatchers();
    initializeVariables();
    initializeFilter();
  });

  describe("Is the filter", function () {
    it("defined", function () {
      expect(filter).toBeDefined();
    });
  });

  describe("Does the filter", function () {
    it("return nothing on empty input", function () {
      var result;

      result = filter("");

      expect(result).toBe("");
    });
  });
});
