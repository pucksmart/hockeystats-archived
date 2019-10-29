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
    '@neutrinojs/jest',
    (neutrino) => neutrino.config
      .entry('vendor')
      .add('react')
      .add('react-dom')
  ]
}
