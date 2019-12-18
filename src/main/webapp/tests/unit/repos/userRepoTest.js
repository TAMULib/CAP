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

  describe("Is the repo", function () {
    it("defined", function () {
      expect(repo).toBeDefined();
    });
  });

  describe("Is the repo method", function () {
    var methods = [
      "getCurators"
    ];

    for (var i in methods) {
      it(methods[i] + " defined", function () {
        expect(repo[methods[i]]).toBeDefined();
        expect(typeof repo[methods[i]]).toEqual("function");
      });
    }
  });

  describe("Does the repo method", function () {
    it("getCurators work as expected", function () {
      // @todo
      repo.getCurators();

      $scope.$digest();
    });
  });

});
