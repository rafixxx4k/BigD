echo "=== Skrypt przygotowujący miejsce docelowe ==="

CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
BROKER="${CLUSTER_NAME}-w-0:9092"

echo "Reset miejsc docelowych..."
for TOPIC in stock-result stock-anomalies; do
    if kafka-topics.sh --bootstrap-server $BROKER --list | grep -q "^${TOPIC}$"; then
        kafka-topics.sh --bootstrap-server $BROKER --delete --topic $TOPIC
        echo "Usunięto: $TOPIC"
    else
        echo "${TOPIC} nie istnieje, pomijam usuwanie."
    fi
done

echo "Tworzenie nowych miejsc docelowych..."
kafka-topics.sh --bootstrap-server $BROKER --create --topic stock-result --partitions 3 --replication-factor 2
kafka-topics.sh --bootstrap-server $BROKER --create --topic stock-anomalies --partitions 3 --replication-factor 2

echo "Miejsca docelowe zostały przygotowane."