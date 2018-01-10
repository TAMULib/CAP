cap.repo("IRRepo", function($q, WsApi, api, HttpMethodVerbs) {
  var iRRepo = this;

  iRRepo.scaffold = {
    name: "Test",
    rootUri: "http://machuff.tamu.edu:8080/fcrepo/rest/",
    username: "fedoraAdmin",
    password: "secret3",
    type: ""
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

  iRRepo.testPing = function(ir) {
    return WsApi.fetch(api.TestIRSettings.testPing, {
      pathValues: {
        type: ir.type
      },
      data: ir
    });
  };

  iRRepo.testAuth = function(ir) {
    return WsApi.fetch(api.TestIRSettings.testAuth, {
      pathValues: {
        type: ir.type
      },
      data: ir
    });
  };

  iRRepo.testContent = function(ir) {
    return WsApi.fetch(api.TestIRSettings.testContent, {
      pathValues: {
        type: ir.type
      },
      data: ir
    });
  };

  return iRRepo;
});
