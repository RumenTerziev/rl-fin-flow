# RL-Fin-Flow Application

## Description:

> This is a finance application for easy calculating and managing your finances.

##### Feel free to explore the code and give ideas.

#### For developers:

> The application depends on `MySQL database` you may either have it installed locally
> or if you have docker installed you may run the following command to run a container for it

```bash
docker run --name mysql-instance -e MYSQL_ROOT_PASSWORD=password -e MYSQL_USER=user -e MYSQL_PASSWORD=password -e MYSQL_DATABASE=rl-fin-flow_db -d mysql:8
```

##### After starting mysql you may run the application by running the following commands from the root directory of the project:

```bash
./gradlew clean build
```

```bash
./gradlew bootRun
```
