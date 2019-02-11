cap.model("RVContext", function ($q, $filter, $interval, $location, $routeParams, $cookies, WsApi, UserService, HttpMethodVerbs, StorageService) {
  return function RVContext() {

    var irContext = this;

    var children = {};

    var fetchContext = function (contextUri) {
      return irContext.rv.performRequest(irContext.getMapping().load, {
        method: HttpMethodVerbs.GET,
        query: {
          contextUri: contextUri
        }
      });
    };

    var removeQuotes = function (value) {
      return $filter('removeQuotes')(value);
    };

    irContext.before(function () {
      var defer = $q.defer();
      if (irContext.fetch) {
        fetchContext(irContext.uri).then(function (res) {
          angular.extend(irContext, angular.fromJson(res.body).payload.RVContext, {
            fetch: false
          });
          irContext.rv.cacheContext(irContext);
          defer.resolve(irContext);

          irContext.ready().then(function() {
            if(irContext.rv.getTransaction().active) {
              irContext.rv.startTransactionTimer();
            }
          });

        });
      } else {
        defer.resolve(irContext);
      }
      return defer.promise;
    });

    irContext.reloadContext = function() {
      var reloadPromise = fetchContext(irContext.uri);
      reloadPromise.then(function (res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.RVContext, {
          fetch: false
        });
      });
      return reloadPromise;
    }; 

    irContext.getChildContext = function (triple) {
      if (!children[triple.object]) {
        children[triple.object] = new RVContext({
          fetch: false
        });
        var cachedContext = irContext.rv.getCachedContext(triple.object);
        if (cachedContext) {
          angular.extend(children[triple.object], cachedContext);
        } else {
          fetchContext(triple.object).then(function (res) {
            angular.extend(children[triple.object], angular.fromJson(res.body).payload.RVContext, {
              rv: irContext.ir,
              uri: triple.object
            });
            irContext.rv.cacheContext(children[triple.object]);
          });
        }
      }
      return children[triple.object];
    };

    irContext.getCachedChildContext = function (contextUri) {
      return children[contextUri];
    };

    irContext.createContainer = function (metadata) {

      var createPromise = irContext.rv.performRequest(irContext.getMapping().children, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: irContext.uri
        },
        data: metadata
      });

      createPromise.then(function (res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.RVContext);
      });

      return createPromise;
    };

    irContext.removeContainers = function (containerTriples) {

      var promises = [];

      angular.forEach(containerTriples, function (containerTriple) {
        var removePromise = irContext.rv.performRequest(irContext.getMapping().load, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: containerTriple.subject
          }
        });

        removePromise.then(function (res) {
          var children = irContext.children;
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

    irContext.removeResources = function (resourceTriples) {

      var promises = [];

      angular.forEach(resourceTriples, function (resourceTriple) {
        var removePromise = irContext.rv.performRequest(irContext.getMapping().resource, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: resourceTriple.subject
          }
        });

        removePromise.then(function (res) {
          var children = irContext.children;
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

    irContext.createResource = function (file) {

      var formData = new FormData();
      formData.append("file", file, file.name);
      
      var createPromise = irContext.rv.performRequest(irContext.getMapping().resource, {
        method: HttpMethodVerbs.POST,
        headers: {
          "Content-Type": undefined
        },
        query: {
          contextUri: irContext.uri
        },
        data: formData
      });

      createPromise.then(function (res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.RVContext);
      });

      return createPromise;
    };

    irContext.createMetadata = function (metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function (metadataTriple) {
        var createPromise = irContext.rv.performRequest(irContext.getMapping().metadata, {
          method: HttpMethodVerbs.POST,
          query: {
            contextUri: irContext.uri
          },
          data: metadataTriple
        });

        createPromise.then(function (res) {
          angular.extend(irContext, angular.fromJson(res.body).payload.RVContext);
        });

        promises.push(createPromise);
      });

      var allCreatePromses = $q.all(promises);

      return allCreatePromses;
    };

    irContext.removeMetadata = function (metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function (metadataTriple) {

        var removePromise = irContext.rv.performRequest(irContext.getMapping().metadata, {
          method: HttpMethodVerbs.DELETE,
          query: {
            contextUri: irContext.uri
          },
          data: metadataTriple
        });

        removePromise.then(function (response) {
          var payload = angular.fromJson(response.body).payload;
          if (payload) {
            angular.extend(irContext, payload.RVContext);
          }
        });

        promises.push(removePromise);
      });

      var allRemovePromses = $q.all(promises);

      return allRemovePromses;
    };

    irContext.updateMetadatum = function (metadataTriple, newValue) {
      
      var updatePromise = irContext.rv.performRequest(irContext.getMapping().metadata, {
        method: HttpMethodVerbs.PUT,
        query: {
          contextUri: irContext.uri,
          newValue: newValue
        },
        data: metadataTriple
      });
      
      return updatePromise;

    };

    irContext.createVersion = function(form) {

      var versionPromise = irContext.rv.performRequest(irContext.getMapping().version, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: irContext.uri
        },
        data: {
          name: form.name
        }
      });

      versionPromise.then(function(apiRes) {

        var newContext = angular.fromJson(apiRes.body).payload.RVContext;
        angular.extend(irContext, newContext);

        if (form) {
          form.$setPristine();
          form.$setUntouched();
          form.name = "";
        }
      });

      return versionPromise;
    };

    irContext.deleteVersion = function(versionContext) {
      return irContext.rv.performRequest(irContext.getMapping().version, {
        method: HttpMethodVerbs.DELETE,
        query: {
          contextUri: versionContext.uri
        }
      });
    };

    irContext.revertVersion = function(context) {
      var revertVersionPromise = irContext.rv.performRequest(irContext.getMapping().version, {
        method: HttpMethodVerbs.PATCH,
        query: {
          contextUri: context.uri
        }
      });
      return revertVersionPromise;
    };

    irContext.advancedUpdate = function (query) {
      var updatePromise = irContext.rv.performRequest(irContext.getMapping().advancedQuery, {
        method: HttpMethodVerbs.POST,
        query: {
          contextUri: irContext.uri
        },
        data: query
      });

      updatePromise.then(function (res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.RVContext);
      });

      return updatePromise;
    };

    var queryHelp = {};

    irContext.getQueryHelp = function () {

      if(!queryHelp.message) {
        
        var updatePromise = irContext.rv.performRequest(irContext.getMapping().advancedQuery, {
          method: HttpMethodVerbs.GET,
          query: {
            contextUri: irContext.uri
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
