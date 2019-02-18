var appConfig = {

    'version': '1.0.x',

    'allowAnonymous': false,
    'anonymousRole': 'ROLE_ANONYMOUS',

    'authService': 'https://labs.library.tamu.edu/auth3',
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

    'mockRole': 'admin',

    'contentMap': {"image": ["image/jpeg","image/png","image/gif"],"seadragon": ["image/jp2","image/tiff"], "plaintext": ["text/plain", "text/html", "text/javascript", "text/css", "text/csv", "text/markdown", "application/json"]},

    'cantaloupeBaseUrl': 'https://api-dev.library.tamu.edu/iiif/2/',
    'fedoraPath': '/fcrepo/rest/',

    'iiifServiceUrl': 'https://api-dev.library.tamu.edu/iiif-service/'

};
