describe("directive: repositoryViewVerification", function () {
  var $compile, $q, $scope, directive, element, repositoryView, results;

  var initializeVariables = function() {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;

      repositoryView = "";
      results = "";
    });
  };

  var initializeDirective = function() {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      element = angular.element("<repository-view-verification repository-view=\"repositoryView\" results=\"results\"></repository-view-verification>");
      directive = $compile(element)($scope);

      $scope.repositoryView = repositoryView;
      $scope.results = results;

      $scope.$digest();
    });
  };

  beforeEach(function() {
    module("core");
    module("cap");
    module("templates");
    module("mock.repositoryViewRepo");

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
    it("should be defined, with repositoryView property", function () {
      repositoryView = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("should be defined, with results property", function () {
      results = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });
  });

});
