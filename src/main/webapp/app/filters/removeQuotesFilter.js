cap.filter('removeQuotes', function () {
  return function (str) {
    if (str[0] === '"' && str[str.length - 1] === '"') {
      return str.substring(1, str.length - 1);
    }
    return str;
  };
});