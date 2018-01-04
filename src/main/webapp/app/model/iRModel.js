cap.model("IR", function(WsApi, IRRepo, api) {
  return function IR() {

    var ir = this;
    var contextLoadedByUri = {};
    var containers = [];
    var properties = {};

    ir.createContainer = function(createForm) {
      console.log(ir);
      var createPromise = WsApi.fetch(api.IRProxy.createContainer, {
        pathValues: {
          irid: ir.id,
          type: ir.type
        },
        query: {
          contextUri: ir.contextUri
        },
        data: {
          name: createForm.name
        }
      });
      createPromise.then(function() {
        ir.getContainers(true);
      });
      return createPromise;
    };

    ir.removeContainers = function(containerUris) {
      var removePromise = WsApi.fetch(api.IRProxy.deleteContainers, {
        pathValues: {
          irid: ir.id,
          type: ir.type
        },
        query: {
          contextUri: ir.contextUri
        },
        data: containerUris
      });
      removePromise.then(function() {
        ir.getContainers(true);
      });
      return removePromise;
    };

    ir.getContainers = function(forceUpdate) {
      if((ir.contextUri !== contextLoadedByUri.containers)||forceUpdate) {
        contextLoadedByUri.containers = ir.contextUri;
        containers.length = 0;
        WsApi.fetch(api.IRProxy.getContainers, {
          pathValues: {
            irid: ir.id,
            type: ir.type
          },
          query: {
            contextUri: ir.contextUri
          } 
        }).then(function(res) {
          angular.extend(containers, angular.fromJson(res.body).payload['ArrayList<String>']);
        });
      }
      return containers;
    };

    ir.getProperties = function(forceUpdate) {
      if((ir.contextUri !== contextLoadedByUri.properties)||forceUpdate) {
        contextLoadedByUri.properties = ir.contextUri;
        angular.forEach(properties, function(v,k) {
          delete properties[k];
        });
        IRRepo.getProperties(ir).then(function(res) {
          angular.extend(properties, angular.fromJson(res.body).payload['HashMap']);
        });
      }
      return properties;
    };

    return ir;

  };

});