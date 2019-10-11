module.exports = function(config){
  config.set({

    preprocessors: {
      "app/!(node_modules)/**/*.js": "coverage",
      "app/views/**/*.html": ["ng-html2js"]
    },

    reporters: ["progress", "coverage"],

    basePath : "../",

    files : [
      "app/config/appConfig.js",
      "app/config/apiMapping.js",

      "app/node_modules/jquery/dist/jquery.js",
      "app/node_modules/bootstrap/dist/js/bootstrap.js",

      "app/node_modules/sockjs-client/dist/sockjs.js",
      "app/node_modules/stompjs/lib/stomp.js",

      "app/node_modules/angular/angular.js",

      "app/node_modules/angular-sanitize/angular-sanitize.js",
      "app/node_modules/angular-route/angular-route.js",
      "app/node_modules/angular-loader/angular-loader.js",
      "app/node_modules/angular-messages/angular-messages.js",
      "app/node_modules/angular-mocks/angular-mocks.js",

      "app/node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls.js",

      "app/node_modules/ng-table/bundles/ng-table.js",

      "app/node_modules/ng-file-upload/dist/ng-file-upload-shim.js",
      "app/node_modules/ng-file-upload/dist/ng-file-upload.js",

      "app/node_modules/openseadragon/build/openseadragon/openseadragon.js",
      "app/node_modules/ng-openseadragon/build/angular-openseadragon.js",

      "app/node_modules/weaver-ui-core/app/config/coreConfig.js",

      "app/node_modules/jasmine-promise-matchers/dist/jasmine-promise-matchers.js",

      "app/node_modules/jasmine-promise-matchers/dist/jasmine-promise-matchers.js",

      "app/node_modules/weaver-ui-core/app/config/coreConfig.js",

      "app/node_modules/weaver-ui-core/app/components/**/*.js",

      "app/node_modules/weaver-ui-core/app/core.js",

      "app/node_modules/weaver-ui-core/app/**/*.js",

      "tests/testSetup.js",

      "app/app.js",

      "app/components/**/*.js",

      "app/config/runTime.js",

      "app/controllers/**/*.js",

      "app/directives/**/*.js",

      "app/filters/**/*.js",

      "app/model/**/*.js",

      "app/repo/**/*.js",

      "app/services/**/*.js",

      "app/views/**/*.html",

      "tests/core/**/*.js",

      "tests/mocks/**/*.js",

      "tests/unit/**/*.js"
    ],

    autoWatch : true,

    frameworks: ["jasmine"],

    browsers : ["Firefox", "Chrome", "ChromeHeadless", "ChromeHeadlessNoSandbox"],

    customLaunchers: {
      ChromeHeadlessNoSandbox: {
        base: "ChromeHeadless",
        flags: ["--no-sandbox"]
      }
    },

    plugins : [
      "karma-coverage",
      "karma-jasmine",
      "karma-chrome-launcher",
      "karma-firefox-launcher",
      "karma-junit-reporter",
      "karma-ng-html2js-preprocessor"
    ],

    junitReporter : {
      outputFile: "test_out/unit.xml",
      suite: "unit"
    },

    ngHtml2JsPreprocessor: {
      stripPrefix: "app/",
      moduleName: "templates"
    },

    coverageReporter: {
      type: "lcov",
      dir: "coverage/"
    }

  });
};
