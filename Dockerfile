FROM adoptopenjdk/openjdk11:ubi

# Maintainer
LABEL maintainer="Nelson M.O >>CMA Project"
LABEL maintainer="Nelson62moses@gmail.com"
WORKDIR /CMA_PROJECT

#RUN sh -c 'touch logs/poslogs/poslog.log'
ENV TZ=Africa/Nairobi
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY /target/cma-project-1.0.jar cma-project-1.0.jar
COPY config config
EXPOSE 9099
#COPY truststore.jks truststore.jks
CMD ["java", "-jar", "cma-project-1.0.jar"]