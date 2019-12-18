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

  describe("Is the repo defined", function () {
    it("should be defined", function () {
      expect(repo).toBeDefined();
    });
  });

  describe("Are the repo methods defined", function () {
    it("findByName should be defined", function () {
      expect(repo.findByName).toBeDefined();
      expect(typeof repo.findByName).toEqual("function");
    });

    it("getTypes should be defined", function () {
      expect(repo.getTypes).toBeDefined();
      expect(typeof repo.getTypes).toEqual("function");
    });

    it("verifyAuth should be defined", function () {
      expect(repo.verifyAuth).toBeDefined();
      expect(typeof repo.verifyAuth).toEqual("function");
    });

    it("verifyContent should be defined", function () {
      expect(repo.verifyContent).toBeDefined();
      expect(typeof repo.verifyContent).toEqual("function");
    });

    it("verifyPing should be defined", function () {
      expect(repo.verifyPing).toBeDefined();
      expect(typeof repo.verifyPing).toEqual("function");
    });
  });

  describe("Are the repo methods working as expected", function () {
    it("findByName should work", function () {
      // @todo
      repo.findByName();
    });

    it("getTypes should work", function () {
      var types = [];

      // @todo
      repo.getTypes(types);

      $scope.$digest();
    });

    it("verifyAuth should work", function () {
      var repositoryView = new mockRepositoryView($q);

      // @todo
      repo.verifyAuth(repositoryView);
    });

    it("verifyContent should work", function () {
      var repositoryView = new mockRepositoryView($q);

      // @todo
      repo.verifyContent(repositoryView);
    });

    it("verifyPing should work", function () {
      var repositoryView = new mockRepositoryView($q);

      // @todo
      repo.verifyPing(repositoryView);
    });
  });

});
