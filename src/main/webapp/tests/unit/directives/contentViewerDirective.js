describe("directive: contentviewer", function () {
  var $compile, $q, $scope, directive, element, contentType, resource;

  var initializeVariables = function() {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;

      contentType = "";
      resource = "";
    });
  };

  var initializeDirective = function() {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      element = angular.element("<contentviewer content-type=\"contentType\" resource=\"resource\"></contentviewer>");
      directive = $compile(element)($scope);

      $scope.contentType = contentType;
      $scope.resource = resource;

      $scope.$digest();
    });
  };

  beforeEach(function() {
    module("core");
    module("cap");
    module("templates");

    installPromiseMatchers();
    initializeVariables();
  });

  describe("Is the directive defined", function () {
    it("should be defined", function () {
      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

  describe("Does the directive initialize properly", function () {
    it("should work with a default viewer", function () {
      contentType = "image/png";

      initializeDirective();
      expect(directive).toBeDefined();
    });

    it("should work with a seadragon viewer", function () {
      contentType = "image/jp2";

      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

});
