describe("service: repositoryViewRepo", function () {
  var $q, $rootScope, $scope, MockedRepositoryView, WsApi, repo;

  var initializeVariables = function (settings) {
    inject(function (_$q_, _$rootScope_, _WsApi_) {
      $q = _$q_;
      $rootScope = _$rootScope_;

      MockedRepositoryView = new mockRepositoryView($q);
      WsApi = _WsApi_;
    });
  };

  var initializeRepo = function (settings) {
    inject(function ($injector, RepositoryViewRepo) {
      $scope = $rootScope.$new();

      repo = RepositoryViewRepo;
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("mock.userService");
    module("mock.repositoryView", function ($provide) {
      var RepositoryView = function () {
        return MockedRepositoryView;
      };
      $provide.value("RepositoryView", RepositoryView);
    });
    module("mock.wsApi");

    initializeVariables();
    initializeRepo();
  });

  describe("Is the repo", function () {
    it("defined", function () {
      expect(repo).toBeDefined();
    });
  });

  describe("Is the repo method", function () {
    var methods = [
      "findByName",
      "getTypes",
      "verifyAuth",
      "verifyContent",
      "verifyPing"
    ];

    var repoMethodExists = function (key) {
      return function() {
        expect(repo[key]).toBeDefined();
        expect(typeof repo[key]).toEqual("function");
      };
    };

    for (var i in methods) {
      it(methods[i] + " defined", repoMethodExists(methods[i]));
    }
  });

  describe("Does the repo method", function () {
    it("findByName work as expected", function () {
      // @todo
      repo.findByName();
    });

    it("getTypes work as expected", function () {
      var types = [];

      // @todo
      repo.getTypes(types);

      $scope.$digest();
    });

    it("verifyAuth work as expected", function () {
      var repositoryView = new mockRepositoryView($q);

      // @todo
      repo.verifyAuth(repositoryView);
    });

    it("verifyContent work as expected", function () {
      var repositoryView = new mockRepositoryView($q);

      // @todo
      repo.verifyContent(repositoryView);
    });

    it("verifyPing work as expected", function () {
      var repositoryView = new mockRepositoryView($q);

      // @todo
      repo.verifyPing(repositoryView);
    });
  });

});
