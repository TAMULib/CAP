#!/usr/bin/env node

const https = require('https');
const fs = require('fs-extra');
const glob = require("glob");

const ghPagesPath = `src/main/resources/gh-pages`;
const lighthousePath = `src/main/resources/gh-pages/audit`;
const lighthouseAssetsPath = `${lighthousePath}/assets`;
const lighthouseCiPath = './.lighthouseci';

const createScoreList = report => {
  const lhrScoreList = [];
  Object.keys(report.categories).forEach(key => {
    var category = report.categories[key];
    lhrScoreList.push({'id': key, 'title': category.title, 'score': (category.score*100)});
  });
  return lhrScoreList;
}

const createReport = htmlReportPath => {
  fs.ensureDir(lighthousePath);
  fs.copySync( htmlReportPath , `./${lighthousePath}/index.html`);
}

const createBadges = resultList => {
  fs.ensureDirSync(lighthouseAssetsPath);
  resultList.forEach(r => {
    const badgeColor = (r.score >= 0 && r.score <= 49) ? 'red' :
      (r.score >= 50 && r.score <= 89) ? 'important' :
      'success' ;
    https.get(`https://img.shields.io/badge/${r.title}-${r.score}-${badgeColor}`, (resp) => {
      let data = '';
      resp.on('data', (chunk) => {
        data += chunk;
      });
      resp.on('end', () => {
        fs.writeFileSync(`./${lighthouseAssetsPath}/${r.id}.svg`, data );
      });
    }).on("error", (err) => {
      console.log("Error: " + err.message);
    });
  });
}

const logSoreList = scoreList => {
  console.log('Finished generating report');
  console.log('Results:');
  scoreList.forEach(s => {
    console.info(s.title, s.score);
  });
}

const createGhPagesDir = () => {
  if(fs.existsSync(ghPagesPath)) {
    const targetPath = 'target/';
    fs.ensureDir(targetPath);
    fs.ensureDir(`${lighthousePath}`);
    // fs.ensureDir(`${ghPagesPath}/assets`);
    fs.copy(`./${ghPagesPath}/index.html`, `${targetPath}/gh-pages/index.html`);
    fs.copy(`./${lighthousePath}/`, `${targetPath}/gh-pages/audit/`);
    fs.copy(`${targetPath}/generated-docs/`, `${targetPath}/gh-pages/api-docs/`);
  }
}

if(fs.existsSync(lighthouseCiPath)) {
  fs.ensureDir(`${lighthousePath}`);
  const htmlReportPath = glob.sync(`${lighthouseCiPath}/lhr-*.html`, {})[0];
  const jsonReportPath = glob.sync(`${lighthouseCiPath}/lhr-*.json`, {})[0];
  const jsonReportRaw = fs.readFileSync(jsonReportPath);
  const jsonReport = JSON.parse(jsonReportRaw);
  const scoreList = createScoreList(jsonReport);
  createReport(htmlReportPath);
  createBadges(scoreList);
  logSoreList(scoreList);
  createGhPagesDir();

} else {
  console.warn(`${lighthouseCiPath} does not exist. Please run 'npm run test:audit'`);
}



