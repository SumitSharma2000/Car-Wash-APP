module.exports = function (config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage'),
      require('@angular-devkit/build-angular/plugins/karma')
    ],
    client: {
      jasmine: {
        // Enable better test organization and reporting
        random: false, // Run tests in order for better debugging
        seed: '4321',
        oneFailurePerSpec: false, // Show all failures, not just first one
        failFast: false, // Continue running tests even after failures
        timeoutInterval: 5000 // Increased timeout for complex tests
      },
      clearContext: false // Keep test results visible in browser
    },
    jasmineHtmlReporter: {
      suppressAll: false, // Show all test details in browser
      suppressSkipped: false, // Show skipped tests
      suppressFailed: false, // Show failed tests with details
      suppressPassed: false // Show passed tests for complete overview
    },
    coverageReporter: {
      dir: require('path').join(__dirname, './coverage/car-wash-frontend'),
      subdir: '.',
      reporters: [
        { type: 'html' },
        { type: 'text-summary' },
        { type: 'lcov' }
      ],
      check: {
        global: {
          statements: 85, // Increased coverage requirements
          branches: 80,
          functions: 85,
          lines: 85
        }
      }
    },
    reporters: ['progress', 'kjhtml', 'coverage'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    // Use Chrome for browser testing to see results visually
    browsers: ['Chrome'],
    customLaunchers: {
      ChromeHeadlessCI: {
        base: 'ChromeHeadless',
        flags: ['--no-sandbox', '--disable-web-security']
      },
      ChromeDebug: {
        base: 'Chrome',
        flags: ['--remote-debugging-port=9333']
      }
    },
    singleRun: false, // Keep browser open for interactive testing
    restartOnFileChange: true
  });
};