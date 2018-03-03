FROM mongo:3.7.2

MAINTAINER <Mykola Yashchenko> vkont4@gmail.com

EXPOSE 27017 27017

ADD run.sh /run.sh
ADD set_mongodb_password.sh /set_mongodb_password.sh

RUN chmod +x /run.sh
RUN chmod +x /set_mongodb_password.sh

CMD ["/run.sh"]