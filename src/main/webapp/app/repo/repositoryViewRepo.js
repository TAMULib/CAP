cap.repo("RepositoryViewRepo", function (WsApi, api) {
  var repositoryViewRepo = this;

  repositoryViewRepo.scaffold = {
    name: "Labs Fedora",
    rootUri: "https://api-dev.library.tamu.edu/fcrepo/rest/",
    username: "fedoraAdmin",
    password: "secret3",
    type: "",
    schemas: [],
    curators: []
  };

  repositoryViewRepo.getTypes = function (types) {
    var typesPromise = WsApi.fetch(repositoryViewRepo.mapping.getTypes);
    typesPromise.then(function (res) {
      angular.extend(types, angular.fromJson(res.body).payload['ArrayList<HashMap>']);
    });
    return typesPromise;
  };

  repositoryViewRepo.findByName = function (name) {
    var repositoryViews = repositoryViewRepo.getAll();
    for (var i in repositoryViews) {
      var repositoryView = repositoryViews[i];
      if (repositoryView.name === name) {
        return repositoryView;
      }
    }
  };

  repositoryViewRepo.verifyPing = function (repositoryView) {
    return WsApi.fetch(api.VerifyRepositoryViewSettings.verifyPing, {
      pathValues: {
        type: repositoryView.type
      },
      data: repositoryView
    });
  };

  repositoryViewRepo.verifyAuth = function (repositoryView) {
    return WsApi.fetch(api.VerifyRepositoryViewSettings.verifyAuth, {
      pathValues: {
        type: repositoryView.type
      },
      data: repositoryView
    });
  };

  repositoryViewRepo.verifyContent = function (repositoryView) {
    return WsApi.fetch(api.VerifyRepositoryViewSettings.verifyContent, {
      pathValues: {
        type: repositoryView.type
      },
      data: repositoryView
    });
  };

  return repositoryViewRepo;
});
