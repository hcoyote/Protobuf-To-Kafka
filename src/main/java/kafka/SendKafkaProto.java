package kafka;

import com.github.javafaker.Faker;
import static com.google.protobuf.util.Timestamps.fromMillis;
import static java.lang.System.currentTimeMillis;
import com.google.protobuf.Timestamp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.Random;

// import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
// import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer;
// import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializerConfig;
import com.example.CardData;

import java.util.Properties;
// import java.util.concurrent.ExecutionException;

public class SendKafkaProto {
  public static void main(String[] args) {

	Logger logger = LoggerFactory.getLogger(SendKafkaProto.class);
        logger.info("This is how you configure Log4J with SLF4J");

    String bootstrapServers = "127.0.0.1:9092";
    var properties = new Properties();

    properties.setProperty("bootstrap.servers", bootstrapServers);
    properties.setProperty("schema.registry.url", "http://localhost:8081");

    properties.setProperty("key.serializer", StringSerializer.class.getName());
    properties.setProperty("value.serializer", KafkaProtobufSerializer.class.getName());

    properties.setProperty("skip.known.types", "true");
    properties.setProperty("auto.register.schemas", "true");

    KafkaProducer<String, CardData.CreditCard> producer = new KafkaProducer<>(properties);
    // Specify Topic
    var topic = "protos_topic_cards";

    for (int i = 0; i < 10; i++) {

      //
      // Construct Fake Object
      //
      Random rd = new Random(); // creating Random object
      Faker faker = new Faker();

      String name = faker.name().fullName();
      String countryCode = faker.address().countryCode();

      String cardNumber = faker.business().creditCardNumber();
      Integer typeValue = rd.nextInt(3);
      String currencyCode = faker.country().currencyCode();
      Timestamp issued = fromMillis(currentTimeMillis());


      var cardData = CardData.CreditCard.newBuilder()
          .setName(name)
          .setCountry(countryCode)
          .setCurrency(currencyCode)
          .setTypeValue(typeValue)
          .setBlocked(false)
          .setCardNumber(cardNumber)
	  .setIssued(issued)
          .build();

      Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
      logger.debug(gson.toJson(cardData));

      var record = new ProducerRecord<String, CardData.CreditCard>(topic, "Credit Card", cardData);

      logger.debug(gson.toJson(record));

      producer.send(record);
    }
    producer.flush();
    producer.close();
    System.out.println("Sent Data Successfully");
  }
}
