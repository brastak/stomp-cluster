## How to run

### Pre-requisites

1. Java 17
2. Gradle 8
3. Docker
4. K8S (Docker Desktop used in this README)

### Prepare RabbitMQ

We need enable STOMP plugin in RabbitMQ server. It is disabled by default in official docker images,
so we will extend the standard image:

```bash
docker build . \
  -f rabbitmq-stomp/Dockerfile \
  -t rabbitmq:3.12-stomp

docker run --name stomp-cluster-rabbitmq \
  -d \
  -p 5672:5672 \
  -p 15672:15672 \
  -p 61613:61613 rabbitmq:3.12-stomp
```

### Run generator

In separate terminal (or within your IDE) run generator application:

```bash
gradle generator:bootRun
```

### Build docker image
```bash
cd backend

docker build . \
  -f docker/Dockerfile \
  -t stomp-cluster-backend:1.0
  
cd -
```

### Deploy K8S service

```bash
kubectl apply -f k8s/deployment.yaml

kubectl apply -f k8s/service.yaml
```

### Get K8S service node port

```bash
kubectl get svc stomp-cluster-backend-service \
  -o=jsonpath='{.spec.ports[].nodePort}'
```

### Prepare DB

```bash
docker run --name stomp-cluster-db \
  -d \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres
```

### Run client application

Use node port to run client with backend URL (if you run it from IDE you should fix 
`client/src/main/resources/application.yaml` file):

```bash
gradle client:bootRun -Dapplication.backend.url=ws://localhost:<port>/ws
```

### Verify results

Connect to PostgreSQL database and examine `message` table to verify results:

```sql
select subscriber, count(*) from message m group by subscriber order by count
```

