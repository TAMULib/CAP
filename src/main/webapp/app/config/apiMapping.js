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
  IR: {
    channel: '/channel/ir',
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
    }
  }
};