describe("model: RepositoryView", function () {
  var $q, $rootScope, $scope, $timeout, MockedRepositoryViewContext, WsApi, model;

  var initializeVariables = function (settings) {
    inject(function (_$q_, _$rootScope_, _$timeout_, _WsApi_) {
      $q = _$q_;
      $rootScope = _$rootScope_;
      $timeout = _$timeout_;

      MockedRepositoryViewContext = new mockRepositoryViewContext($q);

      WsApi = _WsApi_;
    });
  };

  var initializeModel = function (settings) {
    inject(function (_RepositoryView_) {
      $scope = $rootScope.$new();

      model = angular.extend(new _RepositoryView_(), dataRepositoryView1);
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("mock.repositoryViewContext", function ($provide) {
      var RepositoryViewContext = function () {
        return MockedRepositoryViewContext;
      };
      $provide.value("RepositoryViewContext", RepositoryViewContext);
    });
    module("mock.userService");
    module("mock.wsApi");

    initializeVariables();
    initializeModel();
  });

  describe("Is the model defined", function () {
    it("should be defined", function () {
      expect(model).toBeDefined();
    });
  });

  describe("Is the model method", function () {
    var methods = [
      "cacheContext",
      "clearCache",
      "commitTransaction",
      "getCachedContext",
      "getContext",
      "getTransaction",
      "getTransactionSecondsRemaining",
      "inTransaction",
      "isTransactionAboutToExpire",
      "loadContext",
      "performRequest",
      "refreshTransaction",
      "removeCachedContext",
      "rollbackTransaction",
      "startTransaction",
      "startTransactionTimer",
      "stopTransactionTimer"
    ];

    for (var i in methods) {
      it(methods[i] + " defined", function () {
        expect(model[methods[i]]).toBeDefined();
        expect(typeof model[methods[i]]).toEqual("function");
      });
    }
  });

  describe("Are the model methods working as expected", function () {
    it("cacheContext should work", function () {
      var context = new mockRepositoryViewContext($q);

      // @todo
      model.cacheContext(context);
    });

    it("clearCache should work", function () {
      // @todo
      model.clearCache();
    });

    it("commitTransaction should work", function () {
      var context = new mockRepositoryViewContext($q);
      model.currentContext = context;

      // @todo
      model.commitTransaction();
    });

    it("getCachedContext should work", function () {
      var contextUri = "";
      // @todo
      model.getCachedContext(contextUri);
    });

    it("getContext should work", function () {
      var contextUri = "";
      var reload = false;

      // @todo
      model.getCachedContext(contextUri, reload);
    });

    it("getTransaction should work", function () {
      var context = new mockRepositoryViewContext($q);
      model.currentContext = context;

      // @todo
      model.getTransaction();
    });

    it("getTransactionSecondsRemaining should work", function () {
      var context = new mockRepositoryViewContext($q);
      model.currentContext = context;

      // @todo
      model.getTransactionSecondsRemaining();
    });

    it("inTransaction should work", function () {
      var context = new mockRepositoryViewContext($q);
      model.currentContext = context;

      // @todo
      model.inTransaction();
    });

    it("isTransactionAboutToExpire should work", function () {
      var context = new mockRepositoryViewContext($q);
      model.currentContext = context;

      // @todo
      model.isTransactionAboutToExpire();
    });

    it("loadContext should work", function () {
      // @todo
      model.loadContext();
    });

    it("performRequest should work", function () {
      var endpoint = "";
      var options = {
        pathValues: ""
      };

      // @todo
      model.performRequest(endpoint, options);
    });

    it("refreshTransaction should work", function () {
      var context = new mockRepositoryViewContext($q);
      model.currentContext = context;

      // @todo
      model.refreshTransaction();
    });

    it("removeCachedContext should work", function () {
      var contextUri = "";

      // @todo
      model.removeCachedContext(contextUri);
    });

    it("rollbackTransaction should work", function () {
      var context = new mockRepositoryViewContext($q);
      model.currentContext = context;

      // @todo
      model.rollbackTransaction();

      $scope.$digest();
    });

    it("startTransaction should work", function () {
      var context = new mockRepositoryViewContext($q);
      model.currentContext = context;

      // @todo
      //model.startTransaction();

      //$scope.$digest();
    });

    it("startTransactionTimer should work", function () {
      // @todo
      model.startTransactionTimer();

      $scope.$digest();
    });

    it("stopTransactionTimer should work", function () {
      // @todo

      model.stopTransactionTimer();

      $timeout.flush();
    });
  });
});
