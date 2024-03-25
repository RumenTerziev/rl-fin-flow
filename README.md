# RL-Fin-Flow Application

## Description:

> This is a finance application for easy calculating and managing finance numbers and calculating the revenue of a
> particular currency.

##### Feel free to explore the code and give ideas.

#### For developers:

> You can clone the repository and run the application locally.
> You may open the project with [Intellij Idea](https://www.jetbrains.com/idea/download/?section=windows)
> The application depends on `MySQL database` you may either have it installed locally
> by [downloading](https://dev.mysql.com/downloads/installer/) or if you have docker installed or if you want
> to [download](https://www.docker.com/products/docker-desktop/) it you may run the following command to run a container

```bash
docker run --name mysql-instance -e MYSQL_ROOT_PASSWORD=password -e MYSQL_USER=user -e MYSQL_PASSWORD=password -e MYSQL_DATABASE=rl-fin-flow_db -d mysql:8
```

##### After starting mysql you may run the application from Intellij with the run button or by running the following commands from the root directory of the project:

```bash
./gradlew clean build
```

```bash
./gradlew bootRun
```