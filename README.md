# clj-blockchain

Simple blockchain implementation in Clojure. Based on the great description on https://hackernoon.com/learn-blockchains-by-building-one-117428612f46

## Not yet implemented

Consensus functions for multiple hosts not yet implemented.

Transaction validation mechanisms not yet implemented.


## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## Usage

The following GET paths are currently implemented
    /chain              - list the complete blockchain
    /mine               - mine the next block
    /transactions/open  - list of currently opened transactions

To add a new transaction send a POST request (Content-Type must be application/json)
    /transactions/new   - e.g. { "sender": "my address", "recipient": "someone else's address", "amount": 5}

## License

Copyright © 2018 by Sascha Koch
