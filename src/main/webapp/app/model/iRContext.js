cap.model("IRContext", function ($q, $filter, WsApi, HttpMethodVerbs) {
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

    var removeQuotes = function (value) {
      return $filter('removeQuotes')(value);
    };

    irContext.before(function () {
      var defer = $q.defer();
      if (irContext.fetch) {
        fetchContext(irContext.uri).then(function (res) {
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

    irContext.getChildContext = function (triple) {
      if (!children[triple.object]) {
        children[triple.object] = new IRContext({
          fetch: false
        });
        var cachedContext = irContext.ir.getCachedContext(triple.object);
        if (cachedContext) {
          angular.extend(children[triple.object], cachedContext);
        } else {
          fetchContext(triple.object).then(function (res) {
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

    irContext.getCachedChildContext = function (contextUri) {
      return children[contextUri];
    };

    irContext.createContainer = function (metadata) {

      var createPromise = WsApi.fetch(irContext.getMapping().children, {
        method: HttpMethodVerbs.POST,
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: {
          contextUri: irContext.uri
        },
        data: metadata
      });

      createPromise.then(function (res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
      });

      return createPromise;
    };

    irContext.removeContainers = function (containerTriples) {

      var promises = [];

      angular.forEach(containerTriples, function (containerTriple) {
        var removePromise = WsApi.fetch(irContext.getMapping().children, {
          method: HttpMethodVerbs.DELETE,
          pathValues: {
            irid: irContext.ir.id,
            type: irContext.ir.type
          },
          query: {
            containerUri: containerTriple.subject
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

    irContext.removeResources = irContext.removeContainers;

    irContext.createResource = function (file) {

      var formData = new FormData();
      formData.append("file", file, file.name);

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

      createPromise.then(function (res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
      });

      return createPromise;
    };

    irContext.createMetadata = function (metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function (metadataTriple) {
        var createPromise = WsApi.fetch(irContext.getMapping().metadata, {
          method: HttpMethodVerbs.POST,
          pathValues: {
            irid: irContext.ir.id,
            type: irContext.ir.type
          },
          data: metadataTriple
        });

        createPromise.then(function (res) {
          angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
        });

        promises.push(createPromise);
      });

      var allCreatePromses = $q.all(promises);

      return allCreatePromses;
    };

    irContext.removeMetadata = function (metadataTriples) {

      var promises = [];

      angular.forEach(metadataTriples, function (metadataTriple) {

        var removePromise = WsApi.fetch(irContext.getMapping().metadata, {
          method: HttpMethodVerbs.DELETE,
          pathValues: {
            irid: irContext.ir.id,
            type: irContext.ir.type
          },
          data: metadataTriple
        });

        removePromise.then(function (response) {
          var payload = angular.fromJson(response.body).payload;
          if (payload) {
            angular.extend(irContext, payload.IRContext);
          }
        });

        promises.push(removePromise);
      });

      var allRemovePromses = $q.all(promises);

      return allRemovePromses;
    };

    irContext.updateMetadatum = function (metadataTriple, newValue) {
      
      var updatePromise = WsApi.fetch(irContext.getMapping().metadata, {
        method: HttpMethodVerbs.PUT,
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: {
          newValue: newValue
        },
        data: metadataTriple
      });
      
      return updatePromise;

    };

    irContext.createVersion = function(form) {

      var versionPromise = WsApi.fetch(irContext.getMapping().version, {
        method: HttpMethodVerbs.POST,
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: {
          contextUri: irContext.uri
        },
        data: {
          name: form.name
        }
      });

      versionPromise.then(function() {
        if (form) {
          form.$setPristine();
          form.$setUntouched();
          form.name ="";
        }
      });

      return versionPromise;
    };

    irContext.deleteVersion = function(versionContext) {
      return WsApi.fetch(irContext.getMapping().version, {
        method: HttpMethodVerbs.DELETE,
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: {
          versionUri: versionContext.uri
        }
      });
    };

    irContext.revertVersion = function(context) {
      var revertVersionPromise = WsApi.fetch(irContext.getMapping().version, {
        method: HttpMethodVerbs.PATCH,
        pathValues: {
          irid: context.ir.id,
          type: context.ir.type
        },
        query: {
          contextUri: context.uri
        }
      });

      return revertVersionPromise;
    };

    irContext.fixityCheck = function () {
      var fixityPromise = WsApi.fetch(irContext.getMapping().resourceFixity, {
        method: HttpMethodVerbs.GET,
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: irContext.triple
      });

      return fixityPromise;
    };

    irContext.advancedUpdate = function (sparql) {
      var updatePromise = WsApi.fetch(irContext.getMapping().metadata, {
        method: HttpMethodVerbs.PATCH,
        pathValues: {
          irid: irContext.ir.id,
          type: irContext.ir.type
        },
        query: {
          contextUri: irContext.uri
        },
        data: sparql
      });

      updatePromise.then(function (res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
      });

      return updatePromise;
    };

    return this;

  };
});