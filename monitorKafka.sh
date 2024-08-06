#!/bin/bash

# Settings
TOPIC=crypto-prices

cd kafka_2.13-3.8.0
bin/kafka-console-consumer.sh --topic $TOPIC --bootstrap-server localhost:9092 --from-beginning