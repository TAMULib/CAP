cap.component('plaintextLoader', {
  template: '<pre class="plaintext-loader">{{content | json}}</pre>',
  bindings: {
    src: '<'
  },
  controller: function($scope, $http) {
    
    this.$onInit = function() {
      $http.get(this.src).then(function(res) {
        $scope.content = res.data;
      });
    }; 

  }
});