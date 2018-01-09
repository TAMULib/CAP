cap.filter('removeQuotes', function() {
    return function(str) {
      if(str.startsWith("\"") && str.endsWith("\"")) {
        return str.substring(1, str.length - 1);
      }
      return str;
    };
});
