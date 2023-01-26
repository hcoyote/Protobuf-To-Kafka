# Protobuf-To-Kafka using Lenses Box

Note: This has been modified a bit from the original in order to work on ARM-based Macs. In the original repo, this uses the os72 maven plugin for protobuf. It's been switched here in order to use a protobuf release that's more recent and supports non-intel architectures.


Install protobuf from homebrew;

1. ## brew install protobuf

Set things up by running:

1. ## mvn generate-sources
2. ## mvn compile
3. ## mvn package

---

Once you have Lenses box up and running execute:

## java -Dlog4j.configuration=file:`pwd`/log4j.properties  -cp target/kafka-send-proto-0.1.0.jar  kafka.SendKafkaProto

---

If facing unkwown host issue on MacOS try editing your local hosts by running:
**sudo vim /etc/hosts**

and matching 127.0.0.1 to the host ip address.

--- 

