cap.model("IR", function(WsApi) {
  return function IR() {

    var ir = this;
    var lastContextUri = "";
    var containers = [];

    ir.createContainer = function() {
      var createPromise = WsApi.fetch(ir.getMapping().createContainer, {
        data: ir
      });
      createPromise.then(function() {
        ir.getContainers(true);
      });
      return createPromise;
    };

    ir.getContainers = function(forceUpdate) {
      if((ir.contextUri !== lastContextUri)||forceUpdate) {
        lastContextUri = ir.contextUri;
        containers.length = 0;
        WsApi.fetch(ir.getMapping().getContainers, {
          data: ir
        }).then(function(res) {
          angular.extend(containers, angular.fromJson(res.body).payload['ArrayList<String>']);
        });
      }
      return containers;
    };

    return ir;

  };

});