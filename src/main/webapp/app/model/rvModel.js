cap.model("RV", function($location, $timeout, $cookies, $interval, $q, HttpMethodVerbs, RVContext, WsApi, StorageService, UserService) {
  return function RV() {
    var rv = this;

    var cache = {};

    rv.currentContext = {};

    rv.cacheContext = function(context) {
      cache[context.uri] = context;
    };

    rv.getCachedContext = function(contextUri) {
      return cache[contextUri];
    };

    rv.removeCachedContext = function(contextUri) {
      delete cache[contextUri];
    };

    rv.clearCache = function() {
      angular.forEach(cache, function(v,uri){
        rv.removeCachedContext(uri);
      });
    };

    rv.getContext = function(contextUri, reload) {
      var context = rv.getCachedContext(contextUri);
      if(context === undefined) {
        context = new RVContext({
          rv: rv,
          uri: contextUri,
          fetch: true
        });
        console.log(context.rv);
        rv.cacheContext(context);
      } else if(reload) {
       context.reloadContext();
      }
      return context;
    };

    rv.loadContext = function(contextUri, reload) {
      $location.search("context", contextUri);
      rv.currentContext = rv.getContext(contextUri, reload);
      return rv.currentContext;
    };

    rv.performRequest = function(endpoint, options) {

      var defaultPathValues = {
        type: rv.type,
        rvid: rv.id
      };

      if(options.pathValues) {
        angular.extend(options.pathValues, defaultPathValues);
      } else {
        options.pathValues = defaultPathValues;
      }
      
      return WsApi.fetch(endpoint, options);

    };

    rv.startTransaction = function() {

      var transactionPromise = rv.performRequest(rv.getMapping().transaction, {
        method: HttpMethodVerbs.GET,
      });

      transactionPromise.then(function(apiRes) {
        var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
        rv.clearCache();
        rv.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuration);
        rv.currentContext.reloadContext().then(function() {
          rv.startTransactionTimer().then(function() {
            rv.currentContext.reloadContext();
          });
        });
      });

      return transactionPromise;

    };

    rv.refreshTransaction = function() {

      var transaction = rv.getTransaction();

      var refeshPromise = rv.performRequest(rv.getMapping().transaction, {
        method: HttpMethodVerbs.PUT,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      refeshPromise.then(function(apiRes) {
        var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
        rv.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuraction);
      });

      return refeshPromise;

    };

    rv.commitTransaction = function() {
      var transaction = rv.getTransaction();

      var refeshPromise = rv.performRequest(rv.getMapping().transaction, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      refeshPromise.then(function() {
        rv.stopTransactionTimer();
      });

      return refeshPromise;
    };

    rv.rollbackTransaction = function() {
      var transaction = rv.getTransaction();

      var rollbackPromise = rv.performRequest(rv.getMapping().transaction, {
        method: HttpMethodVerbs.DELETE,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      rollbackPromise.then(function() {
        rv.stopTransactionTimer();
      });

      return rollbackPromise;
    };

    rv.createTransactionCookie = function(token, secondsRemaining, transactionDuration) {

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

    rv.getTransaction = function() {
      var transactionCookie = $cookies.getObject("transaction"); 
      if(transactionCookie)  {
        transactionObject.active = true;
      } else {
        transactionObject.active = false;
      }

      angular.extend(transactionObject, transactionCookie);

      return transactionObject;

    };

    rv.transactionTimer = undefined;
    rv.startTransactionTimer = function() {

      var timerDefer = $q.defer();

      if(!angular.isDefined(rv.transactionTimer)) {

        rv.transactionTimer = $interval(function() {

          var transaction = rv.getTransaction();

          var secondsremaining = transaction.secondsRemaining-1;

          rv.createTransactionCookie(transaction.transactionToken, secondsremaining);
          
          if(rv.getTransaction().secondsRemaining<1) {
           rv.stopTransactionTimer();
          }
        }, 1000);
      }

      return timerDefer.promise;

    };

    rv.stopTransactionTimer = function() {
      if(rv.transactionTimer) {
        $interval.cancel(rv.transactionTimer);
        rv.transactionTimer = undefined;
        $timeout(function() {
          transactionObject.active = false;
          var transaction = rv.getTransaction();
          $cookies.remove("transaction", {
            path: "/"
          });
          rv.currentContext.uri = rv.currentContext.uri.replace(transaction.transactionToken, rv.rootUri);
          rv.clearCache();
          $location.search("context", rv.currentContext.uri);
        },250);
      }
    };


    WsApi.listen("/queue/context").then(null, null, function(response) {
      var context = angular.fromJson(response.body).payload.RVContext;
      if(cache[context.triple.subject] === undefined) {
        cache[context.triple.subject] = new RVContext({
          rv: rv,
          uri: context.triple.subject,
          fetch: false
        });
      }
      angular.extend(cache[context.triple.subject], context);
    });

    var user = UserService.getCurrentUser();

    WsApi.listen("/queue/transaction/"+user.uin).then(null, null, function(apiRes) {
      var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
      rv.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuraction);
    });

    return rv;
  };
});
