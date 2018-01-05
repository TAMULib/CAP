cap.model("IRContext", function(WsApi, HttpMethodVerbs) {
  return function IRContext() {

    var irContext = this;

    irContext.before(function() {
      
      var loadPromise = WsApi.fetch(irContext.getMapping().load, {
        method: HttpMethodVerbs.GET,
        pathValues: {
          type: irContext.ir.type,
          irid: irContext.ir.id
        },
        query: {
          contextUri: irContext.contextURI
        }
      });

      loadPromise.then(function(res) {

        angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
        console.log(res.body, irContext);
      });

      return loadPromise;

    });

    return this;

  };
});