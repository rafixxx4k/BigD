#!/bin/bash

# Ustaw domyślne wartości
DELAY_MODE="A"
DAYS_WINDOW=7
PERCENTAGE_THRESHOLD=40.0

# Przetwarzanie argumentów
if [ ! -z "$1" ]; then
  DELAY_MODE="$1"
fi

if [ ! -z "$2" ]; then
  DAYS_WINDOW="$2"
fi

if [ ! -z "$3" ]; then
  PERCENTAGE_THRESHOLD="$3"
fi

# Kopiowanie pliku konfiguracyjnego log4j
sudo cp /usr/lib/kafka/config/tools-log4j.properties /usr/lib/kafka/

# Dodanie konfiguracji log4j do connect-log4j.properties
echo "log4j.logger.org.reflections=ERROR" | \
sudo tee -a /usr/lib/kafka/config/connect-log4j.properties

# Pobranie nazwy klastra
CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)

# Uruchomienie aplikacji Java z parametrami
java -cp "/usr/lib/kafka/libs/*:3.jar" \
com.example.bigdata.ApacheLogToAlertRequests \
"${CLUSTER_NAME}-w-0:9092" "$DELAY_MODE" "$DAYS_WINDOW" "$PERCENTAGE_THRESHOLD"