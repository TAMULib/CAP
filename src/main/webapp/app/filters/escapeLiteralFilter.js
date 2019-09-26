cap.filter('escapeLiteral', function () {
  return function (str, q) {
    var escaped = str;
    var quote = q ? quote : "\"";

    if (escaped && escaped.length > 0) {
      for (var i = 0; i < escaped.length; i++) {
        switch (escaped[i]) {
          case "\"":
            if (quote === "'") continue;
            break;
          case "'":
            if (quote === "\"") continue;
            break;
          case "\t":
          case "\n":
          case "\r":
          case "\b":
          case "\f":
          case "\\":
            break;
          default:
            continue;
        }

        escaped = escaped.substring(0, i) + "\\" + escaped.substring(i);
        i++;
      }

      escaped = quote + escaped + quote;
    }

    return escaped;
  };
});
