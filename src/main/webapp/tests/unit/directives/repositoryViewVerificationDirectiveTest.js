describe("directive: repositoryViewVerification", function () {
  var $compile, $q, $scope, directive, element, repositoryView, results;

  var initializeVariables = function () {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;

      repositoryView = "";
      results = "";
    });
  };

  var initializeDirective = function (settings) {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      var attr = settings && settings.attr ? settings.attr : "repository-view=\"repositoryView\" results=\"results\"";
      var body = settings && settings.body ? settings.body : "";

      element = angular.element("<repository-view-verification " + attr + ">" + body + "</repository-view-verification>");
      directive = $compile(element)($scope);

      $scope.repositoryView = repositoryView;
      $scope.results = results;

      $scope.$digest();
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("templates");
    module("mock.repositoryViewRepo");
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
    it("work with repositoryView property", function () {
      repositoryView = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with results property", function () {
      results = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });
  });

});
