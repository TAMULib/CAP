cap.filter("containedIn", function($filter) {
  return function(list, arrayFilter, element){
    if(arrayFilter){
      return $filter("filter")(list, function(listItem){
        var found = true;
        angular.forEach(arrayFilter, function(arrayItem) {
          if(arrayItem[element]===listItem[element]) {
            found = false;
          }
        });
        return found;
      });
    }
  };
});