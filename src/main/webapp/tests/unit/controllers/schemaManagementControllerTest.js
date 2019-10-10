describe("controller: SchemaManagementController", function () {
  var $q, $scope, $timeout, MockedSchema, MockedUser, NgTableParams, WsApi, controller;

  var initializeVariables = function() {
    inject(function (_$q_, _$timeout_, _WsApi_) {
      $q = _$q_;
      $timeout = _$timeout_;

      MockedSchema = new mockSchema($q);
      MockedUser = new mockUser($q);

      NgTableParams = mockNgTableParams;
      WsApi = _WsApi_;
    });
  };

  var initializeController = function(settings) {
    inject(function (_$controller_, _$rootScope_, _AssumedControl_, _AuthService_, _SchemaRepo_, _StorageService_, _UserRepo_, _UserService_) {
      $scope = _$rootScope_.$new();

      sessionStorage.role = settings && settings.role ? settings.role : "ROLE_ADMIN";
      sessionStorage.token = settings && settings.token ? settings.token : "faketoken";

      controller = _$controller_("SchemaManagementController", {
        $q: $q,
        $scope: $scope,
        AssumedControl: _AssumedControl_,
        AuthService: _AuthService_,
        NgTableParams: NgTableParams,
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

  beforeEach(function() {
    module("core");
    module("cap");
    module("mock.assumedControl");
    module("mock.authService");
    module("mock.ngTableParams");
    module("mock.schema", function($provide) {
      var Schema = function() {
        return MockedSchema;
      };
      $provide.value("Schema", Schema);
    });
    module("mock.schemaRepo");
    module("mock.storageService");
    module("mock.user", function($provide) {
      var User = function() {
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
    it("createSchema should be defined", function () {
      expect($scope.createSchema).toBeDefined();
      expect(typeof $scope.createSchema).toEqual("function");
    });

    it("deleteSchema should be defined", function () {
      expect($scope.deleteSchema).toBeDefined();
      expect(typeof $scope.deleteSchema).toEqual("function");
    });

    it("editSchema should be defined", function () {
      expect($scope.editSchema).toBeDefined();
      expect(typeof $scope.editSchema).toEqual("function");
    });

    it("onCancelCreateSchema should be defined", function () {
      expect($scope.onCancelCreateSchema).toBeDefined();
      expect(typeof $scope.onCancelCreateSchema).toEqual("function");
    });

    it("onCancelDeleteSchema should be defined", function () {
      expect($scope.onCancelDeleteSchema).toBeDefined();
      expect(typeof $scope.onCancelDeleteSchema).toEqual("function");
    });

    it("onCancelEditSchema should be defined", function () {
      expect($scope.onCancelEditSchema).toBeDefined();
      expect(typeof $scope.onCancelEditSchema).toEqual("function");
    });

    it("onCreateSchema should be defined", function () {
      expect($scope.onCreateSchema).toBeDefined();
      expect(typeof $scope.onCreateSchema).toEqual("function");
    });

    it("onDeleteSchema should be defined", function () {
      expect($scope.onDeleteSchema).toBeDefined();
      expect(typeof $scope.onDeleteSchema).toEqual("function");
    });

    it("onEditSchema should be defined", function () {
      expect($scope.onEditSchema).toBeDefined();
      expect(typeof $scope.onEditSchema).toEqual("function");
    });

    it("resetSchemaForms should be defined", function () {
      expect($scope.resetSchemaForms).toBeDefined();
      expect(typeof $scope.resetSchemaForms).toEqual("function");
    });

    it("setTable should be defined", function () {
      expect($scope.setTable).toBeDefined();
      expect(typeof $scope.setTable).toEqual("function");
    });

    it("showProperties should be defined", function () {
      expect($scope.showProperties).toBeDefined();
      expect(typeof $scope.showProperties).toEqual("function");
    });
  });

  describe("Do the $scope methods work as expected", function () {
    it("createSchema should work", function () {
      $scope.schema = undefined;
      spyOn($scope, "openModal");

      $scope.createSchema();
      expect($scope.openModal).toHaveBeenCalled();
      expect($scope.schema).toBeDefined();
    });

    it("deleteSchema should work", function () {
      // @todo
      $scope.deleteSchema();
    });

    it("editSchema should work", function () {
      // @todo
      $scope.editSchema();
    });

    it("onCancelCreateSchema should work", function () {
      // @todo
      $scope.onCancelCreateSchema();

      $timeout.flush();
    });

    it("onCancelDeleteSchema should work", function () {
      // @todo
      $scope.onCancelDeleteSchema();
    });

    it("onCancelEditSchema should work", function () {
      // @todo
      $scope.onCancelEditSchema();

      $timeout.flush();
    });

    it("onCreateSchema should work", function () {
      var schema = new mockSchema($q);
      $scope.schema = schema;

      // @todo
      $scope.onCreateSchema();

      $scope.$digest();
    });

    it("onDeleteSchema should work", function () {
      var schema = new mockSchema($q);
      $scope.schema = schema;

      // @todo
      $scope.onDeleteSchema();

      $scope.$digest();
    });

    it("onEditSchema should work", function () {
      var schema = new mockSchema($q);
      $scope.schema = schema;

      // @todo
      $scope.onEditSchema();

      $scope.$digest();
    });

    it("resetSchemaForms should work", function () {
      // @todo
      $scope.resetSchemaForms();
    });

    it("setTable should work", function () {
      // @todo
      $scope.setTable();
    });

    it("showProperties should work", function () {
      // @todo
      $scope.showProperties();
    });
  });

});

