cap.component('plaintextLoader', {
  template: '<pre class="plaintext-loader">{{content}}</pre>',
  bindings: {
    src: '<'
  },
  controller: function($scope, $http, $filter) {
    
    this.$onInit = function() {

      var filterTypes = {
        "application/json": "json"
      };

      $http.get(this.src).then(function(res) {
        var contentType = res.headers()['content-type'];
        var filterName = filterTypes[contentType];
        $scope.content = filterName ? $filter(filterName)(res.data) : res.data;
      });
    }; 

  }
});