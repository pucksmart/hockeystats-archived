module.exports = {
  use: [
    '@neutrinojs/standardjs',
    ['@neutrinojs/react', {
      html: {
        title: 'web',
        links: [
          {
            href: 'https://fonts.googleapis.com/css?family=Roboto:300,400,500',
            rel: 'stylesheet'
          }
        ]
      }
    }],
    '@neutrinojs/jest',
    (neutrino) => neutrino.config
      .entry('vendor')
      .add('react')
      .add('react-dom'),
    (neutrino) => neutrino.config.output.publicPath('/')
  ]
}
