describe("directive: admintabs", function () {
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

      element = angular.element("<admintabs></admintabs>");
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
