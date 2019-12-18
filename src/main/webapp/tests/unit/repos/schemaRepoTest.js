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

  describe("Is the repo", function () {
    it("defined", function () {
      expect(repo).toBeDefined();
    });
  });

  describe("Is the repo method", function () {
    var methods = [
      "findProperties"
    ];

    for (var i in methods) {
      it(methods[i] + " defined", function () {
        expect(repo[methods[i]]).toBeDefined();
        expect(typeof repo[methods[i]]).toEqual("function");
      });
    }
  });

  describe("Does the repo method", function () {
    it("findProperties work as expected", function () {
      var schema = new mockSchema($q);

      // @todo
      repo.findProperties(schema);

      $scope.$digest();
    });
  });

});
