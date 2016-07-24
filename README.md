# gdax-java-client
Java library around [GDAX API] (https://docs.gdax.com/#api)

## Features
- Most of the API to handle accounts, orders and market data
- WebSocket subscription
- Sandbox/Live servers
- Play with public API without authentication

## Getting started
``` java
GdaxClient gdaxClient = GdaxClientImpl.simple(GdaxConfig.sandboxNoAuth());
List<GdaxTrade> trades = gdaxClient.getTrades("BTC-USD");
// Preferably convert all response objects to your own types.
```

## Possibly improvements
- Proper response objects with HTTP status
- Replace depdency to Apache's HTTP Client with an SPI
- Implement the rest of the GDAX API
- Logging and better error handling
