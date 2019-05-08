cap.controller("SchemaManagementController", function($controller, $scope, $timeout, SchemaRepo, NgTableParams) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.schemas = SchemaRepo.getAll();

  $scope.schemaToCreate = SchemaRepo.getScaffold();
  $scope.schemaToEdit = SchemaRepo.getScaffold();
  $scope.schemaToDelete = {};

  $scope.checkNamespaceOnEdit = undefined;

  $scope.schemaForms = {
    validations: SchemaRepo.getValidations(),
    getResults: SchemaRepo.getValidationResults
  };

  $scope.submitClicked = false;

  $scope.resetCreateSchemaPropertyList = true;

  $scope.resetSchemaForms = function() {
    SchemaRepo.clearValidationResults();
    for (var key in $scope.schemaForms) {
      if ($scope.schemaForms[key] !== undefined && !$scope.schemaForms[key].$pristine && $scope.schemaForms[key].$setPristine) {
        $scope.schemaForms[key].$setPristine();
      }
    }
    $scope.closeModal();
    $scope.checkNamespaceOnEdit = undefined;
  };

  $scope.resetSchemaForms();

  $scope.createSchema = function() {
    $scope.submitClicked = true;
    SchemaRepo.create($scope.schemaToCreate).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.cancelCreateSchema();
      }
      $scope.submitClicked = false;
    });
  };

  $scope.cancelCreateSchema = function() {
    angular.extend($scope.schemaToCreate, SchemaRepo.getScaffold());
    $scope.resetSchemaForms();
    $scope.resetCreateSchemaPropertyList = false;
    $timeout(function(){
      $scope.resetCreateSchemaPropertyList = true;
    });
  };

  $scope.editSchema = function(schema) {
    $scope.schemaToEdit = schema;
    $scope.checkNamespaceOnEdit = schema.namespace;
    $scope.openModal('#schemaEditModal');
  };

  $scope.updateSchema = function() {
    $scope.submitClicked = true;
    $scope.schemaToEdit.save().then(function() {
      $scope.cancelEditSchema();
      $scope.submitClicked = false;
    });
  };

  $scope.propertiesNeedsLoading = function(schema) {
    return !($scope.checkNamespaceOnEdit === undefined || $scope.checkNamespaceOnEdit === schema.namespace);
  };

  $scope.propertiesFound = function(properties) {
    if (Array.isArray(properties) && properties.length > 0) {
      $scope.checkNamespaceOnEdit = undefined;
    }
  };

  $scope.cancelEditSchema = function(schema) {
    $scope.schemaToEdit.refresh();
    $scope.schemaToEdit = SchemaRepo.getScaffold();
    $scope.resetSchemaForms();
  };

  $scope.confirmDeleteSchema = function(schema) {
    $scope.schemaToDelete = schema;
    $scope.openModal('#schemaDeleteModal');
  };

  $scope.cancelDeleteSchema = function(schema) {
    $scope.schemaToDelete = {};
    $scope.closeModal();
  };

  $scope.deleteSchema = function(schema) {
    $scope.submitClicked = true;
    SchemaRepo.delete(schema).then(function(res) {
      if(angular.fromJson(res.body).meta.status === "SUCCESS") {
        $scope.resetSchemaForms();
      }
      $scope.submitClicked = false;
    });
  };

  $scope.showProperties = function(props) {
    $scope.propsToShow = props;
    $scope.openModal("#showPropertiesModal");
  };

  SchemaRepo.ready().then(function() {
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
