cap.model("IR", function($location, $timeout, $cookies, $interval, $q, HttpMethodVerbs, IRContext, WsApi, StorageService, UserService) {
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

    ir.createTransactionCookie = function(token, secondsRemaining, transactionDuration) {

      var cookie = {
        transactionToken: token,
        secondsRemaining: secondsRemaining,
        transactionDuration: transactionDuration
      };

      var expiration = new Date();
      expiration.setSeconds(expiration.getSeconds() + secondsRemaining);

      $cookies.put("transaction", angular.toJson(cookie), {
        expires: expiration,
        path: "/"
      });
    };

    var transactionObject = {
      active: false
    };

    ir.getTransaction = function() {
      var rawTransactionCookie = $cookies.get("transaction");
      var transactionCookie; 
      if(rawTransactionCookie)  {
        transactionCookie = angular.fromJson(rawTransactionCookie.replace(/\+/g, ""));
        transactionObject.active = true;
      } else {
        transactionObject.active = false;
      }

      angular.extend(transactionObject, transactionCookie);

      return transactionObject;

    };

    ir.transactionTimer;
    ir.startTransactionTimer = function() {

      var timerDefer = $q.defer();

      if(!angular.isDefined(ir.transactionTimer)) {
        ir.transactionTimer = $interval(function() {
          
          var transaction = ir.getTransaction();

          var secondsremaining = transaction.secondsRemaining-1;

          ir.createTransactionCookie(transaction.transactionToken, secondsremaining);
          
          if(ir.getTransaction().secondsRemaining<1) {
            $interval.cancel(transactionTimer);
            transactionTimer = undefined;
            timerDefer.resolve();
          }
        }, 1000);
      }

      return timerDefer.promise;

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

    var user = UserService.getCurrentUser();

    WsApi.listen("/queue/transaction/"+user.uin).then(null, null, function(apiRes) {
      var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
      ir.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuraction);

    });

    return ir;
  };
});
