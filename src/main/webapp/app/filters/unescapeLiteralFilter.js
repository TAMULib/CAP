cap.filter('unescapeLiteral', function () {
  return function (str) {
    var unescaped = str;

    if (unescaped && unescaped.length > 0 && (unescaped[0] == "\"" || unescaped[0] == "'")) {
      var first = unescaped[0];
      var i = 1;
      for (; i < unescaped.length - 1; i++) {
        if (unescaped[i] == "\\") {
          if (i + 1 < unescaped.length) {
            switch (unescaped[i + 1]) {
              case "\t":
              case "\n":
              case "\r":
              case "\b":
              case "\f":
              case "\\":
              case "\"":
              case "'":
                break;
              default:
                continue;
            }

            unescaped = unescaped.substring(0, i) + unescaped.substring(i + 1);
          } else {
            // remove invalid "\".
            unescaped = unescaped.substring(0, i - 1);
          }
        }
      }

      // Attempt to fix invalid quoted literals.
      if (i === 1) {
        unescaped += first;
      } else if (unescaped[unescaped.length - 1] !== first) {
        unescaped = unescaped.substring(0, length - 2) + "\\" + unescaped[unescaped.length - 1] + first;
      }

      unescaped = unescaped.substring(1, unescaped.length - 1);
    }

    return unescaped;
  };
});
