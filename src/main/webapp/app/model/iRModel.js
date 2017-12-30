cap.model("IR", function(WsApi, IRRepo) {
  return function IR() {

    var ir = this;
    var contextLoadedByUri = {};
    var containers = [];
    var properties = [];

    ir.createContainer = function(createForm) {
      var createPromise = WsApi.fetch(ir.getMapping().createContainer, {
        data: {
          ir: ir,
          name: createForm.name
        }
      });
      createPromise.then(function() {
        ir.getContainers(true);
      });
      return createPromise;
    };

    ir.removeContainers = function(containerUris) {
      var removePromise = WsApi.fetch(ir.getMapping().deleteContainers, {
        data: {
          ir: ir,
          containerUris: containerUris
        }
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
        WsApi.fetch(ir.getMapping().getContainers, {
          data: ir
        }).then(function(res) {
          angular.extend(containers, angular.fromJson(res.body).payload['ArrayList<String>']);
        });
      }
      return containers;
    };

    ir.getProperties = function(forceUpdate) {
      if((ir.contextUri !== contextLoadedByUri.properties)||forceUpdate) {
        contextLoadedByUri.properties = ir.contextUri;
        properties.length = 0;
        IRRepo.getProperties(ir).then(function(res) {
          angular.extend(properties, angular.fromJson(res.body).payload['ArrayList<String>']);
        });
      }
      return properties;
    };

    return ir;

  };

});