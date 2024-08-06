#!/bin/bash

cd kafka_2.13-3.8.0
bin/zookeeper-server-start.sh config/zookeeper.properties &
bin/kafka-server-start.sh config/server.properties &
