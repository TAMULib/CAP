describe("controller: RepositoryViewContextController", function () {
  var $filter, $location, $q, $scope, $timeout, MockedFixityReport, MockedRepositoryView, MockedSchema, WsApi, controller, routeParams;

  var initializeVariables = function() {
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

  var initializeController = function(settings) {
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

  beforeEach(function() {
    module("core", function($provide) {
      routeParams = {};
      $provide.value("$routeParams", routeParams);
    });
    module("cap");
    module("mock.fixityReport", function($provide) {
      FixityReport = function() {
        return MockedFixityReport;
      };
      $provide.value("FixityReport", FixityReport);
    });
    module("mock.repositoryView", function($provide) {
      RepositoryView = function() {
        return MockedRepositoryView;
      };
      $provide.value("RepositoryView", RepositoryView);
    });
    module("mock.repositoryViewRepo");
    module("mock.schema", function($provide) {
      Schema = function() {
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

  describe("Is the controller defined", function () {
    it("should be defined for admin", function () {
      expect(controller).toBeDefined();
    });

    it("should be defined for manager", function () {
      initializeController({role: "ROLE_MANAGER"});
      expect(controller).toBeDefined();
    });

    it("should be defined for user", function () {
      initializeController({role: "ROLE_USER"});
      expect(controller).toBeDefined();
    });

    it("should be defined for anonymous", function () {
      initializeController({role: "ROLE_ANONYMOUS"});
      expect(controller).toBeDefined();
    });
  });

  describe("Are the scope methods defined", function () {
    it("addMetadata should be defined", function () {
      expect($scope.addMetadata).toBeDefined();
      expect(typeof $scope.addMetadata).toEqual("function");
    });

    it("advancedUpdate should be defined", function () {
      expect($scope.advancedUpdate).toBeDefined();
      expect(typeof $scope.advancedUpdate).toEqual("function");
    });

    it("cancelDeleteRepositoryViewContext should be defined", function () {
      expect($scope.cancelDeleteRepositoryViewContext).toBeDefined();
      expect(typeof $scope.cancelDeleteRepositoryViewContext).toEqual("function");
    });

    it("cancelDeleteRepositoryViewContext should be defined", function () {
      expect($scope.cancelDeleteRepositoryViewContext).toBeDefined();
      expect(typeof $scope.cancelDeleteRepositoryViewContext).toEqual("function");
    });

    it("cancelFixity should be defined", function () {
      expect($scope.cancelFixity).toBeDefined();
      expect(typeof $scope.cancelFixity).toEqual("function");
    });

    it("canPreview should be defined", function () {
      expect($scope.canPreview).toBeDefined();
      expect(typeof $scope.canPreview).toEqual("function");
    });

    it("createContainer should be defined", function () {
      expect($scope.createContainer).toBeDefined();
      expect(typeof $scope.createContainer).toEqual("function");
    });

    it("commitTransaction should be defined", function () {
      expect($scope.commitTransaction).toBeDefined();
      expect(typeof $scope.commitTransaction).toEqual("function");
    });

    it("copyToClipboard should be defined", function () {
      expect($scope.copyToClipboard).toBeDefined();
      expect(typeof $scope.copyToClipboard).toEqual("function");
    });

    it("deleteRepositoryViewContext should be defined", function () {
      expect($scope.deleteRepositoryViewContext).toBeDefined();
      expect(typeof $scope.deleteRepositoryViewContext).toEqual("function");
    });

    it("deleteVersion should be defined", function () {
      expect($scope.deleteVersion).toBeDefined();
      expect(typeof $scope.deleteVersion).toEqual("function");
    });

    it("getContentType should be defined", function () {
      expect($scope.getContentType).toBeDefined();
      expect(typeof $scope.getContentType).toEqual("function");
    });

    it("getIIIFUrl should be defined", function () {
      expect($scope.getIIIFUrl).toBeDefined();
      expect(typeof $scope.getIIIFUrl).toEqual("function");
    });

    it("lengthenContextUri should be defined", function () {
      expect($scope.lengthenContextUri).toBeDefined();
      expect(typeof $scope.lengthenContextUri).toEqual("function");
    });

    it("openFixity should be defined", function () {
      expect($scope.openFixity).toBeDefined();
      expect(typeof $scope.openFixity).toEqual("function");
    });

    it("refreshContext should be defined", function () {
      expect($scope.refreshContext).toBeDefined();
      expect(typeof $scope.refreshContext).toEqual("function");
    });

    it("resetAddMetadataModal should be defined", function () {
      expect($scope.resetAddMetadataModal).toBeDefined();
      expect(typeof $scope.resetAddMetadataModal).toEqual("function");
    });

    it("resetAdvancedUpdate should be defined", function () {
      expect($scope.resetAdvancedUpdate).toBeDefined();
      expect(typeof $scope.resetAdvancedUpdate).toEqual("function");
    });

    it("resetCreateContainer should be defined", function () {
      expect($scope.resetCreateContainer).toBeDefined();
      expect(typeof $scope.resetCreateContainer).toEqual("function");
    });

    it("resetUploadResource should be defined", function () {
      expect($scope.resetUploadResource).toBeDefined();
      expect(typeof $scope.resetUploadResource).toEqual("function");
    });

    it("revertVersion should be defined", function () {
      expect($scope.revertVersion).toBeDefined();
      expect(typeof $scope.revertVersion).toEqual("function");
    });

    it("rollbackTransaction should be defined", function () {
      expect($scope.rollbackTransaction).toBeDefined();
      expect(typeof $scope.rollbackTransaction).toEqual("function");
    });

    it("setOrToggleTheaterMode should be defined", function () {
      expect($scope.setOrToggleTheaterMode).toBeDefined();
      expect(typeof $scope.setOrToggleTheaterMode).toEqual("function");
    });

    it("srcFromFile should be defined", function () {
      expect($scope.srcFromFile).toBeDefined();
      expect(typeof $scope.srcFromFile).toEqual("function");
    });

    it("startTransaction should be defined", function () {
      expect($scope.startTransaction).toBeDefined();
      expect(typeof $scope.startTransaction).toEqual("function");
    });

    it("updateMetadatum should be defined", function () {
      expect($scope.updateMetadatum).toBeDefined();
      expect(typeof $scope.updateMetadatum).toEqual("function");
    });

    it("uploadResource should be defined", function () {
      expect($scope.uploadResource).toBeDefined();
      expect(typeof $scope.uploadResource).toEqual("function");
    });
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
