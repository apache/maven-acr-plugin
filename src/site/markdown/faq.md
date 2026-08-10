---
title: Frequently Asked Questions
---

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

<a id="top"></a>

# Frequently Asked Questions

1. [How can I specify a Class-Path: entry in the manifest of an Application Client jar?](#How_can_I_specify_a_Class-Path.3A_entry_in_the_manifest_of_an_Application_Client_jar.3F)
2. [Why the app-client packaging type is not recognized?](#extensions)

### How can I specify a Class-Path: entry in the manifest of an Application Client jar?

You just have to configure it:

```xml
<project>
  ...
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-acr-plugin</artifactId>
        <version>${project.version}</version>
        ...
        <configuration>
          <archive>
            <manifest>
              <addClasspath>true</addClasspath>
            </manifest>
          </archive>
        </configuration>
        ...
      </plugin>
    </plugins>
  </build>
  ...
</project>
```

Please see the [Maven Archiver Reference](/shared/maven-archiver/examples/classpath.html) for more
information about controlling the exact format of the generated class path entries.

<a id="extensions"></a>

### Why the app-client packaging type is not recognized?

Maven does not know about that packaging type so you need to configure your project accordingly as follows:

```xml
<project>
  ...
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-acr-plugin</artifactId>
        <version>${project.version}</version>
        <extensions>true</extensions>
        ...
      </plugin>
    </plugins>
  </build>
  ...
</project>
```
