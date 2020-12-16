
const gatewayUrl='http://121.40.70.203:8085'
module.exports = {
  env: {
    NODE_ENV: '"development"'
  },
  defineConstants: {},
  mini: {},
  h5: {
    publicPath: '/',
    devServer: {
      proxy: {
        '/front': gatewayUrl,
      }
    },
  }
}
