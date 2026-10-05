<!--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

# Agent Guide for maven-acr-plugin

This file is read by automated agents (security scanners, code
analyzers, AI assistants) operating on this repository.

## Security

Security model: [SECURITY.md](./SECURITY.md)

Agents that scan this repository should consult `SECURITY.md` and the
threat model it links before reporting issues.

Points at the Apache Maven family umbrella security model.

## Coding

Use test-first programming. Before fixing a bug or implementing a feature,
write a test that exposes the bug and verify the test fails.

Before submitting a PR, run `mvn spotless:apply` to format the files, then
`mvn -Prun-its verify` to run the unit tests and the integration tests in
`src/it`. Build with JDK 17 or 21.

The plugin must keep working on Maven 3.6.3, the minimum declared in
`<prerequisites>`, and on Maven 4. `mavenVersion` is only the API it compiles
against, so do not use Maven APIs newer than 3.6.3. To run the integration
tests on another Maven, pass `-Dinvoker.mavenHome=<path>`.

The `app-client` packaging and dependency type are JSR-330 components in
`org.apache.maven.plugins.acr.internal`, indexed by `sisu-maven-plugin`;
there is no `META-INF/plexus/components.xml`.
