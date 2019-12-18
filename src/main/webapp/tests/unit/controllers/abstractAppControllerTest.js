describe("controller: AbstractAppController", function () {
  var $q, $scope, WsApi, controller;

  var initializeVariables = function () {
    inject(function (_$q_, _WsApi_) {
      $q = _$q_;

      WsApi = _WsApi_;
    });
  };

  var initializeController = function (settings) {
    inject(function (_$controller_, _$rootScope_, _ModalService_) {
      $scope = _$rootScope_.$new();

      sessionStorage.role = settings && settings.role ? settings.role : "ROLE_ADMIN";
      sessionStorage.token = settings && settings.token ? settings.token : "faketoken";

      controller = _$controller_("AbstractAppController", {
        $q: $q,
        $scope: $scope,
        ModalService: _ModalService_,
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
    module("mock.modalService");
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
    it("closeModal should be defined", function () {
      expect($scope.closeModal).toBeDefined();
      expect(typeof $scope.closeModal).toEqual("function");
    });

    it("isCollapsable should be defined", function () {
      expect($scope.isCollapsable).toBeDefined();
      expect(typeof $scope.isCollapsable).toEqual("function");
    });

    it("isCurator should be defined", function () {
      expect($scope.isCurator).toBeDefined();
      expect(typeof $scope.isCurator).toEqual("function");
    });
  });

  describe("Do the $scope methods work as expected", function () {
    it("closeModal should work", function () {
      // @todo
      $scope.closeModal();
    });

    it("isCollapsable should work", function () {
      var triples = [];
      var predicate = "";

      // @todo
      $scope.isCollapsable(triples, predicate);
    });

    it("isCurator should work", function () {
      // @todo
      $scope.isCurator();
    });
  });

});
