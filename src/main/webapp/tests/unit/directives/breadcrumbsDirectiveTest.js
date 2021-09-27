describe("directive: breadcrumbs", function () {
  var $compile, $q, $scope, MockedRepositoryView, RepositoryViewContext, directive, element, context;

  var initializeVariables = function () {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;

      MockedRepositoryView = new mockRepositoryView($q);
      MockedRepositoryViewContext = new mockRepositoryViewContext($q);

      context = new mockRepositoryViewContext($q);
      context.repositoryView = new mockRepositoryView($q);
    });
  };

  var initializeDirective = function (settings) {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      var attr = settings && settings.attr ? settings.attr : "context=\"context\"";
      var body = settings && settings.body ? settings.body : "";

      element = angular.element("<breadcrumbs " + attr + ">" + body + "</breadcrumbs>");
      directive = $compile(element)($scope);

      $scope.context = context;

      $scope.$digest();
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("templates");
    module("mock.userService");
    module("mock.repositoryView", function ($provide) {
      var RepositoryView = function () {
        return MockedRepositoryView;
      };
      $provide.value("RepositoryView", RepositoryView);
    });
    module("mock.repositoryViewContext", function ($provide) {
      var RepositoryViewContext = function () {
        return MockedRepositoryViewContext;
      };
      $provide.value("RepositoryViewContext", RepositoryViewContext);
    });

    installPromiseMatchers();
    initializeVariables();
  });

  describe("Is the directive", function () {
    it("defined", function () {
      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

});
