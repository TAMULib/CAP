cap.model("RepositoryView", function($location, $timeout, $cookies, $interval, $q, HttpMethodVerbs, RepositoryViewContext, WsApi, StorageService, UserService) {
  return function RepositoryView() {
    var repositoryView = this;

    var cache = {};

    repositoryView.currentContext = {};

    repositoryView.cacheContext = function(context) {
      cache[context.uri] = context;
    };

    repositoryView.getCachedContext = function(contextUri) {
      return cache[contextUri];
    };

    repositoryView.removeCachedContext = function(contextUri) {
      delete cache[contextUri];
    };

    repositoryView.clearCache = function() {
      angular.forEach(cache, function(v,uri){
        repositoryView.removeCachedContext(uri);
      });
    };

    repositoryView.getContext = function(contextUri, reload) {
      var context = repositoryView.getCachedContext(contextUri);
      if(context === undefined) {
        context = new RepositoryViewContext({
          repositoryView: repositoryView,
          uri: contextUri,
          fetch: true
        });
        console.log(context.repositoryView);
        repositoryView.cacheContext(context);
      } else if(reload) {
       context.reloadContext();
      }
      return context;
    };

    repositoryView.loadContext = function(contextUri, reload) {
      $location.search("context", contextUri);
      repositoryView.currentContext = repositoryView.getContext(contextUri, reload);
      return repositoryView.currentContext;
    };

    repositoryView.performRequest = function(endpoint, options) {

      var defaultPathValues = {
        type: repositoryView.type,
        repositoryViewId: repositoryView.id
      };

      if(options.pathValues) {
        angular.extend(options.pathValues, defaultPathValues);
      } else {
        options.pathValues = defaultPathValues;
      }
      
      return WsApi.fetch(endpoint, options);

    };

    repositoryView.startTransaction = function() {

      var transactionPromise = repositoryView.performRequest(repositoryView.getMapping().transaction, {
        method: HttpMethodVerbs.GET,
      });

      transactionPromise.then(function(apiRes) {
        var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
        repositoryView.clearCache();
        repositoryView.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuration);
        repositoryView.currentContext.reloadContext().then(function() {
          repositoryView.startTransactionTimer().then(function() {
            repositoryView.currentContext.reloadContext();
          });
        });
      });

      return transactionPromise;

    };

    repositoryView.refreshTransaction = function() {

      var transaction = repositoryView.getTransaction();

      var refeshPromise = repositoryView.performRequest(repositoryView.getMapping().transaction, {
        method: HttpMethodVerbs.PUT,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      refeshPromise.then(function(apiRes) {
        var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
        repositoryView.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuraction);
      });

      return refeshPromise;

    };

    repositoryView.commitTransaction = function() {
      var transaction = repositoryView.getTransaction();

      var refeshPromise = repositoryView.performRequest(repositoryView.getMapping().transaction, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      refeshPromise.then(function() {
        repositoryView.stopTransactionTimer();
      });

      return refeshPromise;
    };

    repositoryView.rollbackTransaction = function() {
      var transaction = repositoryView.getTransaction();

      var rollbackPromise = repositoryView.performRequest(repositoryView.getMapping().transaction, {
        method: HttpMethodVerbs.DELETE,
        query: {
          contextUri: transaction.transactionToken
        }
      });

      rollbackPromise.then(function() {
        repositoryView.stopTransactionTimer();
      });

      return rollbackPromise;
    };

    repositoryView.createTransactionCookie = function(token, secondsRemaining, transactionDuration) {

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

    repositoryView.getTransaction = function() {
      var transactionCookie = $cookies.getObject("transaction"); 
      if(transactionCookie)  {
        transactionObject.active = true;
      } else {
        transactionObject.active = false;
      }

      angular.extend(transactionObject, transactionCookie);

      return transactionObject;

    };

    repositoryView.transactionTimer = undefined;
    repositoryView.startTransactionTimer = function() {

      var timerDefer = $q.defer();

      if(!angular.isDefined(repositoryView.transactionTimer)) {

        repositoryView.transactionTimer = $interval(function() {

          var transaction = repositoryView.getTransaction();

          var secondsremaining = transaction.secondsRemaining-1;

          repositoryView.createTransactionCookie(transaction.transactionToken, secondsremaining);
          
          if(repositoryView.getTransaction().secondsRemaining<1) {
           repositoryView.stopTransactionTimer();
          }
        }, 1000);
      }

      return timerDefer.promise;

    };

    repositoryView.stopTransactionTimer = function() {
      if(repositoryView.transactionTimer) {
        $interval.cancel(repositoryView.transactionTimer);
        repositoryView.transactionTimer = undefined;
        $timeout(function() {
          transactionObject.active = false;
          var transaction = repositoryView.getTransaction();
          $cookies.remove("transaction", {
            path: "/"
          });
          repositoryView.currentContext.uri = repositoryView.currentContext.uri.replace(transaction.transactionToken, repositoryView.rootUri);
          repositoryView.clearCache();
          $location.search("context", repositoryView.currentContext.uri);
        },250);
      }
    };


    WsApi.listen("/queue/context").then(null, null, function(response) {
      var context = angular.fromJson(response.body).payload.RepositoryViewContext;
      if(cache[context.triple.subject] === undefined) {
        cache[context.triple.subject] = new RepositoryViewContext({
          repositoryView: repositoryView,
          uri: context.triple.subject,
          fetch: false
        });
      }
      angular.extend(cache[context.triple.subject], context);
    });

    var user = UserService.getCurrentUser();

    WsApi.listen("/queue/transaction/"+user.uin).then(null, null, function(apiRes) {
      var transactionDetails = angular.fromJson(apiRes.body).payload.FedoraTransactionDetails;
      repositoryView.createTransactionCookie(transactionDetails.transactionToken, transactionDetails.secondsRemaining, transactionDetails.transactionDuraction);
    });

    return repositoryView;
  };
});
