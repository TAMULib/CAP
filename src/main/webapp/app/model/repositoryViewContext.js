cap.model("RepositoryViewContext", function ($q, $filter, $interval, $location, $routeParams, $cookies, WsApi, UserService, HttpMethodVerbs, StorageService) {
  return function RepositoryViewContext() {

    var repositoryViewContext = this;

    var children = {};

    var fetchContext = function (contextUri) {
      return repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().load, {
        method: HttpMethodVerbs.GET,
        query: {
          contextUri: contextUri
        }
      });
    };

    var removeQuotes = function (value) {
      return $filter('removeQuotes')(value);
    };

    repositoryViewContext.before(function () {
      var defer = $q.defer();
      if (repositoryViewContext.fetch) {
        fetchContext(repositoryViewContext.uri).then(function (res) {
          angular.extend(repositoryViewContext, angular.fromJson(res.body).payload.RepositoryViewContext, {
            fetch: false
          });
          repositoryViewContext.repositoryView.cacheContext(repositoryViewContext);
          defer.resolve(repositoryViewContext);

          repositoryViewContext.ready().then(function() {
            if(repositoryViewContext.repositoryView.getTransaction().active) {
              repositoryViewContext.repositoryView.startTransactionTimer();
            }
          });

        });
      } else {
        defer.resolve(repositoryViewContext);
      }
      return defer.promise;
    });

    repositoryViewContext.reloadContext = function() {
      var reloadPromise = fetchContext(repositoryViewContext.uri);
      reloadPromise.then(function (res) {
        angular.extend(repositoryViewContext, angular.fromJson(res.body).payload.RepositoryViewContext, {
          fetch: false
        });
      });
      return reloadPromise;
    }; 

    repositoryViewContext.getChildContext = function (triple) {
      if (!children[triple.object]) {
        children[triple.object] = new RepositoryViewContext({
          fetch: false
        });
        var cachedContext = repositoryViewContext.repositoryView.getCachedContext(triple.object);
        if (cachedContext) {
          angular.extend(children[triple.object], cachedContext);
        } else {
          fetchContext(triple.object).then(function (res) {
            angular.extend(children[triple.object], angular.fromJson(res.body).payload.RepositoryViewContext, {
              repositoryView: repositoryViewContext.repositoryView,
              uri: triple.object
            });
            repositoryViewContext.repositoryView.cacheContext(children[triple.object]);
          });
        }
      }
      return children[triple.object];
    };

    repositoryViewContext.getCachedChildContext = function (contextUri) {
      return children[contextUri];
    };

    repositoryViewContext.createContainer = function (metadata) {

      var createPromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().children, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: repositoryViewContext.uri
        },
        data: metadata
      });

      createPromise.then(function (res) {
        angular.extend(repositoryViewContext, angular.fromJson(res.body).payload.RepositoryViewContext);
      });

      return createPromise;
    };

    repositoryViewContext.removeContainers = function (containerTriples) {

      var promises = [];

      angular.forEach(containerTriples, function (containerTriple) {
        var removePromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().load, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: containerTriple.subject
          }
        });

        removePromise.then(function (res) {
          var children = repositoryViewContext.children;
          for (var i in children) {
            if (children.hasOwnProperty(i)) {
              var child = children[i];
              if (child.triple.object === containerTriple.subject) {
                children.splice(i, 1);
                break;
              }
            }
          }
        });

        promises.push(removePromise);
      });

      var allRemovePromses = $q.all(promises);

      return allRemovePromses;
    };

    repositoryViewContext.removeResources = function (resourceTriples) {

      var promises = [];

      angular.forEach(resourceTriples, function (resourceTriple) {
        var removePromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().resource, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: resourceTriple.subject
          }
        });

        removePromise.then(function (res) {
          var children = repositoryViewContext.children;
          for (var i in children) {
            if (children.hasOwnProperty(i)) {
              var child = children[i];
              if (child.triple.object === resourceTriple.subject) {
                children.splice(i, 1);
                break;
              }
            }
          }
        });

        promises.push(removePromise);
      });

      var allRemovePromses = $q.all(promises);

      return allRemovePromses;
    };

    repositoryViewContext.createResource = function (file) {

      var formData = new FormData();
      formData.append("file", file, file.name);
      
      var createPromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().resource, {
        method: HttpMethodVerbs.POST,
        headers: {
          "Content-Type": undefined
        },
        query: {
          contextUri: repositoryViewContext.uri
        },
        data: formData
      });

      createPromise.then(function (res) {
        angular.extend(repositoryViewContext, angular.fromJson(res.body).payload.RepositoryViewContext);
      });

      return createPromise;
    };

    repositoryViewContext.createMetadata = function (metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function (metadataTriple) {
        var createPromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().metadata, {
          method: HttpMethodVerbs.POST,
          query: {
            contextUri: repositoryViewContext.uri
          },
          data: metadataTriple
        });

        createPromise.then(function (res) {
          angular.extend(repositoryViewContext, angular.fromJson(res.body).payload.RepositoryViewContext);
        });

        promises.push(createPromise);
      });

      var allCreatePromses = $q.all(promises);

      return allCreatePromses;
    };

    repositoryViewContext.removeMetadata = function (metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function (metadataTriple) {

        var removePromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().metadata, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: repositoryViewContext.uri
          },
          data: metadataTriple
        });

        removePromise.then(function (response) {
          var payload = angular.fromJson(response.body).payload;
          if (payload) {
            angular.extend(repositoryViewContext, payload.RepositoryViewContext);
          }
        });

        promises.push(removePromise);
      });

      var allRemovePromses = $q.all(promises);

      return allRemovePromses;
    };

    repositoryViewContext.updateMetadatum = function (metadataTriple, newValue) {
      
      var updatePromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().metadata, {
        method: HttpMethodVerbs.PUT,
        query: {
          contextUri: repositoryViewContext.uri,
          newValue: newValue
        },
        data: metadataTriple
      });
      
      return updatePromise;

    };

    repositoryViewContext.createVersion = function(form) {

      var versionPromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().version, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: repositoryViewContext.uri
        },
        data: {
          name: form.name
        }
      });

      versionPromise.then(function(apiRes) {

        var newContext = angular.fromJson(apiRes.body).payload.RepositoryViewContext;
        angular.extend(repositoryViewContext, newContext);

        if (form) {
          form.$setPristine();
          form.$setUntouched();
          form.name = "";
        }
      });

      return versionPromise;
    };

    repositoryViewContext.deleteVersion = function(versionContext) {
      return repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().version, {
        method: HttpMethodVerbs.DELETE,
        query: {
          contextUri: versionContext.uri
        }
      });
    };

    repositoryViewContext.revertVersion = function(context) {
      var revertVersionPromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().version, {
        method: HttpMethodVerbs.PATCH,
        query: {
          contextUri: context.uri
        }
      });
      return revertVersionPromise;
    };

    repositoryViewContext.advancedUpdate = function (query) {
      var updatePromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().advancedQuery, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: repositoryViewContext.uri
        },
        data: query
      });

      updatePromise.then(function (res) {
        angular.extend(repositoryViewContext, angular.fromJson(res.body).payload.RepositoryViewContext);
      });

      return updatePromise;
    };

    var queryHelp = {};

    repositoryViewContext.getQueryHelp = function () {

      if(!queryHelp.message) {
        
        var updatePromise = repositoryViewContext.repositoryView.performRequest(repositoryViewContext.getMapping().advancedQuery, {
          method: HttpMethodVerbs.GET,
          query: {
            contextUri: repositoryViewContext.uri
          }
        });
  
        updatePromise.then(function (res) {
          queryHelp.query = angular.fromJson(res.body).payload.String;
        });
      }

      return queryHelp;
    };

    return this;

  };
});
