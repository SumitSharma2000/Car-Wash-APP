module.exports = function(config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine'],
    files: [
      'test-reports/test-results.js'
    ],
    exclude: [],
    preprocessors: {},
    reporters: ['kjhtml'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: false,
    browsers: ['Chrome'],
    singleRun: false,
    concurrency: Infinity
  });
};