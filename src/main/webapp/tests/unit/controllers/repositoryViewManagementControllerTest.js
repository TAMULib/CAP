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
      "createRepositoryView",
      "confirmDeleteRepositoryView",
      "disableVerify",
      "editRepositoryView",
      "resetRepositoryViewForms",
      "onCancelCreateRepositoryView",
      "onCancelEditRepositoryView",
      "onCancelDeleteRepositoryView",
      "onCreateRepositoryView",
      "onDeleteRepositoryView",
      "onEditRepositoryView",
      "showSchemas"
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
    it("createRepositoryView work as expected", function () {
      $scope.schemas = undefined;
      spyOn($scope, "openModal");

      $scope.createRepositoryView();
      expect($scope.openModal).toHaveBeenCalled();
      expect($scope.schemas).toBeDefined();
    });

    it("confirmDeleteRepositoryView work as expected", function () {
      // @todo
      $scope.confirmDeleteRepositoryView();
    });

    it("disableVerify work as expected", function () {
      var repositoryView = new mockRepositoryView($q);
      // @todo
      $scope.disableVerify(repositoryView);
    });

    it("editRepositoryView work as expected", function () {
      // @todo
      $scope.editRepositoryView();
    });

    it("resetRepositoryViewForms work as expected", function () {
      // @todo
      $scope.resetRepositoryViewForms();
    });

    it("onCancelCreateRepositoryView work as expected", function () {
      // @todo
      $scope.onCancelCreateRepositoryView();
    });

    it("onCancelDeleteRepositoryView work as expected", function () {
      // @todo
      $scope.onCancelDeleteRepositoryView();
    });

    it("onCancelEditRepositoryView work as expected", function () {
      // @todo
      $scope.onCancelEditRepositoryView();
    });

    it("onCreateRepositoryView work as expected", function () {
      var repositoryView = new mockRepositoryView($q);
      $scope.repositoryView = repositoryView;

      // @todo
      $scope.onCreateRepositoryView();
      $scope.$digest();
    });

    it("onDeleteRepositoryView work as expected", function () {
      var repositoryView = new mockRepositoryView($q);
      $scope.repositoryView = repositoryView;

      // @todo
      $scope.onDeleteRepositoryView();
      $scope.$digest();
    });

    it("onEditRepositoryView work as expected", function () {
      var repositoryView = new mockRepositoryView($q);
      $scope.repositoryView = repositoryView;

      // @todo
      $scope.onEditRepositoryView();
      $scope.$digest();
    });

    it("showSchemas work as expected", function () {
      // @todo
      $scope.showSchemas();
    });
  });




});
