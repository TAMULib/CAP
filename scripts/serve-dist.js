#!/usr/bin/env node

const fs = require('fs');
const process = require('process');
const StaticServer = require('static-server');

const fsPromises = fs.promises;
const basePath = './';

const server = new StaticServer({
  rootPath: '.',
  port: 9000,
  name: 'cap-static-server',
  followSymlink: true,
});

server.start(function () { console.log('HERE');
  console.log('Server listening to', server.port); // CAP/target/classes/templates/index.html
  fsPromises.copyFile('src/main/resources/templates/index.html', `${basePath}/index.html`);
});

process.on('exit', function () {
  fs.unlink(`${basePath}/index.html`, err => {
    if (err) throw err;
  });
  fs.unlink(`${basePath}/config.json`, err => {
    if (err) throw err;
  });
  console.log('Cleaning up');
});

process.on('SIGINT', function () {
  process.exit();
});

process.on('uncaughtException', function (e) {
  console.log(e.stack);
  process.exit(99);
});
