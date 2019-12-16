var dataUserRepo1 = [
  dataUser1,
  dataUser2,
  dataUser3
];

var dataUserRepo2 = [
  dataUser3,
  dataUser2,
  dataUser1
];

var dataUserRepo3 = [
  dataUser1,
  dataUser3,
  dataUser2
];

angular.module("mock.userRepo", []).service("UserRepo", function($q) {
  var repo = mockRepo("UserRepo", $q, mockUser, dataUserRepo1);

  repo.scaffold = {
    anonymous: false,
    email: "",
    exp: "",
    firstName: "",
    lastName: "",
    netId: "",
    repositoryViews: [],
    role: "",
    uin: ""
  };

  repo.getCurators = function () {
    var users = repo.getAll();
    var curators = [];

    for (var i in repo.mockedList) {
      if (repo.mockedList[i].role === "ROLE_CURATOR") {
        curators.push(repo.mockedList[i]);
      }
    }

    return curators;
  };

  return repo;
});
