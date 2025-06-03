# BigD
uruchom klaster `Dataproc`
```bash
gcloud dataproc clusters create ${CLUSTER_NAME} \
--enable-component-gateway --region ${REGION} --subnet default \
--master-machine-type n1-standard-4 --master-boot-disk-size 50 \
--num-workers 2 --worker-machine-type n1-standard-2 --worker-boot-disk-size 50 \
--image-version 2.1-debian11 --optional-components DOCKER,ZOOKEEPER \
--project ${PROJECT_ID} --max-age=2h \
--metadata "run-on-master=true" \
--initialization-actions \
gs://goog-dataproc-initialization-actions-${REGION}/kafka/kafka.sh
```

rozpakuj skrypty
przejdź do folderu
nadaj im uprawnienia

git clone https://github.com/rafixxx4k/BigD.git
```
chmod +x *.sh
```


stwórz tematy kafki
```
./1.sh
```

załaduj dane
```
./2.sh <gs://psd-25/project>
./2.sh gs://pbd-24-rs/project
```
uruchom przetwarzanie (zamiana klasy main)
```bash
sudo cp /usr/lib/kafka/config/tools-log4j.properties \
 /usr/lib/kafka/

echo "log4j.logger.org.reflections=ERROR" | \
 sudo tee -a /usr/lib/kafka/config/connect-log4j.properties


CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)

java -cp /usr/lib/kafka/libs/*:stock-market.jar \
 com.example.bigdata.ApacheLogToAlertRequests ${CLUSTER_NAME}-w-0:9092


```
rm -rf /tmp/kafka-streams/alert-requests-application


/bin/kafka-streams-application-reset \
  --application-id alert-requests-application \
  --bootstrap-servers localhost:9092 \
  --input-topics stock-symbols,stock-input