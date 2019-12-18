describe("controller: RepositoryViewManagementController", function () {
  var $q, $scope, MockedNgTableParams, MockedRepositoryView, MockedSchema, MockedUser, NgTableParams, WsApi, controller;

  var initializeVariables = function () {
    inject(function (_$q_, _WsApi_) {
      $q = _$q_;

      MockedNgTableParams = new mockNgTableParams($q);
      MockedRepositoryView = new mockRepositoryView($q);
      MockedSchema = new mockSchema($q);
      MockedUser = new mockUser($q);

      WsApi = _WsApi_;
    });
  };

  var initializeController = function (settings) {
    inject(function (_$controller_, _$rootScope_, _AssumedControl_, _AuthService_, _NgTableParams_, _RepositoryView_, _RepositoryViewRepo_, _SchemaRepo_, _StorageService_, _UserRepo_, _UserService_) {
      $scope = _$rootScope_.$new();

      sessionStorage.role = settings && settings.role ? settings.role : "ROLE_ADMIN";
      sessionStorage.token = settings && settings.token ? settings.token : "faketoken";

      controller = _$controller_("RepositoryViewManagementController", {
        $q: $q,
        $scope: $scope,
        AssumedControl: _AssumedControl_,
        AuthService: _AuthService_,
        NgTableParams: _NgTableParams_,
        RepositoryView: _RepositoryView_,
        RepositoryViewRepo: _RepositoryViewRepo_,
        SchemaRepo: _SchemaRepo_,
        StorageService: _StorageService_,
        UserRepo: _UserRepo_,
        UserService: _UserService_,
        WsApi: WsApi
      });

      // ensure that the isReady() is called.
      if (!$scope.$$phase) {
        $scope.$digest();
      }
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("mock.assumedControl");
    module("mock.authService");
    module("mock.ngTableParams", function ($provide) {
      var NgTableParams = function () {
        return MockedNgTableParams;
      };
      $provide.value("NgTableParams", NgTableParams);
    });
    module("mock.repositoryView", function ($provide) {
      var RepositoryView = function () {
        return MockedRepositoryView;
      };
      $provide.value("RepositoryView", RepositoryView);
    });
    module("mock.repositoryViewRepo");
    module("mock.schema", function ($provide) {
      var Schema = function () {
        return MockedSchema;
      };
      $provide.value("Schema", Schema);
    });
    module("mock.schemaRepo");
    module("mock.storageService");
    module("mock.user", function ($provide) {
      var User = function () {
        return MockedUser;
      };
      $provide.value("User", User);
    });
    module("mock.userRepo");
    module("mock.userService");
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
    it("createRepositoryView should be defined", function () {
      expect($scope.createRepositoryView).toBeDefined();
      expect(typeof $scope.createRepositoryView).toEqual("function");
    });

    it("confirmDeleteRepositoryView should be defined", function () {
      expect($scope.confirmDeleteRepositoryView).toBeDefined();
      expect(typeof $scope.confirmDeleteRepositoryView).toEqual("function");
    });

    it("disableVerify should be defined", function () {
      expect($scope.disableVerify).toBeDefined();
      expect(typeof $scope.disableVerify).toEqual("function");
    });

    it("editRepositoryView should be defined", function () {
      expect($scope.editRepositoryView).toBeDefined();
      expect(typeof $scope.editRepositoryView).toEqual("function");
    });

    it("resetRepositoryViewForms should be defined", function () {
      expect($scope.resetRepositoryViewForms).toBeDefined();
      expect(typeof $scope.resetRepositoryViewForms).toEqual("function");
    });

    it("onCancelCreateRepositoryView should be defined", function () {
      expect($scope.onCancelCreateRepositoryView).toBeDefined();
      expect(typeof $scope.onCancelCreateRepositoryView).toEqual("function");
    });

    it("onCancelEditRepositoryView should be defined", function () {
      expect($scope.onCancelEditRepositoryView).toBeDefined();
      expect(typeof $scope.onCancelEditRepositoryView).toEqual("function");
    });

    it("onCancelDeleteRepositoryView should be defined", function () {
      expect($scope.onCancelDeleteRepositoryView).toBeDefined();
      expect(typeof $scope.onCancelDeleteRepositoryView).toEqual("function");
    });

    it("onCreateRepositoryView should be defined", function () {
      expect($scope.onCreateRepositoryView).toBeDefined();
      expect(typeof $scope.onCreateRepositoryView).toEqual("function");
    });

    it("onDeleteRepositoryView should be defined", function () {
      expect($scope.onDeleteRepositoryView).toBeDefined();
      expect(typeof $scope.onDeleteRepositoryView).toEqual("function");
    });

    it("onEditRepositoryView should be defined", function () {
      expect($scope.onEditRepositoryView).toBeDefined();
      expect(typeof $scope.onEditRepositoryView).toEqual("function");
    });

    it("showSchemas should be defined", function () {
      expect($scope.showSchemas).toBeDefined();
      expect(typeof $scope.showSchemas).toEqual("function");
    });
  });

  describe("Do the $scope methods work as expected", function () {
    it("createRepositoryView should work", function () {
      $scope.schemas = undefined;
      spyOn($scope, "openModal");

      $scope.createRepositoryView();
      expect($scope.openModal).toHaveBeenCalled();
      expect($scope.schemas).toBeDefined();
    });

    it("confirmDeleteRepositoryView should work", function () {
      // @todo
      $scope.confirmDeleteRepositoryView();
    });

    it("disableVerify should work", function () {
      var repositoryView = new mockRepositoryView($q);
      // @todo
      $scope.disableVerify(repositoryView);
    });

    it("editRepositoryView should work", function () {
      // @todo
      $scope.editRepositoryView();
    });

    it("resetRepositoryViewForms should work", function () {
      // @todo
      $scope.resetRepositoryViewForms();
    });

    it("onCancelCreateRepositoryView should work", function () {
      // @todo
      $scope.onCancelCreateRepositoryView();
    });

    it("onCancelDeleteRepositoryView should work", function () {
      // @todo
      $scope.onCancelDeleteRepositoryView();
    });

    it("onCancelEditRepositoryView should work", function () {
      // @todo
      $scope.onCancelEditRepositoryView();
    });

    it("onCreateRepositoryView should work", function () {
      var repositoryView = new mockRepositoryView($q);
      $scope.repositoryView = repositoryView;

      // @todo
      $scope.onCreateRepositoryView();
      $scope.$digest();
    });

    it("onDeleteRepositoryView should work", function () {
      var repositoryView = new mockRepositoryView($q);
      $scope.repositoryView = repositoryView;

      // @todo
      $scope.onDeleteRepositoryView();
      $scope.$digest();
    });

    it("onEditRepositoryView should work", function () {
      var repositoryView = new mockRepositoryView($q);
      $scope.repositoryView = repositoryView;

      // @todo
      $scope.onEditRepositoryView();
      $scope.$digest();
    });

    it("showSchemas should work", function () {
      // @todo
      $scope.showSchemas();
    });
  });




});
