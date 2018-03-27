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
  IRContext: {
    channel: '/channel/ir-context',
    validations: false,
    lazy: true,
    load: {
      'endpoint': '/private/queue',
      'controller': 'ir-context',
      'method': ':type/:irid'
    },
    irContext: {
      'endpoint': '/private/queue',
      'controller': 'ir-context',
      'method': ':type/:irid'
    },
    metadata: {
      'endpoint': '/private/queue',
      'controller': 'ir-context',
      'method': ':type/:irid/metadata'
    },
    children: {
      'endpoint': '/private/queue',
      'controller': 'ir-context',
      'method': ':type/:irid/children'
    },
    resource: {
      'endpoint': '/private/queue',
      'controller': 'ir-context',
      'method': ':type/:irid/resource'
    },
    version: {
      'endpoint': '/private/queue',
      'controller': 'ir-context',
      'method': ':type/:irid/version'
    },
    advancedQuery: {
      'endpoint': '/private/queue',
      'controller': 'ir-context',
      'method': ':type/:irid/query'
    },
    resourceFixity: {
      'endpoint': '/private/queue',
      'controller': 'ir-context',
      'method': ':type/:irid/resource/fixity'
    }
  },
  TestIRSettings: {
    testPing: {
      'endpoint': '/private/queue',
      'controller': 'ir',
      'method': ':type/test/ping'
    },
    testAuth: {
        'endpoint': '/private/queue',
        'controller': 'ir',
        'method': ':type/test/ping'
    },
    testContent: {
        'endpoint': '/private/queue',
        'controller': 'ir',
        'method': ':type/test/ping'
    }
  },
  IR: {
    channel: '/channel/ir',
    validations: true,
    all: {
        'endpoint': '/private/queue',
        'controller': 'ir',
        'method': '',
        'httpMethod': "GET"
    },
    create: {
      'endpoint': '/private/queue',
      'controller': 'ir',
      'method': '',
      'httpMethod': "POST"
    },
    update: {
        'endpoint': '/private/queue',
        'controller': 'ir',
        'method': '',
        'httpMethod': "PUT"
    },
    remove: {
        'endpoint': '/private/queue',
        'controller': 'ir',
        'method': '',
        'httpMethod': "DELETE"
    },
    getTypes: {
      'endpoint': '/private/queue',
      'controller': 'ir',
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