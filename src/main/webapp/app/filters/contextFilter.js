var contextFilter = function(input, context) {
  var output = [];
  angular.forEach(input, function(child) {
    var childContext = context.getChildContext(child.triple);
    if(this.type === 'container' && childContext.resource !== undefined && !childContext.resource) {
      output.push(childContext);
    }
    if(this.type === 'resource' && childContext.resource !== undefined && childContext.resource) {
      output.push(childContext);
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
