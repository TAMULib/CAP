describe("filter: unescapeLiteral", function () {
  var $scope, filter;

  var initializeVariables = function() {
  };

  var initializeFilter = function(settings) {
    inject(function (_$filter_, _$rootScope_) {
      $scope = _$rootScope_.$new();

      filter = _$filter_('unescapeLiteral');
    });
  };

  beforeEach(function() {
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

      result = filter("");

      expect(result).toBe("");
    });
  });
});
