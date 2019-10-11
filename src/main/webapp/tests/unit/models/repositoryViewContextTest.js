describe("model: RepositoryViewContext", function () {
  var $rootScope, $scope, WsApi, model, wsResponse;

  var initializeVariables = function(settings) {
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

  var initializeModel = function(settings) {
    inject(function (RepositoryViewContext) {
      $scope = $rootScope.$new();

      model = angular.extend(new RepositoryViewContext(), dataRepositoryViewContext1);
    });
  };

  beforeEach(function() {
    module("core");
    module("cap");
    module("mock.wsApi");

    initializeVariables();
    // @todo more work needed.
    //initializeModel();
  });

  describe("Is the model defined", function () {
    it("should be defined", function () {
      // @todo more work needed.
      //expect(model).toBeDefined();
    });
  });

  describe("Are the model methods defined", function () {
    it("advancedUpdate should be defined", function () {
      //expect(model.advancedUpdate).toBeDefined();
      //expect(typeof model.advancedUpdate).toEqual("function");
    });

    it("createContainer should be defined", function () {
      //expect(model.createContainer).toBeDefined();
      //expect(typeof model.createContainer).toEqual("function");
    });

    it("createMetadata should be defined", function () {
      //expect(model.createMetadata).toBeDefined();
      //expect(typeof model.createMetadata).toEqual("function");
    });

    it("createResource should be defined", function () {
      //expect(model.createResource).toBeDefined();
      //expect(typeof model.createResource).toEqual("function");
    });

    it("createVersion should be defined", function () {
      //expect(model.createVersion).toBeDefined();
      //expect(typeof model.createVersion).toEqual("function");
    });

    it("deleteVersion should be defined", function () {
      //expect(model.deleteVersion).toBeDefined();
      //expect(typeof model.deleteVersion).toEqual("function");
    });

    it("getCachedChildContext should be defined", function () {
      //expect(model.getCachedChildContext).toBeDefined();
      //expect(typeof model.getCachedChildContext).toEqual("function");
    });

    it("getChildContext should be defined", function () {
      //expect(model.getChildContext).toBeDefined();
      //expect(typeof model.getChildContext).toEqual("function");
    });

    it("getQueryHelp should be defined", function () {
      //expect(model.getQueryHelp).toBeDefined();
      //expect(typeof model.getQueryHelp).toEqual("function");
    });

    it("refreshContext should be defined", function () {
      //expect(model.refreshContext).toBeDefined();
      //expect(typeof model.refreshContext).toEqual("function");
    });

    it("reloadContext should be defined", function () {
      //expect(model.reloadContext).toBeDefined();
      //expect(typeof model.reloadContext).toEqual("function");
    });

    it("removeContainers should be defined", function () {
      //expect(model.removeContainers).toBeDefined();
      //expect(typeof model.removeContainers).toEqual("function");
    });

    it("removeMetadata should be defined", function () {
      //expect(model.removeMetadata).toBeDefined();
      //expect(typeof model.removeMetadata).toEqual("function");
    });

    it("removeResources should be defined", function () {
      //expect(model.removeResources).toBeDefined();
      //expect(typeof model.removeResources).toEqual("function");
    });

    it("revertVersion should be defined", function () {
      //expect(model.revertVersion).toBeDefined();
      //expect(typeof model.revertVersion).toEqual("function");
    });

    it("updateMetadatum should be defined", function () {
      //expect(model.updateMetadatum).toBeDefined();
      //expect(typeof model.updateMetadatum).toEqual("function");
    });
  });

  describe("Are the model methods working as expected", function () {
    it("advancedUpdate should work", function () {
      // @todo
    });

    it("createContainer should work", function () {
      // @todo
    });

    it("createMetadata should work", function () {
      // @todo
    });

    it("createResource should work", function () {
      // @todo
    });

    it("createVersion should work", function () {
      // @todo
    });

    it("deleteVersion should work", function () {
      // @todo
    });

    it("getChildContext should work", function () {
      // @todo
    });

    it("getCachedChildContext should work", function () {
      // @todo
    });

    it("getQueryHelp should work", function () {
      // @todo
    });

    it("refreshContext should work", function () {
      // @todo
    });

    it("reloadContext should work", function () {
      // @todo
    });

    it("removeContainers should work", function () {
      // @todo
    });

    it("removeMetadata should work", function () {
      // @todo
    });

    it("removeResources should work", function () {
      // @todo
    });

    it("revertVersion should work", function () {
      // @todo
    });

    it("updateMetadatum should work", function () {
      // @todo
    });
  });
});
