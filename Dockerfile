FROM 10.131.9.12:5000/base/jdk8-redis-nginx-apm:1.0.0
WORKDIR /work
ADD ./target/boot001-1.0.0.jar ./boot001.jar

#ADD nginx.conf /etc/nginx/nginx.conf

ADD run.sh .
RUN chmod +x ./run.sh
CMD ["./run.sh"]

