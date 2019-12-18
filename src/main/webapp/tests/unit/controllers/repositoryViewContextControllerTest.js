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
    for (var i in roles) {
      it("defined for " + roles[i], function () {
        initializeController({ role: roles[i] });
        expect(controller).toBeDefined();
      });
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

    for (var i in methods) {
      it(methods[i] + " defined", function () {
        expect($scope[methods[i]]).toBeDefined();
        expect(typeof $scope[methods[i]]).toEqual("function");
      });
    }
  });

  describe("Do the $scope methods work as expected", function () {
    it("addMetadata should work", function () {
      // @todo
      //$scope.addMetadata();
    });

    it("advancedUpdate should work", function () {
      // @todo
      //$scope.advancedUpdate();

      //$scope.$digest();
    });

    it("cancelDeleteRepositoryViewContext should work", function () {
      // @todo
      $scope.cancelDeleteRepositoryViewContext();
    });

    it("cancelFixity should work", function () {
      // @todo
      $scope.cancelFixity();
    });

    it("canPreview should work", function () {
      // @todo
      $scope.canPreview();
    });

    it("createContainer should work", function () {
      // @todo
      //$scope.createContainer("http://localhost/");
    });

    it("commitTransaction should work", function () {
      // @todo
      //$scope.commitTransaction();

      //$scope.$digest();
    });

    it("copyToClipboard should work", function () {
      // @todo
      //$scope.copyToClipboard();

      //$timeout.flush();
    });

    it("deleteRepositoryViewContext should work", function () {
      // @todo
      $scope.deleteRepositoryViewContext();

      $scope.$digest();
    });

    it("deleteVersion should work", function () {
      // @todo
      //$scope.deleteVersion();

      //$scope.$digest();
    });

    it("getContentType should work", function () {
      // @todo
      //$scope.getContentType();
    });

    it("getIIIFUrl should work", function () {
      // @todo
      $scope.getIIIFUrl();
    });

    it("lengthenContextUri should work", function () {
      // @todo
      //$scope.lengthenContextUri("http://localhost/");
    });

    it("openFixity should work", function () {
      // @todo
      $scope.openFixity("http://localhost/");
    });

    it("refreshContext should work", function () {
      // @todo
      //$scope.refreshContext();
    });

    it("resetAddMetadataModal should work", function () {
      // @todo
      //$scope.resetAddMetadataModal();
    });

    it("resetAdvancedUpdate should work", function () {
      // @todo
      //$scope.resetAdvancedUpdate();
    });

    it("resetCreateContainer should work", function () {
      // @todo
      $scope.resetCreateContainer();
    });

    it("resetUploadResource should work", function () {
      // @todo
      $scope.resetUploadResource();
    });

    it("rollbackTransaction should work", function () {
      // @todo
      //$scope.rollbackTransaction();
    });

    it("revertVersion should work", function () {
      // @todo
      $scope.revertVersion();

      $scope.$digest();
    });

    it("setOrToggleTheaterMode should work", function () {
      // @todo
      $scope.setOrToggleTheaterMode(true);
    });

    it("srcFromFile should work", function () {
      // @todo
      //$scope.srcFromFile();
    });

    it("startTransaction should work", function () {
      // @todo
      //$scope.startTransaction();
    });

    it("updateMetadatum should work", function () {
      // @todo
      $scope.updateMetadatum();
    });

    it("uploadResource should work", function () {
      // @todo
      //$scope.uploadResource();

      //$scope.$digest();
    });
  });

});
