cap.model("RepositoryView", function($location, $timeout, $interval, $q, HttpMethodVerbs, RepositoryViewContext, WsApi, UserService) {
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
      angular.forEach(cache, function(v, uri){
        repositoryView.removeCachedContext(uri);
      });
    };

    repositoryView.getContext = function(contextUri, reload) {
      var context = repositoryView.getCachedContext(contextUri);
      if (context === undefined) {
        context = new RepositoryViewContext({
          repositoryView: repositoryView,
          uri: contextUri,
          fetch: true
        });
        repositoryView.cacheContext(context);
      } else if (reload) {
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
        method: HttpMethodVerbs.GET
      });

      transactionPromise.then(function(res) {
        var transactionDetails = angular.fromJson(res.body).payload.TransactionDetails;

        repositoryView.clearCache();

        angular.extend(repositoryView.currentContext, {
          transactionDetails: transactionDetails,
          uri: transactionDetails.token + "/" + repositoryView.currentContext.uri
        });

        $location.search("context", repositoryView.currentContext.uri);

        repositoryView.startTransactionTimer().then(function() {
          repositoryView.currentContext.reloadContext();
        });
      });
      return transactionPromise;
    };

    repositoryView.refreshTransaction = function() {
      var transaction = repositoryView.getTransaction();
      var refeshPromise = repositoryView.performRequest(repositoryView.getMapping().transaction, {
        method: HttpMethodVerbs.PUT,
        query: {
          contextUri: repositoryView.rootUri + (repositoryView.rootUri.endsWith('/') ? "" : "/") + transaction.token
        }
      });
      refeshPromise.then(function(res) {
        var transactionDetails = angular.fromJson(res.body).payload.TransactionDetails;
        angular.extend(repositoryView.currentContext, {
          transactionDetails: transactionDetails
        });
      });
      return refeshPromise;
    };

    repositoryView.commitTransaction = function() {
      var transaction = repositoryView.getTransaction();
      var refeshPromise = repositoryView.performRequest(repositoryView.getMapping().transaction, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: repositoryView.rootUri + (repositoryView.rootUri.endsWith('/') ? "" : "/") + transaction.token
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
          contextUri: repositoryView.rootUri + (repositoryView.rootUri.endsWith('/') ? "" : "/") + transaction.token
        }
      });
      rollbackPromise.then(function() {
        repositoryView.stopTransactionTimer();
      });
      return rollbackPromise;
    };

    repositoryView.getTransaction = function() {
      return repositoryView.currentContext.transactionDetails;
    };

    repositoryView.inTransaction = function() {
      return repositoryView.currentContext.transactionDetails !== null && 
        repositoryView.currentContext.transactionDetails !== undefined && 
        repositoryView.currentContext.transactionDetails.expiration > new Date().getTime();
    };

    repositoryView.isTransactionAboutToExpire = function() {
      return (repositoryView.currentContext.transactionDetails.expiration - new Date().getTime()) < 30000;
    };

    repositoryView.getTransactionSecondsRemaining = function() {
      var secondsRemaining = Math.round((repositoryView.currentContext.transactionDetails.expiration - new Date().getTime()) / 1000);
      return secondsRemaining > 9 ? secondsRemaining : '0' + secondsRemaining;
    };

    repositoryView.transactionTimer = undefined;

    repositoryView.startTransactionTimer = function() {
      var timerDefer = $q.defer();

      if (!angular.isDefined(repositoryView.transactionTimer)) {
        repositoryView.transactionTimer = $interval(function() {
          var transaction = repositoryView.getTransaction();
          if (transaction.expiration <= new Date().getTime()) {
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
          var transaction = repositoryView.getTransaction();
          angular.extend(repositoryView.currentContext, {
            transactionDetails: null,
            uri: repositoryView.currentContext.uri.replace(transaction.token + "/", '')
          });
          repositoryView.clearCache();
          $location.search("context", repositoryView.currentContext.uri);
        }, 250);
      }
    };

    WsApi.listen("/queue/context").then(null, null, function(res) {
      var context = angular.fromJson(res.body).payload.RepositoryViewContext;
      var contextUri = context.triple.subject.replace(repositoryView.rootUri, '');
      if (cache[contextUri] === undefined) {
        cache[contextUri] = new RepositoryViewContext({
          repositoryView: repositoryView,
          uri: contextUri,
          fetch: false
        });
      }
      angular.extend(cache[contextUri], context);
    });

    var user = UserService.getCurrentUser();

    WsApi.listen("/queue/transaction/" + user.uin).then(null, null, function(res) {
      var transactionDetails = angular.fromJson(res.body).payload.TransactionDetails;
      angular.extend(repositoryView.currentContext, {
        transactionDetails: transactionDetails
      });
    });

    return repositoryView;
  };
});
