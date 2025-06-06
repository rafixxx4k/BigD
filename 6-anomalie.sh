kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic stock-anomalies \
  --from-beginning \
  --property print.key=true \
  --property print.value=true \
  --property key.separator=: