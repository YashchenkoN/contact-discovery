FROM java:8

VOLUME /tmp

ADD contact-discovery-query.jar contact-discovery-query.jar
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.0.0/wait /wait

RUN chmod +x /wait
RUN bash -c 'touch /contact-discovery-query.jar'

ENTRYPOINT /wait && java -jar /contact-discovery-query.jar