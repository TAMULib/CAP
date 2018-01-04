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
  IRProxy: {
    channel: '/channel/ir-proxy',
    validations: false,
    createContainer: {
      'endpoint': '/private/queue',
      'controller': 'ir-proxy',
      'method': ':type/:irid/container'
    },
    deleteContainer: {
      'endpoint': '/private/queue',
      'controller': 'ir-proxy',
      'method': ':type/:irid/container/delete'
    },
    getContainers: {
      'endpoint': '/private/queue',
      'controller': 'ir-proxy',
      'method': ':type/:irid/containers'
    },
    getProperties: {
      'endpoint': '/private/queue',
      'controller': 'ir-proxy',
      'method': ':type/:irid/metadata'
    },
    testPing: {
        'endpoint': '/private/queue',
        'controller': 'ir-test',
        'method': ':type/ping'
    },
    testAuth: {
        'endpoint': '/private/queue',
        'controller': 'ir-test',
        'method': ':type/auth'
    },
    testContent: {
        'endpoint': '/private/queue',
        'controller': 'ir-test',
        'method': ':type/content'
    }
  },
  IR: {
    channel: '/channel/ir',
    validations: true,
    all: {
        'endpoint': '/private/queue',
        'controller': 'ir',
        'method': 'all'
    },
    create: {
      'endpoint': '/private/queue',
      'controller': 'ir',
      'method': 'create'
    },
    update: {
        'endpoint': '/private/queue',
        'controller': 'ir',
        'method': 'update'
    },
    remove: {
        'endpoint': '/private/queue',
        'controller': 'ir',
        'method': 'delete'
    },
    getTypes: {
      'endpoint': '/private/queue',
      'controller': 'ir',
      'method': 'types'
    }
  }
};