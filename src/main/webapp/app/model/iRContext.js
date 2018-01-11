cap.model("IRContext", function($q, WsApi, HttpMethodVerbs) {
  return function IRContext() {

    var irContext = this;

    var children = {};

    var fetchContext = function (contextUri) {
      return WsApi.fetch(irContext.getMapping().load, {
        method: HttpMethodVerbs.GET,
        pathValues: {
          type: irContext.ir.type,
          irid: irContext.ir.id
        },
        query: {
          contextUri: contextUri
        }
      });
    };

    irContext.before(function() {
      var defer = $q.defer();
      if(irContext.fetch) {
        fetchContext(irContext.uri).then(function(res) {
          angular.extend(irContext, angular.fromJson(res.body).payload.IRContext, {
            fetch: false
          });
          irContext.ir.cacheContext(irContext);
          defer.resolve(irContext);
        });
      } else {
        defer.resolve(irContext);
      }
      return defer.promise;
    });

    irContext.getChildContext = function(triple) {
      if(!children[triple.object]) {
        children[triple.object] = new IRContext({
          fetch: false
        });
        var cachedContext = irContext.ir.getCachedContext(triple.object);
        if(cachedContext) {
          angular.extend(children[triple.object], cachedContext);
        } else {
          fetchContext(triple.object).then(function(res) {
            angular.extend(children[triple.object], angular.fromJson(res.body).payload.IRContext, {
              ir: irContext.ir,
              uri: triple.object
            });
            irContext.ir.cacheContext(children[triple.object]);
          });
        }
      }
      return children[triple.object];
    };

    irContext.getCachedChildContext = function(contextUri) {
      return children[contextUri];
    };

    irContext.createContainer = function(createForm) {
      var createPromise = WsApi.fetch(irContext.getMapping().container, {
        method: HttpMethodVerbs.POST,
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: {
          contextUri: irContext.uri
        },
        data: {
          name: createForm.name
        }
      });

      createPromise.then(function(res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
      });

      return createPromise;
    };

    irContext.removeContainers = function(containerTriples) {

      var promises = [];

      angular.forEach(containerTriples, function(containerTriple) {
        var removePromise = WsApi.fetch(irContext.getMapping().container, {
          method: HttpMethodVerbs.DELETE,
          pathValues: {
            irid: irContext.ir.id,
            type: irContext.ir.type
          },
          query: {
            containerUri: containerTriple.subject
          }
        });

        removePromise.then(function(res) {
          var children = irContext.children;
          for(var i in children) {
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

    irContext.removeResources = irContext.removeContainers;

    irContext.createResource = function(createForm) {

      var formData = new FormData();
      formData.append("file", createForm.file, createForm.file.name);

      var createPromise = WsApi.fetch(irContext.getMapping().resource, {
        method: HttpMethodVerbs.POST,
        headers: {
          "Content-Type": undefined
        },
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: {
          contextUri: irContext.uri
        },
        data: formData
      });

      createPromise.then(function(res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
      });

      return createPromise;
    };

    irContext.createMetadata = function(metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function(metadataTriple) {
        var createPromise = WsApi.fetch(irContext.getMapping().metadata, {
          method: HttpMethodVerbs.POST,
          pathValues: {
            irid: irContext.ir.id,
            type: irContext.ir.type
          },
          data: metadataTriple
        });

        createPromise.then(function(res) {
          angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
        });

        promises.push(createPromise);
      });

      var allRemovePromses = $q.all(promises);

      return allRemovePromses;
    };

    irContext.advancedUpdate = function(updateForm) {
      var updatePromise = WsApi.fetch(irContext.getMapping().metadata, {
        method: HttpMethodVerbs.PATCH,
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: {
          contextUri: irContext.uri
        },
        data: updateForm.sparql
      });

      updatePromise.then(function(res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
      });

      return updatePromise;
    };

    return this;

  };
});
