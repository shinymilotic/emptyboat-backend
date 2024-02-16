FROM container-registry.oracle.com/graalvm/native-image:21
MAINTAINER overcloud
COPY target/blog-1.jar blog-1.jar
ENTRYPOINT ["java","-jar","blog-1.jar"]
