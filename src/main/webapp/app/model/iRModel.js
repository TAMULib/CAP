cap.model("IR", function() {
  return function IR() {
    var ir = this;

    var cache = {};

    ir.cacheContext = function(context) {
      cache[context.uri] = context;
    };

    ir.getCachedContext = function(contextUri) {
      if(cache[contextUri]) {
        console.log('hit', contextUri, cache);
      }
      return cache[contextUri];
    };

    return ir;
  };
});
