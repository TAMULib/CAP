<a name="readme-top"></a>
[![Build Status][build-badge]][build-status]
[![Coverage Status][coverage-badge]][coverage-status]
[![Performance][performance-badge]][performance-status]
[![Accessibility][accessibility-badge]][accessibility-status]
[![Best Practices][best_practices-badge]][best_practices-status]
[![SEO][seo-badge]][seo-status]
[![Progressive Web App][pwa-badge]][pwa-status]


# Curator's Administration Platform

*Curator's Administration Platform (CAP)*, consisting of a service back-end and a client front-end, is developed and maintained by [Texas A&M University Libraries][tamu-library].

*CAP* is an open source application that has been designed to provide a common user interface (UI) and application programing interface (API) for any *Institutional Repository (IR)* participating within a *Digital Asset Management Ecosystem (DAME)*.

Currently *CAP* has support for **Fedora 4x** through use of the [fcrepo-java-client][fcrepo_java_client-url].
Future work will add support for **Fedora 5x**, and there has been some preliminary work to support **DSpace 6x**.

<div align="right">(<a href="#readme-top">back to top</a>)</div>


## User Documentation

*CAP* user documentation can be found in [the wiki][user-docs].

For more technical users, deployment related configurations are described in the [Deployment Guide][deployment-guide].

<div align="right">(<a href="#readme-top">back to top</a>)</div>


## Deployment

A quick and easy deployment method using `docker-compose` is described in the [Deployment Guide][deployment-guide].

For _advanced use cases_, or when `docker-compose` is unavailable, the use of `docker` or `npm`/`mvn` is also described in the [Deployment Guide][deployment-guide].

Deployment, in general, may look something like this:

```shell
cp example.env .env
cp example.env.client .env.client
cp example.env.service .env.service

# Make any changes to the .env, .env.client, and .env.service files before here.
docker-compose up
```

<sub>_* Note: It may be necessary to disable caching during build by passing `--no-cache` to the `docker-compose up` command._</sub>

<div align="right">(<a href="#readme-top">back to top</a>)</div>


## Additional Resources

- [Contributors Documentation][contribute-guide]
- [Deployment Documentation][deployment-guide]
- [API Documentation][api-docs]

Please feel free to file any issues concerning *CAP* to the issues section of the repository.

Any questions concerning *CAP* can be directed to helpdesk@library.tamu.edu.

Copyright © 2022 Texas A&M University Libraries under the [MIT License][license].

<div align="right">(<a href="#readme-top">back to top</a>)</div>


<!-- LINKS -->
[build-status]: https://github.com/TAMULib/CAP/actions?query=workflow%3ABuild
[build-badge]: https://github.com/TAMULib/CAP/workflows/Build/badge.svg
[coverage-status]: https://coveralls.io/github/TAMULib/CAP
[coverage-badge]: https://coveralls.io/repos/github/TAMULib/CAP/badge.svg
[performance-status]: https://tamulib.github.io/CAP/audit/#performance
[performance-badge]: https://tamulib.github.io/CAP/audit/assets/performance.svg
[accessibility-status]: https://tamulib.github.io/CAP/audit/#accessibility
[accessibility-badge]: https://tamulib.github.io/CAP/audit/assets/accessibility.svg
[best_practices-status]: https://tamulib.github.io/CAP/audit/#best-practices
[best_practices-badge]: https://tamulib.github.io/CAP/audit/assets/best-practices.svg
[seo-status]: https://tamulib.github.io/CAP/audit/#seo
[seo-badge]: https://tamulib.github.io/CAP/audit/assets/seo.svg
[pwa-status]: https://tamulib.github.io/CAP/audit/#pwa
[pwa-badge]: https://tamulib.github.io/CAP/audit/assets/pwa.svg

[tamu-library]: http://library.tamu.edu
[api-docs]: https://tamulib.github.io/CAP
[user-docs]: https://github.com/TAMULib/CAP/wiki
[fcrepo_java_client-url]: https://github.com/fcrepo4-exts/fcrepo-java-client

[deployment-guide]: DEPLOYING.md
[contribute-guide]: CONTRIBUTING.md
[license]: LICENSE
