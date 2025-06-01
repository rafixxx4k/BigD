package com.example.stocketl;

import com.example.stocketl.model.SymbolMetaRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Properties;

public class StockETLApp {
    public static void main(String[] args) {
        // Konfiguracja Kafka Streams
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "symbol-printer-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Budowa topologii
        StreamsBuilder builder = new StreamsBuilder();

        // Konsumowanie tematu stock-symbols
        KTable<String, String> symbolTable = builder.table("stock-symbols");

        // Parsowanie i wypisywanie na konsolę
        symbolTable.toStream()
                .mapValues(SymbolMetaRecord::parseFromCsvLine)
                .peek((key, value) -> System.out.println(">> SymbolMetaRecord: " + value));

        // Start Kafka Streams
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        // Zamknięcie przy Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
