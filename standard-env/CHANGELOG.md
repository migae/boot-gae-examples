# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
### Added

### Changed
- move content of web.xml.edn to webapp.edn
- delete web.xml.edn
- :gae map from build.boot moved to appengine.edn
- remove microservices-app, apps now assembled in default module (here: coordinator)

## [0.1.0]
### Added

### Changed
- sync with boot-gae 0.1.0
-- use relative path in :gae :app :dir
- remove unused files/dirs
- example projects all use 'tmp as groupid, 0.1.0-SNAPSHOT as version
- fix :repositories to use conj
- delete pointless comment lines
- internal javac task: add :options ["-target" "1.7"]


[Unreleased]: https://github.com/migae/boot-gae-example/tree/master
[0.1.0]: https://github.com/migae/boot-gae-example/tree/master
