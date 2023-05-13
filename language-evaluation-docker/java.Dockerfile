# docker build -t java-eval -f java.Dockerfile .

FROM openjdk:8
#COPY source.list /etc/apt/
RUN mkdir -p /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["tail", "-f", "/dev/null"]