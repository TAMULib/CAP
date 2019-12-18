describe("service: schemaRepo", function () {
  var $q, $rootScope, $scope, MockedSchema, WsApi, repo;

  var initializeVariables = function (settings) {
    inject(function (_$q_, _$rootScope_, _WsApi_) {
      $q = _$q_;
      $rootScope = _$rootScope_;

      MockedSchema = new mockSchema($q);
      WsApi = _WsApi_;
    });
  };

  var initializeRepo = function (settings) {
    inject(function ($injector, SchemaRepo) {
      $scope = $rootScope.$new();

      repo = SchemaRepo;
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("mock.schema", function ($provide) {
      var Schema = function () {
        return MockedSchema;
      };
      $provide.value("Schema", Schema);
    });
    module("mock.wsApi");

    initializeVariables();
    initializeRepo();
  });

  describe("Is the repo defined", function () {
    it("should be defined", function () {
      expect(repo).toBeDefined();
    });
  });

  describe("Are the repo methods defined", function () {
    it("findProperties should be defined", function () {
      expect(repo.findProperties).toBeDefined();
      expect(typeof repo.findProperties).toEqual("function");
    });
  });

  describe("Are the repo methods working as expected", function () {
    it("findProperties should work", function () {
      var schema = new mockSchema($q);

      // @todo
      repo.findProperties(schema);

      $scope.$digest();
    });
  });

});
