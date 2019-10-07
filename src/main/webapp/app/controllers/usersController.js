cap.controller('UsersController', function ($controller, $location, $injector, $scope, $route, UserService) {

    angular.extend(this, $controller('AbstractController', {$scope: $scope}));

    $scope.user = UserService.getCurrentUser();

    UserService.userReady().then(function() {

        $scope.assignableRoles = function(userRole) {
            if ($scope.isAdmin()) {
                return ['ROLE_ADMIN','ROLE_CURATOR','ROLE_USER'];
            }
            else if ($scope.isManager()) {
                if (userRole == 'ROLE_ADMIN') {
                    return ['ROLE_ADMIN'];
                }
                return ['ROLE_CURATOR','ROLE_USER'];
            }
            else {
                return [userRole];
            }
        };

        $scope.canDelete = function(user) {
            var canDelete = false;
            if ((user.uin != $scope.user.uin) && $scope.isAdmin()) {
                canDelete = true;
            }
            return canDelete;
        };

        if ($scope.isAdmin()) {

            var UserRepo = $injector.get("UserRepo");

            $scope.userUpdated = {};

            $scope.users = UserRepo.getAll();

            $scope.updateRole = function(user) {

                angular.extend($scope.userUpdated, user);

                user.save();

                if ($scope.user.username == user.username) {
                    if (user.role == 'ROLE_USER') {
                        $location.path('/myview');
                    }
                    else {}
                }
            };

            $scope.delete = function(user) {
                user.delete();
            };

            UserRepo.listen(function(response) {
                $scope.userUpdated = {};
                $route.reload();
            });

        }

    });

});
