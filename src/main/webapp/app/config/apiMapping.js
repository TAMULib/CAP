// CONVENTION: must match model name, case sensitive
var apiMapping = {
  User: {
    channel: '/channel/user',
    instantiate: {
        'endpoint': '/private/queue',
        'controller': 'user',
        'method': 'credentials'
    },
    all: {
        'endpoint': '/private/queue',
        'controller': 'user',
        'method': 'all'
    },
    update: {
        'endpoint': '/private/queue',
        'controller': 'user',
        'method': 'update'
    },
    remove: {
        'endpoint': '/private/queue',
        'controller': 'user',
        'method': 'delete'
    }
  },
  RVContext: {
    channel: '/channel/rv-context',
    validations: false,
    lazy: true,
    load: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid'
    },
    rvContext: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid'
    },
    metadata: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid/metadata'
    },
    children: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid/children'
    },
    resource: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid/resource'
    },
    version: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid/version'
    },
    advancedQuery: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid/query'
    },
    resourceFixity: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid/resource/fixity'
    }
  },
  FixityReport: {
    validations: false,
    method: "",
    lazy: false,
    load: {
      'endpoint': '/private/queue',
      'httpMethod': 'GET',
      'controller': 'rv-context',
      'method': ':type/:rvid/resource/fixity'
    },
    instantiate: {
      'endpoint': '/private/queue',
      'httpMethod': 'GET',
      'controller': 'rv-context',
      'method': ':type/:rvid/resource/fixity'
    }
  },
  VerifyRVSettings: {
    verifyPing: {
      'endpoint': '/private/queue',
      'controller': 'rv',
      'method': ':type/verify/ping'
    },
    verifyAuth: {
        'endpoint': '/private/queue',
        'controller': 'rv',
        'method': ':type/verify/auth'
    },
    verifyContent: {
        'endpoint': '/private/queue',
        'controller': 'rv',
        'method': ':type/verify/content'
    }
  },
  RV: {
    channel: '/channel/rv',
    validations: true,
    all: {
        'endpoint': '/private/queue',
        'controller': 'rv',
        'method': '',
        'httpMethod': "GET"
    },
    create: {
      'endpoint': '/private/queue',
      'controller': 'rv',
      'method': '',
      'httpMethod': "POST"
    },
    update: {
        'endpoint': '/private/queue',
        'controller': 'rv',
        'method': '',
        'httpMethod': "PUT"
    },
    remove: {
        'endpoint': '/private/queue',
        'controller': 'rv',
        'method': '',
        'httpMethod': "DELETE"
    },
    transaction: {
      'endpoint': '/private/queue',
      'controller': 'rv-context',
      'method': ':type/:rvid/transaction'
    },
    getTypes: {
      'endpoint': '/private/queue',
      'controller': 'rv',
      'method': 'types'
    }
  },
  Schema: {
    channel: '/channel/schema',
    validations: true,
    all: {
        'endpoint': '/private/queue',
        'controller': 'schema',
        'method': '',
        'httpMethod': 'GET'
    },
    create: {
      'endpoint': '/private/queue',
      'controller': 'schema',
      'method': '',
      'httpMethod': 'POST'
    },
    update: {
        'endpoint': '/private/queue',
        'controller': 'schema',
        'method': '',
        'httpMethod': 'PUT'
    },
    remove: {
        'endpoint': '/private/queue',
        'controller': 'schema',
        'method': '',
        'httpMethod': 'DELETE'
    },
    findProperties: {
      'endpoint': '/private/queue',
      'controller': 'schema',
      'method': 'properties'
    }
  }
};