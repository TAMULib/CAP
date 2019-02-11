cap.repo("RVRepo", function($q, WsApi, api, HttpMethodVerbs) {
  var rvRepo = this;

  rvRepo.scaffold = {
    name: "Labs Fedora",
    rootUri: "https://api-dev.library.tamu.edu/fcrepo/rest/",
    username: "fedoraAdmin",
    password: "secret3",
    type: "",
    schemas: []
  };

  rvRepo.getTypes = function(types) {
    var typesPromise = WsApi.fetch(rvRepo.mapping.getTypes);
    typesPromise.then(function(res) {
      angular.extend(types, angular.fromJson(res.body).payload['ArrayList<HashMap>']);
    });
    return typesPromise;
  };

  rvRepo.findByName = function(name) {
    var rvs = rvRepo.getAll();
    for(var i in rvs) {
      var rv = rvs[i];
      if(rv.name === name) {
        return rv;
      }
    }
  };

  rvRepo.verifyPing = function(rv) {
    return WsApi.fetch(api.VerifyRVSettings.verifyPing, {
      pathValues: {
        type: rv.type
      },
      data: rv
    });
  };

  rvRepo.verifyAuth = function(rv) {
    return WsApi.fetch(api.VerifyRVSettings.verifyAuth, {
      pathValues: {
        type: rv.type
      },
      data: rv
    });
  };

  rvRepo.verifyContent = function(rv) {
    return WsApi.fetch(api.VerifyRVSettings.verifyContent, {
      pathValues: {
        type: rv.type
      },
      data: rv
    });
  };

  return rvRepo;
});
