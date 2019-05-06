cap.filter('lengthenUri', function () {
  return function (shortUri, rootUri) {
    var longUri = shortUri;

    // utilize new URL() throwing an exception when contextUri is not a full URI to determine if a long/full URL needs to be generated.
    try {
      new URL(shortUri);
    } catch (_) {
      if (rootUri.length > 0) {
        if (rootUri[rootUri.length - 1] == "/" || rootUri[rootUri.length - 1] == "#") {
          longUri = shortUri.length > 0 && shortUri[0] == "/" ? rootUri + shortUri.substring(1) : rootUri + shortUri;
        } else {
          longUri = shortUri.length > 0 && shortUri[0] == "/" ? rootUri + shortUri : rootUri + "/" + shortUri;
        }
      }
    }

    return longUri;
  };
});
