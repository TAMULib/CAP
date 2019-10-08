cap.controller("RepositoryViewManagementController", function($controller, $scope, $q, ApiResponseActions, NgTableParams, RepositoryView, RepositoryViewRepo, SchemaRepo, UserRepo) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  angular.extend(this, $controller('AbstractAppController', {
      $scope: $scope
  }));

  var startupQueue = [];

  var resetVerification = function(force) {
    if (force || $scope.verificationResults.status === 'SUCCESS') {
      $scope.verificationResults = {};
    }
  };

  $scope.repositoryViews = RepositoryViewRepo.getAll();

  $scope.repositoryView = {};

  if ($scope.isAdmin()) {

    $scope.repositoryViewTypes = [];

    $scope.curators = UserRepo.getCurators();

    $scope.submitting = false;

    RepositoryViewRepo.getTypes($scope.repositoryViewTypes).then(function() {
      $scope.repositoryViewToCreate = RepositoryViewRepo.getScaffold({
        type: $scope.repositoryViewTypes[0].value
      });

      angular.forEach($scope.repositoryViews, function (repositoryView) {
        repositoryView.clearCache();
      });

      $scope.$watch('repositoryView',function() {
        resetVerification();
      },true);

      $scope.disableVerify = function(activeRepositoryView) {
        if (angular.isDefined(activeRepositoryView)) {
          var typeIsVerifying = false;
          for (var i in $scope.repositoryViewTypes) {
            var type = $scope.repositoryViewTypes[i];
            if (type.value === activeRepositoryView.type) {
              typeIsVerifying = type.verifying===true;
            }
          }
          return activeRepositoryView.rootUri && !typeIsVerifying;
        }
        return false;
      };
    });

    resetVerification(true);

    $scope.repositoryViewForms = {
      validations: RepositoryViewRepo.getValidations(),
      getResults: RepositoryViewRepo.getValidationResults
    };

    $scope.resetRepositoryViewForms = function() {
      RepositoryViewRepo.clearValidationResults();
      for (var key in $scope.repositoryViewForms) {
        if ($scope.repositoryViewForms[key] !== undefined && !$scope.repositoryViewForms[key].$pristine && $scope.repositoryViewForms[key].$setPristine) {
          $scope.repositoryViewForms[key].$setPristine();
        }
      }
      resetVerification(true);
      delete $scope.repositoryView;
      $scope.closeModal();
    };

    $scope.resetRepositoryViewForms();

    $scope.createRepositoryView = function() {
      $scope.schemas = SchemaRepo.getAll();
      $scope.repositoryView = new RepositoryView(RepositoryViewRepo.getScaffold());
      $scope.openModal("#repositoryViewCreateModal");
    };

    $scope.onCreateRepositoryView = function() {
      $scope.submitting = true;
      RepositoryViewRepo.create($scope.repositoryView).then(function(res) {
        if (angular.fromJson(res.body).meta.status === "SUCCESS") {
          $scope.resetRepositoryViewForms();
        }
        $scope.submitting = false;
      });
    };

    $scope.onCancelCreateRepositoryView = function() {
      $scope.resetRepositoryViewForms();
    };

    $scope.editRepositoryView = function(repositoryView) {
      $scope.schemas = SchemaRepo.getAll();
      $scope.repositoryView = new RepositoryView(angular.copy(repositoryView));
      $scope.openModal('#repositoryViewEditModal');
    };

    $scope.onEditRepositoryView = function() {
      $scope.submitting = true;
      $scope.repositoryView.dirty(true);
      $scope.repositoryView.save().then(function(res) {
        if (angular.fromJson(res.body).meta.status === "SUCCESS") {
          $scope.resetRepositoryViewForms();
        }
        $scope.submitting = false;
      });
    };

    $scope.onCancelEditRepositoryView = function() {
      $scope.resetRepositoryViewForms();
    };

    $scope.confirmDeleteRepositoryView = function(repositoryView) {
      $scope.repositoryView = new RepositoryView(angular.copy(repositoryView));
      $scope.openModal('#repositoryViewDeleteModal');
    };

    $scope.onCancelDeleteRepositoryView = function() {
      $scope.resetRepositoryViewForms();
    };

    $scope.onDeleteRepositoryView = function() {
      $scope.submitting = true;
      $scope.repositoryView.delete().then(function(res) {
        if (angular.fromJson(res.body).meta.status === "SUCCESS") {
          $scope.resetRepositoryViewForms();
        }
        $scope.submitting = false;
      });
    };

    startupQueue = [RepositoryViewRepo.ready(),SchemaRepo.ready()];
  } else if ($scope.isCurator()) {
    startupQueue = [RepositoryViewRepo.ready(),SchemaRepo.ready()];
  }

  $scope.showSchemas = function(schemas) {
    $scope.schemasToShow = schemas;
    $scope.openModal("#showSchemasModal");
  };

  $q.all(startupQueue).then(function() {
    $scope.setTable = function () {
      $scope.tableParams = new NgTableParams({
        count: $scope.repositoryViews.length,
        sorting: {
          name: 'asc'
        }
    }, {
      counts: [],
      total: 0,
      getData: function (params) {
        return $scope.repositoryViews;
        }
      });
    };
    $scope.setTable();
  });

  RepositoryViewRepo.listen([ApiResponseActions.CREATE, ApiResponseActions.DELETE], function (arg) {
    $scope.setTable();
  });

});
