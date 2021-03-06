**Support this project**
<!-- BADGES/ -->
<span class="badge-paypal">
<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&amp;hosted_button_id=MA847TR65D4N2" title="Donate to this project using PayPal">
<img src="https://img.shields.io/badge/paypal-donate-yellow.svg" alt="PayPal Donate"/>
</a></span>
<span class="badge-flattr">
<a href="https://flattr.com/submit/auto?fid=o6ok7n&url=https%3A%2F%2Fgithub.com%2Floxal" title="Donate to this project using Flattr">
<img src="https://img.shields.io/badge/flattr-donate-yellow.svg" alt="Flattr Donate" />
</a></span>
<span class="badge-gratipay"><a href="https://gratipay.com/~loxal" title="Donate weekly to this project using Gratipay">
<img src="https://img.shields.io/badge/gratipay-donate-yellow.svg" alt="Gratipay Donate" />
</a></span>
<!-- /BADGES -->

[Support this work with cryptocurrencies like BitCoin!](http://me.loxal.net/coin-support.html)

Services & Endpoints
=

## Endpoint
* OpenID 2.0 authentication: `/play/ground.html`


## Getting Started

1. **Add all corresponding required properties like `appdirect.oauth.consumer.secret` to your `~/.m2/settings.xml`.**

1. Run WAR __mvn jetty:run__
1. Call http://local.loxal.net:8200/application.wadl
1. Try http://local.loxal.net:8200

Alternatively _Tomcat_ Servlet Container can run the app

1. mvn package cargo:run

## Demo Showcase

* Demo Instance running on (private) CloudFoundry
    * https://rest-kit-v1.us-east.stage.internal.yaas.io/dilbert-quote/index.html

# Deploy & Release

## Deploy to Cloud Foundry

* Run `./release.sh` 

## Deploy to Google App Engine 

1. Edit `./release.sh` appropriately 
1. Run `./release.sh`