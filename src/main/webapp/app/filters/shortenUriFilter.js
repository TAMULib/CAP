cap.filter('shortenUri', function () {
  return function (longUri, baseUri) {
    var shortUri = longUri;

    if (longUri.indexOf(baseUri) !== -1) {
      var parts = longUri.split(baseUri, 2);
      shortUri = parts[1] ? parts[1] : '';
    }

    if (shortUri[0] == '/') {
      shortUri = shortUri.substring(1);
    }

    return shortUri;
  };
});
