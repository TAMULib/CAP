var contextFilter = function(input, context) {
  var output = [];
  angular.forEach(input, function(triple) {
    var childContext = context.getChildContext(triple);
    if(this.type === 'container' && childContext.resource !== undefined && !childContext.resource) {
      output.push(triple);
    }
    if(this.type === 'resource' && childContext.resource !== undefined && childContext.resource) {
      output.push(triple);
    }
  }.bind(this));
  return output;
};

cap.filter("filterContainers", function() {
  return contextFilter.bind({type: 'container'});
});
  
cap.filter("filterResources", function() {
  return contextFilter.bind({type: 'resource'});
});
  