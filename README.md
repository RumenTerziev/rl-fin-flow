# RL-Fin-Flow Application

## Description:

> This is a finance API for easy calculating and managing your finances.

##### Feel free to explore the code and give ideas.

### For developers:

> The application depends on `MySQL database` you may either have it installed locally
> or if you have docker installed you may run the following command to run a container for it

```bash
docker run --name mysql-instance -e MYSQL_ROOT_PASSWORD=password -e MYSQL_USER=user -e MYSQL_PASSWORD=password  -e MYSQL_DATABASE=rl-fin-flow_db -d -p 3306:3306 mysql:8
```

##### After starting mysql database you may run the application by running the following commands from the root directory of the project:

```bash
./gradlew clean build
```

- For Windows:

```shell
$env:db_username="user"; $env:db_password="password"; $env:first_user_username="bryan"; $env:first_user_password="4321"; gradle bootRun
```

- For unix like:

```bash
db_username="user" db_password="password" first_user_username="bryan" first_user_password="4321" gradle bootRun
```

>Note that when you run the application with one of these command then you will be able to log in with 
>username: bryan and password: 4321

##### If you want to run the tests:

```bash
./gradlew test
```

> For the tests h2 database is configured.
> For the Integration tests Cucumber framework is configured.

##### Swagger UI
- For default profile -> [Swagger endpoint](http://localhost:8080/api/v1/swagger-ui)
- For docker profile -> [Swagger endpoint](http://localhost:8081/api/v1/swagger-ui)

### About the frontend:

> You may find the frontend of the project here:
> [rl-fin-flow-frontend](https://github.com/RumenTerziev/rl-fin-flow-frontend)

##### If you want to run the frontend and the backend with docker you may run the following command

```bash
docker-compose up
```

> Note that if you run the application with docker compose then the nginx server will be used for the frontend and
> you will find the app on "localhost" and the default port 80.

##### To stop the app:

```bash
docker compose down
```

###### Both frontend and backend applications have continuous integration pipelines configured.