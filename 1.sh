#!/bin/bash

echo "=== Reset środowiska Kafka Streams i tematów Kafka ==="

CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
BROKER="${CLUSTER_NAME}-w-0:9092"

echo "Kasowanie tematów Kafka..."
for TOPIC in stock-input stock-symbols stock-aggregates stock-anomalies; do
    if kafka-topics.sh --bootstrap-server $BROKER --list | grep -q "^${TOPIC}$"; then
        kafka-topics.sh --bootstrap-server $BROKER --delete --topic $TOPIC
        echo "Usunięto temat: $TOPIC"
    else
        echo "Temat ${TOPIC} nie istnieje, pomijam usuwanie."
    fi
done

echo "Tworzenie tematów Kafka..."
kafka-topics.sh --bootstrap-server $BROKER --create --topic stock-input --partitions 3 --replication-factor 2
kafka-topics.sh --bootstrap-server $BROKER --create --topic stock-symbols --partitions 1 --replication-factor 2
kafka-topics.sh --bootstrap-server $BROKER --create --topic stock-aggregates --partitions 3 --replication-factor 2
kafka-topics.sh --bootstrap-server $BROKER --create --topic stock-anomalies --partitions 3 --replication-factor 2

echo "Czyszczenie checkpointów Kafka Streams..."
rm -rf /tmp/kafka-streams/*

echo "Środowisko zostało zresetowane."