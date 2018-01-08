cap.model("IRContext", function($q, WsApi, HttpMethodVerbs) {
  return function IRContext() {

    var irContext = this;

    irContext.ir = undefined;
    irContext.uri = undefined;

    irContext.before(function() {

      var loadPromise = WsApi.fetch(irContext.getMapping().load, {
        method: HttpMethodVerbs.GET,
        pathValues: {
          type: irContext.ir.type,
          irid: irContext.ir.id
        },
        query: {
          contextUri: irContext.uri
        }
      });

      loadPromise.then(function(res) {
        angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
      });

      return loadPromise;

    });

    var children = {};

    irContext.getChildContext = function(triple) {

      if(!children[triple.object]) {

        children[triple.object] = {};

        var loadPromise = WsApi.fetch(irContext.getMapping().load, {
          method: HttpMethodVerbs.GET,
          pathValues: {
            type: irContext.ir.type,
            irid: irContext.ir.id
          },
          query: {
            contextUri: triple.object
          }
        });

        loadPromise.then(function(res) {
          angular.extend(children[triple.object], angular.fromJson(res.body).payload.IRContext);
        });

      }

      return children[triple.object];
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

    irContext.createResource = function(createForm) {

      var formData = new FormData();
      formData.append("file", createForm.file, createForm.file.name);      

      console.log(Array.from(formData.entries()));

      var createPromise = WsApi.fetch(irContext.getMapping().resource, {
        method: HttpMethodVerbs.POST,
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
        console.log(res);
        // angular.extend(irContext, angular.fromJson(res.body).payload.IRContext);
      });

      return createPromise;
    };

    return this;

  };
});
