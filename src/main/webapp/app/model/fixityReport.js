cap.model("FixityReport", function FixityReport(HttpMethodVerbs) {
  return function FixityReport() {
    console.log();

    var fixityReport = this;

    fixityReport.run = function () {
      
      fixityReport.finished = false;

      var fixityPromise = fixityReport.ir.performRequest(fixityReport.getMapping().load, {
        method: HttpMethodVerbs.GET,
        query: {
          contextUri: fixityReport.contextUri
        }
      });

      fixityPromise.then(function(apiRes) {
        var newReport = angular.fromJson(apiRes.body).payload.FixityReport;
        angular.extend(fixityReport, newReport);
        fixityReport.finished = true;
      });

      return fixityPromise;
    };


    return fixityReport;
  };
});