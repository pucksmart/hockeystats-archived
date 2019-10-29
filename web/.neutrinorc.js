module.exports = {
  use: [
    '@neutrinojs/standardjs',
    [
      '@neutrinojs/react',
      {
        html: {
          title: 'web'
        }
      }
    ],
    '@neutrinojs/jest'
  ]
};
