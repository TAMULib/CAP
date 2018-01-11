cap.model("IR", function($location, $q, IRContext) {
  return function IR() {
    var ir = this;

    var cache = {};

    ir.cacheContext = function(context) {
      cache[context.uri] = context;
    };

    ir.getCachedContext = function(contextUri) {
      return cache[contextUri];
    };

    ir.getContext = function(contextUri) {
      var context = ir.getCachedContext(contextUri);
      if(context === undefined) {
        context = new IRContext({
          ir: ir,
          uri: contextUri,
          fetch: true
        });
      }
      return context;
    };

    ir.loadContext = function(contextUri) {
      $location.search("context", contextUri);
      return ir.getContext(contextUri);
    };

    return ir;
  };
});
