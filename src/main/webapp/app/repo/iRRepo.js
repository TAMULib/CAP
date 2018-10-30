cap.repo("IRRepo", function($q, WsApi, api, HttpMethodVerbs) {
  var iRRepo = this;

  iRRepo.scaffold = {
    name: "Labs Fedora",
    rootUri: "https://api-dev.library.tamu.edu/fcrepo/rest/",
    username: "fedoraAdmin",
    password: "secret3",
    type: "",
    schemas: []
  };

  iRRepo.getTypes = function(types) {
    var typesPromise = WsApi.fetch(iRRepo.mapping.getTypes);
    typesPromise.then(function(res) {
      angular.extend(types, angular.fromJson(res.body).payload['ArrayList<HashMap>']);
    });
    return typesPromise;
  };

  iRRepo.findByName = function(name) {
    var irs = iRRepo.getAll();
    for(var i in irs) {
      var ir = irs[i];
      if(ir.name === name) {
        return ir;
      }
    }
  };

  iRRepo.verifyPing = function(ir) {
    return WsApi.fetch(api.VerifyIRSettings.verifyPing, {
      pathValues: {
        type: ir.type
      },
      data: ir
    });
  };

  iRRepo.verifyAuth = function(ir) {
    return WsApi.fetch(api.VerifyIRSettings.verifyAuth, {
      pathValues: {
        type: ir.type
      },
      data: ir
    });
  };

  iRRepo.verifyContent = function(ir) {
    return WsApi.fetch(api.VerifyIRSettings.verifyContent, {
      pathValues: {
        type: ir.type
      },
      data: ir
    });
  };

  return iRRepo;
});
