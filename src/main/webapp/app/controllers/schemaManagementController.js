cap.controller("SchemaManagementController", function($controller, $scope, $timeout, SchemaRepo, NgTableParams) {

  angular.extend(this, $controller('CoreAdminController', {
      $scope: $scope
  }));

  $scope.schemas = SchemaRepo.getAll();

  $scope.schemaToCreate = SchemaRepo.getScaffold();
  $scope.schemaToEdit = SchemaRepo.getScaffold();
  $scope.schemaToDelete = {};

  $scope.schemaForms = {
    validations: SchemaRepo.getValidations(),
    getResults: SchemaRepo.getValidationResults
  };

  $scope.submitClicked = false;

  $scope.resetSchemaPropertyList = true;

  $scope.resetSchemaForms = function() {
    SchemaRepo.clearValidationResults();
    for (var key in $scope.schemaForms) {
      if ($scope.schemaForms[key] !== undefined && !$scope.schemaForms[key].$pristine && $scope.schemaForms[key].$setPristine) {
        $scope.schemaForms[key].$setPristine();
      }
    }
    $scope.closeModal();
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
    $scope.resetSchemaPropertyList = false;
    $timeout(function(){
      $scope.resetSchemaPropertyList = true;
    });
  };

  $scope.editSchema = function(schema) {
    $scope.schemaToEdit = schema;
    $scope.openModal('#schemaEditModal');
  };

  $scope.updateSchema = function() {
    $scope.submitClicked = true;
    $scope.schemaToEdit.save().then(function() {
      $scope.cancelEditSchema();
      $scope.submitClicked = false;
    });
  };

  $scope.cancelEditSchema = function(schema) {
    $scope.schemaToEdit.refresh();
    $scope.schemaToEdit = SchemaRepo.getScaffold();
    $scope.resetSchemaForms();
    $scope.resetSchemaPropertyList = false;
    $timeout(function(){
      $scope.resetSchemaPropertyList = true;
    });
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
