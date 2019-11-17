const copy = require('@neutrinojs/copy');
const jest = require('@neutrinojs/jest');
const react = require('@neutrinojs/react');
const standardjs = require('@neutrinojs/standardjs');

module.exports = {
  use: [
    standardjs(),
    copy(),
    jest(),
    react({
      html: {
        title: 'web',
        template: require.resolve('./src/page-template.ejs')
      }
    }),
    (neutrino) => neutrino.config.plugin('copy').tap(args => [[{from: "static", to: "."}]].concat(args.slice(1))),
    (neutrino) => neutrino.config.output.publicPath('/')
  ]
}
