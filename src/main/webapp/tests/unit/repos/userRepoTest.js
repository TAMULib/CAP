describe("service: userRepo", function () {
  var $q, $rootScope, $scope, MockedUser, WsApi, repo;

  var initializeVariables = function (settings) {
    inject(function (_$q_, _$rootScope_, _WsApi_) {
      $q = _$q_;
      $rootScope = _$rootScope_;

      MockedUser = new mockUser($q);
      WsApi = _WsApi_;
    });
  };

  var initializeRepo = function (settings) {
    inject(function ($injector, UserRepo) {
      $scope = $rootScope.$new();

      repo = UserRepo;
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("mock.user", function ($provide) {
      var User = function () {
        return MockedUser;
      };
      $provide.value("User", User);
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
    it("getCurators should be defined", function () {
      expect(repo.getCurators).toBeDefined();
      expect(typeof repo.getCurators).toEqual("function");
    });
  });

  describe("Are the repo methods working as expected", function () {
    it("getCurators should work", function () {
      // @todo
      repo.getCurators();

      $scope.$digest();
    });
  });

});
