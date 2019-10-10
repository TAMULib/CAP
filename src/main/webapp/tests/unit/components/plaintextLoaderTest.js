describe("component: plaintextLoader", function () {
  var $compile, $httpBackend, $scope, component, element, src;

  var initializeVariables = function() {
    inject(function (_$compile_, _$httpBackend_) {
      $compile = _$compile_;
      $httpBackend = _$httpBackend_;

      src = "";

      // @todo responsd needs headers set and also provide a valid mocked response.
      $httpBackend.whenGET(src).respond('');
    });
  };

  var initializeComponent = function() {
    inject(function (_$rootScope_) {
      $scope = _$rootScope_.$new();

      element = angular.element("<plaintextLoader src=\"src\"></plaintextLoader>");
      component = $compile(element)($scope);

      $scope.src = src;

      $scope.$digest();

      // @todo find a way to unit test controller methods, the angularjs documentation uses a Spy, which prevents actual unit testing.
    });
  };

  beforeEach(function() {
    module("core");
    module("cap");
    module('templates');

    installPromiseMatchers();
    initializeVariables();
  });

  describe("Is the component defined", function () {
    it("should be defined", function () {
      initializeComponent();
      expect(component).toBeDefined();
    });
  });

  describe("Does the component initialize properly", function () {
    it("should perform a http.get() on the provided src URL", function () {
      src = "http://localhost/";

      initializeComponent();
    });
  });

});
