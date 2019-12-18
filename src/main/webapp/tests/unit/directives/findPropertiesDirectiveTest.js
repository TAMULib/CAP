describe("directive: findproperties", function () {
  var $compile, $q, $scope, directive, element, schema, status, mode;

  var initializeVariables = function () {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;

      schema = "";
      status = "";
      mode = "";
    });
  };

  var initializeDirective = function (settings) {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      var attr = settings && settings.attr ? settings.attr : "schema=\"schema\" status=\"status\" mode=\"mode\"";
      var body = settings && settings.body ? settings.body : "";

      element = angular.element("<findproperties " + attr + ">" + body + "</findproperties>");
      directive = $compile(element)($scope);

      $scope.schema = schema;
      $scope.status = status;
      $scope.mode = mode;

      $scope.$digest();
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("templates");

    installPromiseMatchers();
    initializeVariables();
  });

// @todo window.stompClient needs to be provided and possibly mocked.
/*
  describe("Is the directive", function () {
    it("defined", function () {
      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

  describe("Does the directive", function () {
    it("work with schema property", function () {
      schema = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with status property", function () {
      status = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with mode property", function () {
      mode = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });
  });
*/

});
