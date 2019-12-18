describe("directive: repositoryViewSection", function () {
  var $compile, $q, $scope, directive, element, context, title, type, list, listElementAction, addAction, removeAction, editAction;

  var initializeVariables = function () {
    inject(function (_$q_, _$compile_) {
      $q = _$q_;
      $compile = _$compile_;

      context = "";
      title = "";
      type = "";
      list = "";
      listElementAction = "";
      addAction = "";
      removeAction = "";
      editAction = "";
    });
  };

  var initializeDirective = function (settings) {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      var attr = settings && settings.attr ? settings.attr : "context=\"context\" title=\"title\" type=\"type\" list=\"list\" list-element-action=\"listElementAction\" add-action=\"addAction\" remove-action=\"removeAction\" edit-action=\"editAction\"";
      var body = settings && settings.body ? settings.body : "";

      element = angular.element("<repository-view-section " + attr + ">" + body + "</repository-view-section>");
      directive = $compile(element)($scope);

      $scope.context = context;
      $scope.title = title;
      $scope.type = type;
      $scope.list = list;
      $scope.listElementAction = listElementAction;
      $scope.addAction = addAction;
      $scope.removeAction = removeAction;
      $scope.editAction = editAction;

      $scope.$digest();
    });
  };

  beforeEach(function () {
    module("core");
    module("cap");
    module("templates");

    // @fixme: RepositoryViewSectionService is defined within this file! (it probably should be moved and the mock and tests need to then be written.)
    //module("mock.repositoryViewSectionService");

    installPromiseMatchers();
    initializeVariables();
  });

  describe("Is the directive", function () {
    it("defined", function () {
      initializeDirective();
      expect(directive).toBeDefined();
    });
  });

  describe("Does the directive", function () {
    it("work with context property", function () {
      context = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with title property", function () {
      title = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with type property", function () {
      type = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with list property", function () {
      list = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with listElementAction property", function () {
      listElementAction = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with addAction property", function () {
      addAction = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with removeAction property", function () {
      removeAction = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });

    it("work with editAction property", function () {
      editAction = "";

      //initializeDirective();
      //expect(directive).toBeDefined();
    });
  });

});
