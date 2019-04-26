// CONVENTION: must match model name, case sensitive
var apiMapping = {
  User: {
    channel: '/channel/user',
    lazy: true,
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
  RepositoryViewContext: {
    channel: '/channel/repository-view-context',
    validations: false,
    lazy: true,
    load: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId'
    },
    repositoryViewContext: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId'
    },
    metadata: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/metadata'
    },
    children: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/children'
    },
    resource: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/resource'
    },
    version: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/version'
    },
    advancedQuery: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/query'
    },
    resourceFixity: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/resource/fixity'
    }
  },
  FixityReport: {
    validations: false,
    method: "",
    lazy: false,
    load: {
      'endpoint': '/private/queue',
      'httpMethod': 'GET',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/resource/fixity'
    },
    instantiate: {
      'endpoint': '/private/queue',
      'httpMethod': 'GET',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/resource/fixity'
    }
  },
  VerifyRepositoryViewSettings: {
    verifyPing: {
      'endpoint': '/private/queue',
      'controller': 'repository-view',
      'method': ':type/verify/ping'
    },
    verifyAuth: {
        'endpoint': '/private/queue',
        'controller': 'repository-view',
        'method': ':type/verify/auth'
    },
    verifyContent: {
        'endpoint': '/private/queue',
        'controller': 'repository-view',
        'method': ':type/verify/content'
    }
  },
  RepositoryView: {
    channel: '/channel/repository-view',
    validations: true,
    all: {
        'endpoint': '/private/queue',
        'controller': 'repository-view',
        'method': '',
        'httpMethod': "GET"
    },
    create: {
      'endpoint': '/private/queue',
      'controller': 'repository-view',
      'method': '',
      'httpMethod': "POST"
    },
    update: {
        'endpoint': '/private/queue',
        'controller': 'repository-view',
        'method': '',
        'httpMethod': "PUT"
    },
    remove: {
        'endpoint': '/private/queue',
        'controller': 'repository-view',
        'method': '',
        'httpMethod': "DELETE"
    },
    transaction: {
      'endpoint': '/private/queue',
      'controller': 'repository-view-context',
      'method': ':type/:repositoryViewId/transaction'
    },
    getTypes: {
      'endpoint': '/private/queue',
      'controller': 'repository-view',
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