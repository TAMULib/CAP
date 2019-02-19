cap.repo("UserRepo", function UserRepo() {
  userRepo = this;

  userRepo.getCurators = function() {
    var users = userRepo.getAll();
    var curators = [];
    for(var i in users) {
      console.log("test");
      var user = users[i];
      console.log(user.role);
      if(user.role === "ROLE_CURATOR") {
        curators.push(user);
      }
    }
    return curators;
  };

  return userRepo;

});