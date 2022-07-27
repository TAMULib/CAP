### Development with Weaver

* Clone [Weaver-UI-Core](git@github.com:TAMULib/Weaver-UI-Core.git)
* Start docker compose within Weaver-UI-Core directory.

```sh
docker-compose up
```

* Copy the `example.env` file and call it `.env`. These are build args and container environment variables used in docker-compose.yml.
* Change variables as needed.
* Run `docker-compose` commands.

```sh
docker-compose build --no-cache
docker-compose up
```
