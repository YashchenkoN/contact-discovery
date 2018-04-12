FROM java:8

VOLUME /tmp

ADD cd-gateway.jar cd-gateway.jar
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.0.0/wait /wait

RUN chmod +x /wait
RUN bash -c 'touch /cd-gateway.jar'

ENTRYPOINT /wait && java -jar /cd-gateway.jar