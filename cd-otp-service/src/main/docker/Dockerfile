FROM java:8

VOLUME /tmp

ADD cd-otp-service.jar cd-otp-service.jar
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.0.0/wait /wait

RUN chmod +x /wait
RUN bash -c 'touch /cd-otp-service.jar'

ENTRYPOINT /wait && java -jar /cd-otp-service.jar