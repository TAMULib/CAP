describe("controller: RepositoryViewContextController", function () {
  var $filter, $location, $q, $scope, $timeout, MockedFixityReport, MockedRepositoryView, MockedSchema, WsApi, controller, routeParams;

  var initializeVariables = function () {
    inject(function (_$filter_, _$location_, _$q_, _$timeout_, _WsApi_) {
      $filter = _$filter_;
      $location = _$location_;
      $q = _$q_;
      $timeout = _$timeout_;

      MockedRepositoryView = new mockRepositoryView($q);
      MockedSchema = new mockSchema($q);
      MockedFixityReport = new mockFixityReport($q);

      WsApi = _WsApi_;
    });
  };

  var initializeController = function (settings) {
    inject(function (_$controller_, _$rootScope_, _FixityReport_, _RepositoryViewRepo_, _SchemaRepo_) {
      $scope = _$rootScope_.$new();

      sessionStorage.role = settings && settings.role ? settings.role : "ROLE_ADMIN";
      sessionStorage.token = settings && settings.token ? settings.token : "faketoken";

      if (settings) {
        if (settings.routeParams) {
          angular.extend(routeParams, settings.routeParams);
        }
      }

      controller = _$controller_("RepositoryViewContextController", {
        $filter: $filter,
        $location: $location,
        $q: $q,
        $scope: $scope,
        $timeout: $timeout,
        RepositoryViewRepo: _RepositoryViewRepo_,
        $routeParams: routeParams,
        SchemaRepo: _SchemaRepo_,
        FixityReport: _FixityReport_,
        WsApi: WsApi
      });

      // ensure that the isReady() is called.
      if (!$scope.$$phase) {
        $scope.$digest();
      }
    });
  };

  beforeEach(function () {
    module("core", function ($provide) {
      routeParams = {};
      $provide.value("$routeParams", routeParams);
    });
    module("cap");
    module("mock.fixityReport", function ($provide) {
      FixityReport = function () {
        return MockedFixityReport;
      };
      $provide.value("FixityReport", FixityReport);
    });
    module("mock.repositoryView", function ($provide) {
      RepositoryView = function () {
        return MockedRepositoryView;
      };
      $provide.value("RepositoryView", RepositoryView);
    });
    module("mock.repositoryViewRepo");
    module("mock.schema", function ($provide) {
      Schema = function () {
        return MockedSchema;
      };
      $provide.value("Schema", Schema);
    });
    module("mock.schemaRepo");
    module("mock.wsApi");

    installPromiseMatchers();
    initializeVariables();
    initializeController();
  });

  describe("Is the controller", function () {
    var roles = [ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER", "ROLE_ANONYMOUS" ];

    var controllerExists = function (setting) {
      return function() {
        initializeController(setting);
        expect(controller).toBeDefined();
      };
    };

    for (var i in roles) {
      it("defined for " + roles[i], controllerExists({ role: roles[i] }));
    }
  });

  describe("Is the scope method", function () {
    var methods = [
      "addMetadata",
      "advancedUpdate",
      "cancelDeleteRepositoryViewContext",
      "cancelDeleteRepositoryViewContext",
      "cancelFixity",
      "canPreview",
      "createContainer",
      "commitTransaction",
      "copyToClipboard",
      "deleteRepositoryViewContext",
      "deleteVersion",
      "getContentType",
      "getIIIFUrl",
      "lengthenContextUri",
      "openFixity",
      "refreshContext",
      "resetAddMetadataModal",
      "resetAdvancedUpdate",
      "resetCreateContainer",
      "resetUploadResource",
      "revertVersion",
      "rollbackTransaction",
      "setOrToggleTheaterMode",
      "srcFromFile",
      "startTransaction",
      "updateMetadatum",
      "uploadResource"
    ];

    var scopeMethodExists = function (method) {
      return function() {
        expect($scope[method]).toBeDefined();
        expect(typeof $scope[method]).toEqual("function");
      };
    };

    for (var i in methods) {
      it(methods[i] + " defined", scopeMethodExists(methods[i]));
    }
  });

  describe("Does the $scope method", function () {
    it("addMetadata work as expected", function () {
      // @todo
      //$scope.addMetadata();
    });

    it("advancedUpdate work as expected", function () {
      // @todo
      //$scope.advancedUpdate();

      //$scope.$digest();
    });

    it("cancelDeleteRepositoryViewContext work as expected", function () {
      // @todo
      $scope.cancelDeleteRepositoryViewContext();
    });

    it("cancelFixity work as expected", function () {
      // @todo
      $scope.cancelFixity();
    });

    it("canPreview work as expected", function () {
      // @todo
      $scope.canPreview();
    });

    it("createContainer work as expected", function () {
      // @todo
      //$scope.createContainer("http://localhost/");
    });

    it("commitTransaction work as expected", function () {
      // @todo
      //$scope.commitTransaction();

      //$scope.$digest();
    });

    it("copyToClipboard work as expected", function () {
      // @todo
      //$scope.copyToClipboard();

      //$timeout.flush();
    });

    it("deleteRepositoryViewContext work as expected", function () {
      // @todo
      $scope.deleteRepositoryViewContext();

      $scope.$digest();
    });

    it("deleteVersion work as expected", function () {
      // @todo
      //$scope.deleteVersion();

      //$scope.$digest();
    });

    it("getContentType work as expected", function () {
      // @todo
      //$scope.getContentType();
    });

    it("getIIIFUrl work as expected", function () {
      // @todo
      $scope.getIIIFUrl();
    });

    it("lengthenContextUri work as expected", function () {
      // @todo
      //$scope.lengthenContextUri("http://localhost/");
    });

    it("openFixity work as expected", function () {
      // @todo
      $scope.openFixity("http://localhost/");
    });

    it("refreshContext work as expected", function () {
      // @todo
      //$scope.refreshContext();
    });

    it("resetAddMetadataModal work as expected", function () {
      // @todo
      //$scope.resetAddMetadataModal();
    });

    it("resetAdvancedUpdate work as expected", function () {
      // @todo
      //$scope.resetAdvancedUpdate();
    });

    it("resetCreateContainer work as expected", function () {
      // @todo
      $scope.resetCreateContainer();
    });

    it("resetUploadResource work as expected", function () {
      // @todo
      $scope.resetUploadResource();
    });

    it("rollbackTransaction work as expected", function () {
      // @todo
      //$scope.rollbackTransaction();
    });

    it("revertVersion work as expected", function () {
      // @todo
      $scope.revertVersion();

      $scope.$digest();
    });

    it("setOrToggleTheaterMode work as expected", function () {
      // @todo
      $scope.setOrToggleTheaterMode(true);
    });

    it("srcFromFile work as expected", function () {
      // @todo
      //$scope.srcFromFile();
    });

    it("startTransaction work as expected", function () {
      // @todo
      //$scope.startTransaction();
    });

    it("updateMetadatum work as expected", function () {
      // @todo
      $scope.updateMetadatum();
    });

    it("uploadResource work as expected", function () {
      // @todo
      //$scope.uploadResource();

      //$scope.$digest();
    });
  });

});
