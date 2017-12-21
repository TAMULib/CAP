cap.repo("IRRepo", function($q, WsApi) {
  var iRRepo = this;

  this.scaffold = {
    name: "",
    uri: "",
    username: "",
    password: "",
    type: ""
  };

  iRRepo.getTypes = function(types) {
    var typesPromise = WsApi.fetch(iRRepo.mapping.getTypes);
    typesPromise.then(function(res) {
      angular.extend(types, angular.fromJson(res.body).payload['ArrayList<HashMap>']);
    });
    return typesPromise;
  };

  iRRepo.testPing = function(ir) {
    return WsApi.fetch(iRRepo.mapping.testPing, {
      data: ir
    });
  };

  iRRepo.testAuth = function(ir) {
    return WsApi.fetch(iRRepo.mapping.testAuth, {
      data: ir
    });
  };

  iRRepo.testContent = function(ir) {
    return WsApi.fetch(iRRepo.mapping.testContent, {
      data: ir
    });
  };
  
  return iRRepo;
});