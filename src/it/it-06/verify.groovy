/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

def log = new File(basedir, "build.log").text

def expected = [
    "maven-resources-plugin:3.4.0:resources",
    "maven-compiler-plugin:3.15.0:compile",
    "maven-resources-plugin:3.4.0:testResources",
    "maven-compiler-plugin:3.15.0:testCompile",
    "maven-surefire-plugin:3.5.4:test",
    "maven-install-plugin:3.1.4:install"
]
def missing = expected.findAll { !log.contains("org.apache.maven.plugins:" + it + " ") }
if (!missing.isEmpty()) {
    throw new AssertionError("Default bindings not used: " + missing)
}
