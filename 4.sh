#!/bin/bash

# Kopiowanie pliku konfiguracyjnego log4j
sudo cp /usr/lib/kafka/config/tools-log4j.properties /usr/lib/kafka/

# Dodanie konfiguracji log4j do connect-log4j.properties
echo "log4j.logger.org.reflections=ERROR" | \
sudo tee -a /usr/lib/kafka/config/connect-log4j.properties

# Pobranie nazwy klastra
CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)

# Uruchomienie aplikacji Java
java -cp "/usr/lib/kafka/libs/*:stock-market.jar" \
com.example.bigdata.ApacheLogToAlertRequests "${CLUSTER_NAME}-w-0:9092"