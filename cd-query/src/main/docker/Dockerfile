FROM java:8

VOLUME /tmp

ADD cd-query.jar cd-query.jar
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.0.0/wait /wait

RUN chmod +x /wait
RUN bash -c 'touch /cd-query.jar'

ENTRYPOINT /wait && java -jar /cd-query.jar