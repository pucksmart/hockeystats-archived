import React, { Component } from 'react'
import { BrowserRouter as Router, Route } from 'react-router-dom'
import { Security, ImplicitCallback } from '@okta/okta-react'
import './App.css'
import Home from './Home'

const config = {
  issuer: 'https://dev-505484.okta.com/oauth2/default',
  redirectUri: window.location.origin + '/implicit/callback',
  clientId: '0oa1p59jwkrRaEmql357',
  pkce: true
}

export default class App extends Component {
  render () {
    return (
      <Router>
        <Security {...config}>
          <Route path='/' exact component={Home} />
          <Route path='/implicit/callback' component={ImplicitCallback} />
        </Security>
      </Router>
    )
  }
}
