cap.repo("UserRepo", function UserRepo() {
  var userRepo = this;

  userRepo.getCurators = function () {
    var users = userRepo.getAll();
    var curators = [];
    userRepo.ready().then(function () {
      for (var i in users) {
        var user = users[i];
        if (user.role === "ROLE_CURATOR") {
          curators.push(user);
        }
      }
    });
    return curators;
  };

  return userRepo;

});