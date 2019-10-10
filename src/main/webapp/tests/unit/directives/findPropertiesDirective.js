describe("directive: findproperties", function () {
  var $compile, $q, $scope, directive, element, schema, status, mode;

  var initializeVariables = function() {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;

      schema = "";
      status = "";
      mode = "";
    });
  };

  var initializeDirective = function() {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      element = angular.element("<findproperties schema=\"schema\" status=\"status\" mode=\"mode\"></findproperties>");
      directive = $compile(element)($scope);

      $scope.schema = schema;
      $scope.status = status;
      $scope.mode = mode;

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

// @todo window.stompClient needs to be provided and possibly mocked.
/*
  describe("Is the directive defined", function () {
    it("should be defined", function () {
      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

  describe("Does the directive initialize properly", function () {
    it("should be defined, with schema property", function () {
      schema = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("should be defined, with status property", function () {
      status = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("should be defined, with mode property", function () {
      mode = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });
  });
*/

});
