# Services & Endpoints

## Quotation Endpoint
* Browser client: /dilbert-quote/index.html
* Endpoint for enterprise environment working condition quotations: /dilbert-quote/enterprise
* Endpoint for manager quotations: /dilbert-quote/manager
* Endpoint for programmer quotations: /dilbert-quote/programmer

## Who am I

* /who-am-i:
    * Retrieves the IP address of the requesting host

## Getting Started

1. Run WAR __mvn jetty:run__
1. Call http://local.loxal.net:8200/application.wadl
1. Try http://local.loxal.net:8200

## Demo Showcase

* Demo Instance running on (private) CloudFoundry: https://rest-kit-v1.us-east.stage.internal.yaas.io/dilbert-quote/index.html

# Deploy & Release

## Deploy to CloudFoundry

* run `./release.sh` 

## Deploy to Google App Engine 

* edit `./release.sh` appropriately 
* run `./release.sh`