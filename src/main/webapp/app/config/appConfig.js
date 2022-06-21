var appConfig = {

  'version': '1.0.x',

  'allowAnonymous': true,
  'anonymousRole': 'ROLE_ANONYMOUS',

  'authStrategies': ['emailRegistration'],

  'authService': 'https://labs.library.tamu.edu/authfix',
  'webService': window.location.protocol + '//' + window.location.host + window.location.base,

  'storageType': 'session',

  'logging': {
    'log': true,
    'info': true,
    'warn': true,
    'error': true,
    'debug': true
  },

  'stompDebug': false,

  /*
    Determines the type of connection stomp will attempt to make with the service.
    TYPES:  websocket, xhr-streaming, xdr-streaming, eventsource, iframe-eventsource,
            htmlfile, iframe-htmlfile, xhr-polling, xdr-polling, iframe-xhr-polling,
            jsonp-polling
  */
  'sockJsConnectionType': ['websocket', 'xhr-polling'],

  // Set this to 'admin' or 'user' if using mock AuthService
  // otherwise set to null or false

  'mockRole': null,

  'messagingEnabled': false,

  'contentMap': {
    "image": ["image/jpeg", "image/png", "image/gif"],
    "plaintext": ["text/plain", "text/html", "text/css", "text/javascript", "text/csv", "text/markdown", "text/calendar", "text/xml", "application/xml", "application/json"],
    "seadragon": ["image/jp2", "image/tiff"],
    "pdf": ["application/pdf"]
  },

  'fedoraPath': '/fcrepo/rest/',

  'cantaloupeBaseUrl': 'https://api-dev.library.tamu.edu/iiif/2/',

  'iiifServiceUrl': 'https://api-dev.library.tamu.edu/iiif-service/'

};
