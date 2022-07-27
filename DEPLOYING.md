### Development with Weaver

* Clone [Weaver-UI-Core](git@github.com:TAMULib/Weaver-UI-Core.git)
* Start docker compose within Weaver-UI-Core directory.

```sh
docker-compose up
```

* Copy the `example.env` file and call it `.env`. These are build args used in docker-compose.yml.
* Copy the `example.env.client` file and call it `.env.client`.
* Copy the `example.env.service` file and call it `.env.service`.
* Change variables as needed.
* Run `docker-compose` commands.

```sh
docker-compose build --no-cache
docker-compose up
```
