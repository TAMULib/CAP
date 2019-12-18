describe("controller: SchemaManagementController", function () {
  var $q, $scope, $timeout, MockedNgTableParams, MockedSchema, MockedUser, WsApi, controller;

  var initializeVariables = function () {
    inject(function (_$q_, _$timeout_, _WsApi_) {
      $q = _$q_;
      $timeout = _$timeout_;

      MockedNgTableParams = new mockNgTableParams($q);
      MockedSchema = new mockSchema($q);
      MockedUser = new mockUser($q);

      WsApi = _WsApi_;
    });
  };

  var initializeController = function (settings) {
    inject(function (_$controller_, _$rootScope_, _AssumedControl_, _AuthService_, _NgTableParams_, _SchemaRepo_, _StorageService_, _UserRepo_, _UserService_) {
      $scope = _$rootScope_.$new();

      sessionStorage.role = settings && settings.role ? settings.role : "ROLE_ADMIN";
      sessionStorage.token = settings && settings.token ? settings.token : "faketoken";

      controller = _$controller_("SchemaManagementController", {
        $q: $q,
        $scope: $scope,
        AssumedControl: _AssumedControl_,
        AuthService: _AuthService_,
        NgTableParams: _NgTableParams_,
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
    for (var i in roles) {
      it("defined for " + roles[i], function () {
        initializeController({ role: roles[i] });
        expect(controller).toBeDefined();
      });
    }
  });

  describe("Is the scope method", function () {
    var methods = [
      "createSchema",
      "deleteSchema",
      "editSchema",
      "onCancelCreateSchema",
      "onCancelDeleteSchema",
      "onCancelEditSchema",
      "onCreateSchema",
      "onDeleteSchema",
      "onEditSchema",
      "resetSchemaForms",
      "setTable",
      "showProperties"
    ];

    for (var i in methods) {
      it(methods[i] + " defined", function () {
        expect($scope[methods[i]]).toBeDefined();
        expect(typeof $scope[methods[i]]).toEqual("function");
      });
    }
  });

  describe("Does the $scope method", function () {
    it("createSchema work as expected", function () {
      $scope.schema = undefined;
      spyOn($scope, "openModal");

      $scope.createSchema();
      expect($scope.openModal).toHaveBeenCalled();
      expect($scope.schema).toBeDefined();
    });

    it("deleteSchema work as expected", function () {
      // @todo
      $scope.deleteSchema();
    });

    it("editSchema work as expected", function () {
      // @todo
      $scope.editSchema();
    });

    it("onCancelCreateSchema work as expected", function () {
      // @todo
      $scope.onCancelCreateSchema();

      $timeout.flush();
    });

    it("onCancelDeleteSchema work as expected", function () {
      // @todo
      $scope.onCancelDeleteSchema();
    });

    it("onCancelEditSchema work as expected", function () {
      // @todo
      $scope.onCancelEditSchema();

      $timeout.flush();
    });

    it("onCreateSchema work as expected", function () {
      var schema = new mockSchema($q);
      $scope.schema = schema;

      // @todo
      $scope.onCreateSchema();

      $scope.$digest();
    });

    it("onDeleteSchema work as expected", function () {
      var schema = new mockSchema($q);
      $scope.schema = schema;

      // @todo
      $scope.onDeleteSchema();

      $scope.$digest();
    });

    it("onEditSchema work as expected", function () {
      var schema = new mockSchema($q);
      $scope.schema = schema;

      // @todo
      $scope.onEditSchema();

      $scope.$digest();
    });

    it("resetSchemaForms work as expected", function () {
      // @todo
      $scope.resetSchemaForms();
    });

    it("setTable work as expected", function () {
      // @todo
      $scope.setTable();
    });

    it("showProperties work as expected", function () {
      // @todo
      $scope.showProperties();
    });
  });

});

