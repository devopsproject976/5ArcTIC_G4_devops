module.exports = function(config) {
  config.set({
    // ... other Karma configuration ...
    browsers: ['ChromeHeadless'],
    customLaunchers: {
      ChromeHeadless: {
        base: 'ChromeHeadless',
        flags: ['--no-sandbox'] // --no-sandbox is often required for CI environments
      }
    }
  });
};
