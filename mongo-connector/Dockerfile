FROM python:3.4.3
MAINTAINER vkont4@github.com

VOLUME /data

ENV DEBIAN_FRONTEND noninteractive

COPY startup.sh /tmp/

ADD https://github.com/weflex/docker-mongo-connector/raw/master/mongo /usr/bin/
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.0.0/wait /wait

RUN chmod a+x /usr/bin/mongo
RUN chmod +x /wait
RUN pip install elastic2-doc-manager[elastic5]
RUN pip install mongo-connector[elastic5]

CMD ["/bin/bash", "/tmp/startup.sh"]

ENTRYPOINT /wait && /bin/bash /tmp/startup.sh