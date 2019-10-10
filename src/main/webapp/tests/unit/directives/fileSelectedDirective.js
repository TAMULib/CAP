describe("directive: fileSelected", function () {
  var $compile, $q, $scope, directive, element;

  var initializeVariables = function() {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;
    });
  };

  var initializeDirective = function() {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      element = angular.element("<file-selected></file-selected>");
      directive = $compile(element)($scope);

      $scope.$digest();
    });
  };

  beforeEach(function() {
    module("core");
    module("cap");
    module('templates');

    installPromiseMatchers();
    initializeVariables();
  });

  describe("Is the directive defined", function () {
    it("should be defined", function () {
      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

});
