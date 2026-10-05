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

# Contributing

Bug reports, fixes and new features are welcome. To keep reviews quick:

- Discuss a new feature on the [dev mailing list](https://maven.apache.org/mailing-lists.html)
  first, so you know it fits the project before you write it.
- Report bugs in [GitHub Issues](https://github.com/apache/maven-acr-plugin/issues),
  with the earliest affected version and steps to reproduce. Report security
  issues privately, as [SECURITY.md](SECURITY.md) describes.
- Open pull requests from a topic branch, in commits of logical units.
- Add or update tests for your change, run `mvn spotless:apply` to format it,
  and run `mvn -Prun-its verify` before you push.
- Keep the diff minimal: leave unrelated formatting and import order alone,
  and send a reformat as its own pull request.

If you plan to contribute regularly, file an
[Apache Individual Contributor License Agreement](https://www.apache.org/licenses/contributor-agreements.html#clas).

More: [Maven developer guide](https://maven.apache.org/guides/development/guide-maven-development.html),
[code conventions](https://maven.apache.org/developers/conventions/code.html).
