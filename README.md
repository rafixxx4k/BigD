# BigD
### 1. Przygotowanie danych
Przed rozpoczęciem przetwarzania przygoduj dane poprzez wysłanie ich do bucketa. Nie zmieniaj nazwy rozpakowanego folderu z danymi, ani nazw plików. Pobrany plik z nazwami spółek giełdowych umieść w katalogu _stocks_symbols_. Wymagana struktura plików:
```
bucket/
└── project/
    ├── stocks_result/
    │   ├── part-00000-<hash>.csv
    │   ├── part-00001-<hash>.csv
    │   ├── ...
    │   └── part-00099-<hash>.csv
    └── stocks_symbols/
        └── symbols_valid_meta.csv
```
### 2. Uruchomienie klastra `Dataproc`
Jest to standardowy klaster uruchamiany na zajęciach. **Jeśli taka konfiguracja nie działa**, zadbaj oto aby mieć odpowiednie uprawienia nadane projektowi, zmienne `CLUSTER_NAME`, `REGION` oraz `PROJECT_ID` zdefiniowane. Czasem pojawia się problem z niektórymi regionami. Można je zmienić np.: `export REGION=europe-central2`.
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
pobierz i rozpakuj skrypty, następnie przejdź do folderu i nadaj im uprawnienia.
```
cd <nazwa-folderu>
chmod +x *.sh
```

### 3. Wykonanie przetwarzania
Stwórz tematy kafki
```
./1.sh
```

Załaduj dane. Argumentem skryptu jest ścierzka do bucketu projektu.
```
./2.sh <gs://psd-25/project>
```
Stwórz miejsca docelowe agregacji (temat kafki 🥺) i temat kafki dla alertów.
```
./5.sh
```
Rozpocznij przetwarzanie poprzez uruchomienie pliku .jar skryptem. Przyjmuje on 3 opcjonalne pozycyjne argumenty 
- **tryb:** "A" _Approximate_ - publikuje dane mogące się później zmienić, "C" _Certain_ - czeka na spóźnione dane do pięciu dni
- **D:** długość okresu wykrywania anomalii wyrażoną w dniach 
- **P:** minimalny stosunek różnicy pomiędzy najwyższym kursem akcji danej spółki a najniższym do najwyższego kursu wyrażony w procentach

Uwaga, przetwarzanie może trochę potrwać.

```
./4.sh
```
lub 
```
./4.sh A 7 40.0
```
### 4. Podgląd wyników
Najlepiej w osobnych terminalach uruchomić skrypty odczytujące wyniki:
```
./6-wyniki.sh
```
```
./6-anomalies.sh
```
