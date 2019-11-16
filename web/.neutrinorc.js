module.exports = {
  use: [
    '@neutrinojs/copy',
    '@neutrinojs/jest',
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
    '@neutrinojs/standardjs',
    (neutrino) => neutrino.config.plugin('copy').tap(args => [[{from: "static", to: "."}]].concat(args.slice(1))),
    (neutrino) => neutrino.config
      .entry('vendor')
      .add('react')
      .add('react-dom')
      .add('react-router-dom')
      .add('grommet')
      .add('grommet-icons')
      .add('styled-components'),
    (neutrino) => neutrino.config.output.publicPath('/')
  ]
}
