cap.model("IR", function($location, $timeout, $cookies, $interval, $q, HttpMethodVerbs, IRContext, WsApi, StorageService, UserService) {
  return function IR() {
    var ir = this;

    var cache = {};

    ir.currentContext = {};

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
      ir.currentContext = ir.getContext(contextUri, reload);
      return ir.currentContext;
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

    ir.startTransaction = function() {

      var transactionPromise = ir.performRequest(ir.getMapping().transaction, {
        method: HttpMethodVerbs.GET,
      });

      transactionPromise.then(function(apiRes) {
        var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
        ir.clearCache();
        ir.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuration);
        ir.currentContext.reloadContext().then(function() {
          ir.startTransactionTimer().then(function() {
            ir.currentContext.reloadContext();
          });
        });
      });

      return transactionPromise;

    };

    ir.refreshTransaction = function() {

      var transaction = ir.getTransaction();

      var refeshPromise = ir.performRequest(ir.getMapping().transaction, {
        method: HttpMethodVerbs.PUT,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      refeshPromise.then(function(apiRes) {
        var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
        ir.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuraction);
      });

      return refeshPromise;

    };

    ir.commitTransaction = function() {
      var transaction = ir.getTransaction();

      var refeshPromise = ir.performRequest(ir.getMapping().transaction, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      refeshPromise.then(function() {
        ir.stopTransactionTimer();
      });

      return refeshPromise;
    };

    ir.rollbackTransaction = function() {
      var transaction = ir.getTransaction();

      var rollbackPromise = ir.performRequest(ir.getMapping().transaction, {
        method: HttpMethodVerbs.DELETE,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      rollbackPromise.then(function() {
        ir.stopTransactionTimer();
      });

      return rollbackPromise;
    };

    ir.createTransactionCookie = function(token, secondsRemaining, transactionDuration) {

      var cookie = {
        transactionToken: token,
        secondsRemaining: secondsRemaining,
        transactionDuration: transactionDuration
      };

      var expiration = new Date();
      expiration.setSeconds(expiration.getSeconds() + secondsRemaining);

      $cookies.putObject("transaction", cookie, {
        expires: expiration,
        path: "/"
      });
    };

    var transactionObject = {
      active: false
    };

    ir.getTransaction = function() {
      var transactionCookie = $cookies.getObject("transaction"); 
      if(transactionCookie)  {
        transactionObject.active = true;
      } else {
        transactionObject.active = false;
      }

      angular.extend(transactionObject, transactionCookie);

      return transactionObject;

    };

    ir.transactionTimer = undefined;
    ir.startTransactionTimer = function() {

      var timerDefer = $q.defer();

      if(!angular.isDefined(ir.transactionTimer)) {

        ir.transactionTimer = $interval(function() {

          var transaction = ir.getTransaction();

          var secondsremaining = transaction.secondsRemaining-1;

          ir.createTransactionCookie(transaction.transactionToken, secondsremaining);
          
          if(ir.getTransaction().secondsRemaining<1) {
           ir.stopTransactionTimer();
          }
        }, 1000);
      }

      return timerDefer.promise;

    };

    ir.stopTransactionTimer = function() {
      if(ir.transactionTimer) {
        $interval.cancel(ir.transactionTimer);
        ir.transactionTimer = undefined;
        $timeout(function() {
          transactionObject.active = false;
          var transaction = ir.getTransaction();
          $cookies.remove("transaction", {
            path: "/"
          });
          ir.currentContext.uri = ir.currentContext.uri.replace(transaction.transactionToken, ir.rootUri);
          ir.currentContext.reloadContext();
        }, 500);
      }
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
