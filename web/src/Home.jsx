import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { withAuth } from '@okta/okta-react'
import { Box, Button, Grommet, Heading } from 'grommet'

const theme = {
  global: {
    colors: {
      brand: '#228BE6'
    },
    font: {
      family: 'Roboto',
      size: '18px',
      height: '20px'
    }
  }
}

const AppBar = (props) => (
  <Box
    tag='header'
    direction='row'
    align='center'
    justify='between'
    background='brand'
    pad={{ left: 'medium', right: 'small', vertical: 'small' }}
    elevation='medium'
    style={{ zIndex: '-1' }}
    {...props}
  />
)

class Home extends Component {
  constructor (props) {
    super(props)
    this.state = { authenticated: null }
    this.checkAuthentication = this.checkAuthentication.bind(this)
    this.checkAuthentication()
    this.handleLogin = this.handleLogin.bind(this)
    this.handleLogout = this.handleLogout.bind(this)
  }

  async checkAuthentication () {
    const authenticated = await this.props.auth.isAuthenticated()
    if (authenticated !== this.state.authenticated) {
      this.setState({ authenticated })
    }
  }

  componentDidUpdate () {
    this.checkAuthentication()
  }

  async handleLogin () {
    // Redirect to '/' after login
    this.props.auth.login('/')
  }

  async handleLogout () {
    // Redirect to '/' after logout
    this.props.auth.logout('/')
  }

  render () {
    if (this.state.authenticated === null) return null

    const button = this.state.authenticated
      ? <Button onClick={this.handleLogout}>Logout</Button>
      : <Button onClick={this.handleLogin}>Login</Button>

    return (
      <Grommet theme={theme}>
        <AppBar>
          <Heading level='3' margin='none'>Hockey Stats</Heading>
          {button}
        </AppBar>
        <Box direction='row' flex overflow={{ horizontal: 'hidden' }}>
          <Box flex align='center' justify='center'>
            app body
          </Box>
        </Box>
      </Grommet>
    )
  }
}

Home.propTypes = {
  auth: PropTypes.object.isRequired
}

export default withAuth(Home)
