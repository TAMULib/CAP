cap.model("IR", function($location, $timeout, IRContext, WsApi, StorageService) {
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

    ir.clearCache = function() {
      angular.forEach(cache, function(v,uri){
        ir.removeCachedContext(uri);
      });
    };

    ir.getContext = function(contextUri, reload) {
      var context = ir.getCachedContext(contextUri);
      if(context === undefined) {
        context = new IRContext({
          ir: ir,
          uri: contextUri,
          fetch: true
        });
        ir.cacheContext(context);
      } else if(reload) {
       context.reloadContext();
      }
      return context;
    };

    ir.loadContext = function(contextUri, reload) {
      $location.search("context", contextUri);
      return ir.getContext(contextUri, reload);
    };

    ir.performRequest = function(endpoint, options) {

      var defaultPathValues = {
        type: ir.type,
        irid: ir.id
      };

      if(options.pathValues) {
        angular.extend(options.pathValues, defaultPathValues);
      } else {
        options.pathValues = defaultPathValues;
      }
      
      return WsApi.fetch(endpoint, options);

    };

    ir.getTransactionTimeRemaining = function() {
      
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
