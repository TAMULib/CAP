describe("directive: contentviewer", function () {
  var $compile, $q, $scope, directive, element, contentType, resource;

  var initializeVariables = function () {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;

      contentType = "";
      resource = "";
    });
  };

  var initializeDirective = function (settings) {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      var attr = settings && settings.attr ? settings.attr : "content-type=\"contentType\" resource=\"resource\"";
      var body = settings && settings.body ? settings.body : "";

      element = angular.element("<contentviewer " + attr + ">" + body + "</contentviewer>");
      directive = $compile(element)($scope);

      $scope.contentType = contentType;
      $scope.resource = resource;

      $scope.$digest();
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("templates");
    module("mock.userService");

    installPromiseMatchers();
    initializeVariables();
  });

  describe("Is the directive", function () {
    it("defined", function () {
      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

  describe("Does the directive", function () {
    it("work with a default viewer", function () {
      contentType = "image/png";

      initializeDirective();
      expect(directive).toBeDefined();
    });

    it("work with a seadragon viewer", function () {
      contentType = "image/jp2";

      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

});
