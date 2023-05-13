FROM openjdk:11
#COPY source.list /etc/apt/
RUN curl -s https://get.sdkman.io | bash \
    && bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install kotlin"
RUN mkdir -p /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["tail", "-f", "/dev/null"]