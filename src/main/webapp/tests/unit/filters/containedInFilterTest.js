describe("filter: containedIn", function () {
  var $scope, filter;

  var initializeVariables = function () {
  };

  var initializeFilter = function (settings) {
    inject(function (_$filter_, _$rootScope_) {
      $scope = _$rootScope_.$new();

      filter = _$filter_("containedIn");
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");

    installPromiseMatchers();
    initializeVariables();
    initializeFilter();
  });

  describe("Is the filter defined", function () {
    it("should be defined", function () {
      expect(filter).toBeDefined();
    });
  });

  describe("Does the filter work as expected", function () {
    it("should return nothing on empty input", function () {
      var result;

      result = filter("", "", "");

      // @todo review filter design and this test.
      //expect(result).toBe("");
    });
  });
});
