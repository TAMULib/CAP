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

  describe("Are the model methods defined", function () {
    it("cacheContext should be defined", function () {
      expect(model.cacheContext).toBeDefined();
      expect(typeof model.cacheContext).toEqual("function");
    });

    it("clearCache should be defined", function () {
      expect(model.clearCache).toBeDefined();
      expect(typeof model.clearCache).toEqual("function");
    });

    it("commitTransaction should be defined", function () {
      expect(model.commitTransaction).toBeDefined();
      expect(typeof model.commitTransaction).toEqual("function");
    });

    it("getCachedContext should be defined", function () {
      expect(model.getCachedContext).toBeDefined();
      expect(typeof model.getCachedContext).toEqual("function");
    });

    it("getContext should be defined", function () {
      expect(model.getContext).toBeDefined();
      expect(typeof model.getContext).toEqual("function");
    });

    it("getTransaction should be defined", function () {
      expect(model.getTransaction).toBeDefined();
      expect(typeof model.getTransaction).toEqual("function");
    });

    it("getTransactionSecondsRemaining should be defined", function () {
      expect(model.getTransactionSecondsRemaining).toBeDefined();
      expect(typeof model.getTransactionSecondsRemaining).toEqual("function");
    });

    it("inTransaction should be defined", function () {
      expect(model.inTransaction).toBeDefined();
      expect(typeof model.inTransaction).toEqual("function");
    });

    it("isTransactionAboutToExpire should be defined", function () {
      expect(model.isTransactionAboutToExpire).toBeDefined();
      expect(typeof model.isTransactionAboutToExpire).toEqual("function");
    });

    it("loadContext should be defined", function () {
      expect(model.loadContext).toBeDefined();
      expect(typeof model.loadContext).toEqual("function");
    });

    it("performRequest should be defined", function () {
      expect(model.performRequest).toBeDefined();
      expect(typeof model.performRequest).toEqual("function");
    });

    it("refreshTransaction should be defined", function () {
      expect(model.refreshTransaction).toBeDefined();
      expect(typeof model.refreshTransaction).toEqual("function");
    });

    it("removeCachedContext should be defined", function () {
      expect(model.removeCachedContext).toBeDefined();
      expect(typeof model.removeCachedContext).toEqual("function");
    });

    it("rollbackTransaction should be defined", function () {
      expect(model.rollbackTransaction).toBeDefined();
      expect(typeof model.rollbackTransaction).toEqual("function");
    });

    it("startTransaction should be defined", function () {
      expect(model.startTransaction).toBeDefined();
      expect(typeof model.startTransaction).toEqual("function");
    });

    it("startTransactionTimer should be defined", function () {
      expect(model.startTransactionTimer).toBeDefined();
      expect(typeof model.startTransactionTimer).toEqual("function");
    });

    it("stopTransactionTimer should be defined", function () {
      expect(model.stopTransactionTimer).toBeDefined();
      expect(typeof model.stopTransactionTimer).toEqual("function");
    });
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
