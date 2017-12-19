cap.repo("IRRepo", function($q, WsApi) {
  var iRRepo = this;

  this.scaffold = {
    name: "",
    uri: "",
    username: "",
    password: ""
  }

  iRRepo.testPing = function(ir) {
    return WsApi.fetch(iRRepo.mapping.testPing, {
      data: ir
    });
  }

  iRRepo.testAuth = function(ir) {
    return WsApi.fetch(iRRepo.mapping.testAuth, {
      data: ir
    });
  }

  iRRepo.testContent = function(ir) {
    return WsApi.fetch(iRRepo.mapping.testContent, {
      data: ir
    });
  }
  
  return iRRepo;
});