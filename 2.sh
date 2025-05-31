#!/usr/bin/env bash
set -euo pipefail

# --- Kafka settings ---
CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
BOOTSTRAP_SERVER="${CLUSTER_NAME}-w-0:9092"

# --- Kafka topics ---
TOPIC_STOCK_INPUT="stock-input"
TOPIC_STOCK_SYMBOLS="stock-symbols"

# --- Local directories ---
DOWNLOAD_DIR="/tmp/stock_data"
rm -rf "${DOWNLOAD_DIR}"
STOCK_FILES_DIR="${DOWNLOAD_DIR}/datasource1"
SYMBOLS_FILES_DIR="${DOWNLOAD_DIR}/datasource2"

# --- GCS Source ---
GCS_BUCKET= "$1"

# --- Download data from GCS ---
echo "$(date '+%Y-%m-%d %H:%M:%S') Downloading data from GCS..."
mkdir -p "${STOCK_FILES_DIR}" "${SYMBOLS_FILES_DIR}"
gsutil -m cp "${GCS_BUCKET}/stocks_result/*.csv" "${STOCK_FILES_DIR}/"
gsutil -m cp "${GCS_BUCKET}/stocks_symbols/*.csv" "${SYMBOLS_FILES_DIR}/"
echo "$(date '+%Y-%m-%d %H:%M:%S') Download complete."

# --- Function to stream files into Kafka ---
stream_to_kafka() {
    local directory="$1"
    local topic="$2"
    local header_lines=1
    local sleep_time=$3

    echo "$(date '+%Y-%m-%d %H:%M:%S') Streaming data from ${directory} to Kafka topic ${topic}..."

    for csvfile in "${directory}"/*.csv; do
        echo "  Processing file: ${csvfile}"
        tail -n +$((header_lines + 1)) "${csvfile}" | while IFS= read -r line; do
            if [ -n "$line" ]; then
                echo "$line" | kafka-console-producer.sh \
                    --broker-list "${BOOTSTRAP_SERVER}" \
                    --topic "${topic}" \
                    --property "parse.key=false" \
                    --property "parse.headers=false" \
                    --property "key.separator=:" >/dev/null
            fi
            sleep "${sleep_time}"
        done
    done
}

# --- First: Load static company data immediately ---
stream_to_kafka "${SYMBOLS_FILES_DIR}" "${TOPIC_STOCK_SYMBOLS}" 0.0

# --- Then: Stream stock data slowly (simulate real-time) ---
stream_to_kafka "${STOCK_FILES_DIR}" "${TOPIC_STOCK_INPUT}" 0.1

echo "$(date '+%Y-%m-%d %H:%M:%S') Finished streaming all data to Kafka."