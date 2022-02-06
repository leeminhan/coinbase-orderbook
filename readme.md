## Coinbase WebSocket Feed
This application subscribes to Coinbase L2 WebSocket channel and maintains an OrderBook of 10 levels.

### Getting Started
1. Compile Project
```bash
mvn clean compile package
```
2. Start Application
```bash
java -jar target/coinbase-orderbook-1.0-SNAPSHOT.jar BTC-USD
```
3. Terminate Application
```
CTRL+C
```

### Demo

![](demo.gif)

### WebSocket Information
#### L2 Subscription Message
```json lines
{
    "type": "subscribe",
    "product_ids": ["BTC-USD"],
    "channels": ["level2"]
}
```
#### L2 Subscription Response
```json lines
{"type":"subscriptions","channels":[{"name":"level2","product_ids":["BTC-USD"]}]}
```

#### L2 Snapshot Message
```json lines
{
    "type": "snapshot",
    "product_id": "BTC-USD",
    "bids": [["10101.10", "0.45054140"]],
    "asks": [["10102.55", "0.57753524"]]
}
```

#### L2 Update Message
```json lines
{"type":"l2update","product_id":"BTC-USD","changes":[["buy","41501.16","0.03924800"]],"time":"2022-02-06T06:43:18.845125Z"}
```