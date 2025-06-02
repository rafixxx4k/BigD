package com.example.bigdata;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.text.ParseException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ApacheLogToAlertRequests {

    public static void main(String[] args) throws Exception {
        Properties config = new Properties();
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, args[0]);
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "alert-requests-application");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

//        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, ???);

        final Serde<String> stringSerde = Serdes.String();
        final StreamsBuilder builder = new StreamsBuilder();

        // Przeczytaj strumień z topicu stock-input (CSV jako String -> StockRecord)
        KStream<String, String> stockRawStream = builder.stream("stock-input", Consumed.with(stringSerde, stringSerde));
        KStream<String, StockRecord> stockStream = stockRawStream
                .mapValues(value -> {
                    try {
                        return StockRecord.parseFromCSVLine(value);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .selectKey((key, value) -> value.getStockSymbol());

        // Przeczytaj tablicę z topicu stock-symbols (CSV jako String -> SymbolRecord)
        KTable<String, SymbolRecord> symbolTable = builder.table("stock-symbols",
                        Consumed.with(stringSerde, stringSerde))
                .mapValues(SymbolRecord::parseFromCSVLine);

        // Połącz strumienie (join)
        KStream<String, StockSymbolRecord> joinedStream = stockStream.join(
                symbolTable,
                (stock, symbol) -> new StockSymbolRecord(stock, symbol)
        );

        // Filtrowanie: tylko symbol == "A"
        KStream<String, StockSymbolRecord> filteredStream = joinedStream
                .filter((key, value) -> "A".equalsIgnoreCase(value.getStockRecord().getStockSymbol()));

        // Wyświetl na konsoli
        filteredStream.foreach((key, value) -> System.out.println("JOINED: " + value));

        final Topology topology = builder.build();
        System.out.println(topology.describe());

        KafkaStreams streams = new KafkaStreams(topology, config);

        final CountDownLatch latch = new CountDownLatch(1);

//        KStream<String, String> textLines = builder
//                .stream("???", Consumed.with(stringSerde, stringSerde));
//
//        KStream<String, AccessLogRecord> apacheLogStream = textLines
//                .filter((key, value) -> AccessLogRecord.lineIsCorrect(value))
//                .mapValues(value -> ???);
//
//        KTable<Windowed<String>, Long> ipCounts = apacheLogStream
//                .map((key, value) -> KeyValue.pair(???, ""))
//                .groupByKey()
//                .windowedBy(TimeWindows.of(Duration.???(10)) /* time-based window */)
//                .count();
//
//        KTable<String, String> difficultIps = ipCounts.toStream()
//                .filter((key, value) -> value > ???)
//                .map((key, value) -> KeyValue.pair(key.key(), String.valueOf(value)))
//                .groupByKey()
//                .reduce((aggValue, newValue) -> newValue,
//                        Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("???"));
//
//        KStream<String, String> keyIpValueEndpoint = apacheLogStream
//                .map((key, value) -> KeyValue.pair(???, value.getEndpoint()));
//
//        keyIpValueEndpoint
//                . ???(???, (enpoint, howmany) -> enpoint + "," + howmany)
//                .to("???");
//
//        final Topology topology = builder.build();
//        System.out.println(topology.describe());
//
//        KafkaStreams streams = new KafkaStreams(topology, config);
//
//        final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);

    }
}

