cap.filter("containedIn", function() {
  return function(input, array, prop) {
   
    var notContained = true;

    for(var i in array) {
      var item = array[i];
      if(input[prop]===item[prop]) {
        notcontained=false;
      }
    }
    
   
    return notContained;
  };

});