<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

# Apache Maven ACR Plugin

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/apache/maven.svg?label=License)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven.plugins/maven-acr-plugin.svg?label=Maven%20Central)](https://search.maven.org/artifact/org.apache.maven.plugins/maven-acr-plugin)
[![Reproducible Builds](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/jvm-repo-rebuild/reproducible-central/master/content/org/apache/maven/plugins/maven-acr-plugin/badge.json)](https://github.com/jvm-repo-rebuild/reproducible-central/blob/master/content/org/apache/maven/plugins/maven-acr-plugin/README.md)
[![Verify](https://github.com/apache/maven-acr-plugin/actions/workflows/maven-verify.yml/badge.svg)](https://github.com/apache/maven-acr-plugin/actions/workflows/maven-verify.yml)

Builds Jakarta EE application client JARs. The plugin adds the `app-client`
packaging and dependency type, and the `acr:acr` goal that packages them.

- Documentation: <https://maven.apache.org/plugins/maven-acr-plugin/>
- Issues: <https://github.com/apache/maven-acr-plugin/issues>
- Security reports: see [SECURITY.md](SECURITY.md); never a public issue
- Contributing: see [CONTRIBUTING.md](CONTRIBUTING.md)

## Build

```shell
mvn verify                # unit tests
mvn -Prun-its verify      # unit and integration tests
```
