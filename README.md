# Spring API with gRPC

## Docker

### Run docker compose
```docker-compose up --build```

## gRPC

### 1. Compile Java classes Proto files
- source: target.generated-sources.protobuf.java.br.com.offtopic.grpc

```protobuf:compile -f pom.xml```

### 2. Compile gRPC-Java (controller/resource) from Proto files
- source: target.generated-sources.protobuf.grpc-java.br.com.offtopic.grpc

```protobuf:compile-custom -f pom.xml```

