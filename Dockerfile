FROM strimzi/kafka:0.17.0-rc1-kafka-2.4.0
ARG JAR_FILE
USER root:root
COPY target/${JAR_FILE} /opt/kafka/plugins/
USER 1001
