FROM openjdk:8
#COPY source.list /etc/apt/
RUN mkdir -p /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["tail", "-f", "/dev/null"]