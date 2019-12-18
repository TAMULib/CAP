describe("model: RepositoryViewContext", function () {
  var $rootScope, $scope, WsApi, model, wsResponse;

  var initializeVariables = function (settings) {
    inject(function (_$rootScope_, _WsApi_) {
      $rootScope = _$rootScope_;

      WsApi = _WsApi_;

      wsResponse = {
        type: "payload",
        payload: {
          RepositoryViewContext: dataRepositoryViewContext1
        }
      };

      WsApi.mockFetchResponse(wsResponse);
    });
  };

  var initializeModel = function (settings) {
    inject(function (_RepositoryViewContext_) {
      $scope = $rootScope.$new();

      model = angular.extend(new _RepositoryViewContext_(), dataRepositoryViewContext1);
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("mock.wsApi");

    initializeVariables();
    // @todo more work needed.
    //initializeModel();
  });

  describe("Is the model", function () {
    it("defined", function () {
      // @todo more work needed.
      //expect(model).toBeDefined();
    });
  });

  describe("Is the model method", function () {
    var methods = [
      //"advancedUpdate",
      //"createContainer",
      //"createMetadata",
      //"createResource",
      //"createVersion",
      //"deleteVersion",
      //"getCachedChildContext",
      //"getChildContext",
      //"getQueryHelp",
      //"refreshContext",
      //"reloadContext",
      //"removeContainers",
      //"removeMetadata",
      //"removeResources",
      //"revertVersion",
      //"updateMetadatum"
    ];

    for (var i in methods) {
      it(methods[i] + " defined", function () {
        expect(model[methods[i]]).toBeDefined();
        expect(typeof model[methods[i]]).toEqual("function");
      });
    }
  });

  describe("Does the model method", function () {
    it("advancedUpdate work as expected", function () {
      // @todo
    });

    it("createContainer work as expected", function () {
      // @todo
    });

    it("createMetadata work as expected", function () {
      // @todo
    });

    it("createResource work as expected", function () {
      // @todo
    });

    it("createVersion work as expected", function () {
      // @todo
    });

    it("deleteVersion work as expected", function () {
      // @todo
    });

    it("getChildContext work as expected", function () {
      // @todo
    });

    it("getCachedChildContext work as expected", function () {
      // @todo
    });

    it("getQueryHelp work as expected", function () {
      // @todo
    });

    it("refreshContext work as expected", function () {
      // @todo
    });

    it("reloadContext work as expected", function () {
      // @todo
    });

    it("removeContainers work as expected", function () {
      // @todo
    });

    it("removeMetadata work as expected", function () {
      // @todo
    });

    it("removeResources work as expected", function () {
      // @todo
    });

    it("revertVersion work as expected", function () {
      // @todo
    });

    it("updateMetadatum work as expected", function () {
      // @todo
    });
  });
});
