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

import java.util.jar.JarFile

var file = new File(basedir, "target/maven-acr-plugin-it04-1.0.jar")
println "Checking for existence of $file"
assert file.isFile() : "FAILURE! Missing JAR file"

var jarFile = new JarFile(file)

var zipEntry = jarFile.getEntry("META-INF/application-client.xml")
assert zipEntry != null : "FAILURE! Missing META-INF/application-client.xml in the JAR file"
var is = jarFile.getInputStream(zipEntry)
var contents = is.getText('UTF-8')
var index = contents.indexOf("it04.key.value")
assert index < 0 : "FAILURE! The key 'it04.key.value' has not been replaced during filtering"
index = contents.indexOf("it04-success")
assert index >= 0 : "FAILURE! The value 'it04-success' has not been injected during filtering"
is.close()

jarFile.close()
