cap.model("IR", function($location, IRContext, WsApi) {
  return function IR() {
    var ir = this;

    var cache = {};

    ir.cacheContext = function(context) {
      cache[context.uri] = context;
    };

    ir.getCachedContext = function(contextUri) {
      return cache[contextUri];
    };

    ir.removeCachedContext = function(contextUri) {
      delete cache[contextUri];
    };

    ir.getContext = function(contextUri, reload) {
      var context = reload ? undefined : ir.getCachedContext(contextUri);
      if(context === undefined) {
        ir.removeCachedContext(contextUri);
        context = new IRContext({
          ir: ir,
          uri: contextUri,
          fetch: true
        });
      }
      return context;
    };

    ir.loadContext = function(contextUri, reload) {
      $location.search("context", contextUri);
      return ir.getContext(contextUri, reload);
    };

    WsApi.listen("/queue/context").then(null, null, function(response) {
      var context = angular.fromJson(response.body).payload.IRContext;
      if(cache[context.triple.subject] === undefined) {
        cache[context.triple.subject] = new IRContext({
          ir: ir,
          uri: context.triple.subject,
          fetch: false
        });
      }
      angular.extend(cache[context.triple.subject], context);
    });

    return ir;
  };
});
