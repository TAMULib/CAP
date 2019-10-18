cap.controller("SchemaManagementController", function ($controller, $scope, $timeout, Schema, SchemaRepo, NgTableParams) {

  angular.extend(this, $controller('CoreAdminController', {
    $scope: $scope
  }));

  $scope.schemas = SchemaRepo.getAll();

  $scope.propertiesStatus = {
    loading: false,
    loaded: false
  };

  $scope.schemaForms = {
    validations: SchemaRepo.getValidations(),
    getResults: SchemaRepo.getValidationResults
  };

  $scope.submitting = false;

  $scope.resetSchemaPropertyList = true;

  $scope.resetSchemaForms = function () {
    SchemaRepo.clearValidationResults();
    for (var key in $scope.schemaForms) {
      if ($scope.schemaForms[key] !== undefined && !$scope.schemaForms[key].$pristine && $scope.schemaForms[key].$setPristine) {
        $scope.schemaForms[key].$setPristine();
      }
    }
    delete $scope.schema;
    $scope.closeModal();
  };

  $scope.resetSchemaForms();

  $scope.createSchema = function () {
    $scope.schema = new Schema(SchemaRepo.getScaffold());
    $scope.openModal('#schemaCreateModal');
  };

  $scope.onCreateSchema = function () {
    $scope.submitting = true;
    SchemaRepo.create($scope.schema).then(function (res) {
      if (angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.onCancelCreateSchema();
      }
      $scope.submitting = false;
    });
  };

  $scope.onCancelCreateSchema = function () {
    $scope.resetSchemaForms();
    $scope.resetSchemaPropertyList = false;
    $timeout(function () {
      $scope.resetSchemaPropertyList = true;
    });
  };

  $scope.editSchema = function (schema) {
    $scope.schema = new Schema(angular.copy(schema));
    $scope.openModal('#schemaEditModal');
  };

  $scope.onEditSchema = function () {
    $scope.submitting = true;
    $scope.schema.save().then(function () {
      $scope.onCancelEditSchema();
      $scope.submitting = false;
    });
  };

  $scope.onCancelEditSchema = function () {
    $scope.resetSchemaForms();
    $scope.resetSchemaPropertyList = false;
    $timeout(function () {
      $scope.resetSchemaPropertyList = true;
    });
  };

  $scope.deleteSchema = function (schema) {
    $scope.schema = new Schema(angular.copy(schema));
    $scope.openModal('#schemaDeleteModal');
  };

  $scope.onCancelDeleteSchema = function () {
    $scope.resetSchemaForms();
  };

  $scope.onDeleteSchema = function () {
    $scope.submitting = true;
    $scope.schema.delete().then(function (res) {
      if (angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.resetSchemaForms();
      }
      $scope.submitting = false;
    });
  };

  $scope.showProperties = function (props) {
    $scope.propsToShow = props;
    $scope.openModal("#showPropertiesModal");
  };

  SchemaRepo.ready().then(function () {
    $scope.setTable = function () {
      $scope.tableParams = new NgTableParams({
        count: $scope.schemas.length,
        sorting: {
          name: 'asc'
        }
      }, {
        counts: [],
        total: 0,
        getData: function (params) {
          return $scope.schemas;
        }
      });
    };
    $scope.setTable();
  });

});
