FROM java:8

VOLUME /tmp

ADD contact-discovery-gateway.jar contact-discovery-gateway.jar
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.0.0/wait /wait

RUN chmod +x /wait
RUN bash -c 'touch /contact-discovery-gateway.jar'

ENTRYPOINT /wait && java -jar /contact-discovery-gateway.jar