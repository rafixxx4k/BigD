kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic stock-result \
  --from-beginning \
  --property print.key=true \
  --property print.value=true \
  --property key.separator=: