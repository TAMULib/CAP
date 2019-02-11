cap.model("RVContext", function ($q, $filter, $interval, $location, $routeParams, $cookies, WsApi, UserService, HttpMethodVerbs, StorageService) {
  return function RVContext() {

    var rvContext = this;

    var children = {};

    var fetchContext = function (contextUri) {
      return rvContext.rv.performRequest(rvContext.getMapping().load, {
        method: HttpMethodVerbs.GET,
        query: {
          contextUri: contextUri
        }
      });
    };

    var removeQuotes = function (value) {
      return $filter('removeQuotes')(value);
    };

    rvContext.before(function () {
      var defer = $q.defer();
      if (rvContext.fetch) {
        fetchContext(rvContext.uri).then(function (res) {
          angular.extend(rvContext, angular.fromJson(res.body).payload.RVContext, {
            fetch: false
          });
          rvContext.rv.cacheContext(rvContext);
          defer.resolve(rvContext);

          rvContext.ready().then(function() {
            if(rvContext.rv.getTransaction().active) {
              rvContext.rv.startTransactionTimer();
            }
          });

        });
      } else {
        defer.resolve(rvContext);
      }
      return defer.promise;
    });

    rvContext.reloadContext = function() {
      var reloadPromise = fetchContext(rvContext.uri);
      reloadPromise.then(function (res) {
        angular.extend(rvContext, angular.fromJson(res.body).payload.RVContext, {
          fetch: false
        });
      });
      return reloadPromise;
    }; 

    rvContext.getChildContext = function (triple) {
      if (!children[triple.object]) {
        children[triple.object] = new RVContext({
          fetch: false
        });
        var cachedContext = rvContext.rv.getCachedContext(triple.object);
        if (cachedContext) {
          angular.extend(children[triple.object], cachedContext);
        } else {
          fetchContext(triple.object).then(function (res) {
            angular.extend(children[triple.object], angular.fromJson(res.body).payload.RVContext, {
              rv: rvContext.rv,
              uri: triple.object
            });
            rvContext.rv.cacheContext(children[triple.object]);
          });
        }
      }
      return children[triple.object];
    };

    rvContext.getCachedChildContext = function (contextUri) {
      return children[contextUri];
    };

    rvContext.createContainer = function (metadata) {

      var createPromise = rvContext.rv.performRequest(rvContext.getMapping().children, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: rvContext.uri
        },
        data: metadata
      });

      createPromise.then(function (res) {
        angular.extend(rvContext, angular.fromJson(res.body).payload.RVContext);
      });

      return createPromise;
    };

    rvContext.removeContainers = function (containerTriples) {

      var promises = [];

      angular.forEach(containerTriples, function (containerTriple) {
        var removePromise = rvContext.rv.performRequest(rvContext.getMapping().load, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: containerTriple.subject
          }
        });

        removePromise.then(function (res) {
          var children = rvContext.children;
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

    rvContext.removeResources = function (resourceTriples) {

      var promises = [];

      angular.forEach(resourceTriples, function (resourceTriple) {
        var removePromise = rvContext.rv.performRequest(rvContext.getMapping().resource, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: resourceTriple.subject
          }
        });

        removePromise.then(function (res) {
          var children = rvContext.children;
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

    rvContext.createResource = function (file) {

      var formData = new FormData();
      formData.append("file", file, file.name);
      
      var createPromise = rvContext.rv.performRequest(rvContext.getMapping().resource, {
        method: HttpMethodVerbs.POST,
        headers: {
          "Content-Type": undefined
        },
        query: {
          contextUri: rvContext.uri
        },
        data: formData
      });

      createPromise.then(function (res) {
        angular.extend(rvContext, angular.fromJson(res.body).payload.RVContext);
      });

      return createPromise;
    };

    rvContext.createMetadata = function (metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function (metadataTriple) {
        var createPromise = rvContext.rv.performRequest(rvContext.getMapping().metadata, {
          method: HttpMethodVerbs.POST,
          query: {
            contextUri: rvContext.uri
          },
          data: metadataTriple
        });

        createPromise.then(function (res) {
          angular.extend(rvContext, angular.fromJson(res.body).payload.RVContext);
        });

        promises.push(createPromise);
      });

      var allCreatePromses = $q.all(promises);

      return allCreatePromses;
    };

    rvContext.removeMetadata = function (metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function (metadataTriple) {

        var removePromise = rvContext.rv.performRequest(rvContext.getMapping().metadata, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: rvContext.uri
          },
          data: metadataTriple
        });

        removePromise.then(function (response) {
          var payload = angular.fromJson(response.body).payload;
          if (payload) {
            angular.extend(rvContext, payload.RVContext);
          }
        });

        promises.push(removePromise);
      });

      var allRemovePromses = $q.all(promises);

      return allRemovePromses;
    };

    rvContext.updateMetadatum = function (metadataTriple, newValue) {
      
      var updatePromise = rvContext.rv.performRequest(rvContext.getMapping().metadata, {
        method: HttpMethodVerbs.PUT,
        query: {
          contextUri: rvContext.uri,
          newValue: newValue
        },
        data: metadataTriple
      });
      
      return updatePromise;

    };

    rvContext.createVersion = function(form) {

      var versionPromise = rvContext.rv.performRequest(rvContext.getMapping().version, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: rvContext.uri
        },
        data: {
          name: form.name
        }
      });

      versionPromise.then(function(apiRes) {

        var newContext = angular.fromJson(apiRes.body).payload.RVContext;
        angular.extend(rvContext, newContext);

        if (form) {
          form.$setPristine();
          form.$setUntouched();
          form.name = "";
        }
      });

      return versionPromise;
    };

    rvContext.deleteVersion = function(versionContext) {
      return rvContext.rv.performRequest(rvContext.getMapping().version, {
        method: HttpMethodVerbs.DELETE,
        query: {
          contextUri: versionContext.uri
        }
      });
    };

    rvContext.revertVersion = function(context) {
      var revertVersionPromise = rvContext.rv.performRequest(rvContext.getMapping().version, {
        method: HttpMethodVerbs.PATCH,
        query: {
          contextUri: context.uri
        }
      });
      return revertVersionPromise;
    };

    rvContext.advancedUpdate = function (query) {
      var updatePromise = rvContext.rv.performRequest(rvContext.getMapping().advancedQuery, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: rvContext.uri
        },
        data: query
      });

      updatePromise.then(function (res) {
        angular.extend(rvContext, angular.fromJson(res.body).payload.RVContext);
      });

      return updatePromise;
    };

    var queryHelp = {};

    rvContext.getQueryHelp = function () {

      if(!queryHelp.message) {
        
        var updatePromise = rvContext.rv.performRequest(rvContext.getMapping().advancedQuery, {
          method: HttpMethodVerbs.GET,
          query: {
            contextUri: rvContext.uri
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
