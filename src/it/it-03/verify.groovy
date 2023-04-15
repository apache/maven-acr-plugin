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

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

var jarFile = new File(basedir, "target/maven-acr-plugin-it03-1.0.jar")
println "Checking for existence of $jarFile"
if (!jarFile.isFile()) {
    throw new FileNotFoundException("Missing " + jarFile)
}

var zis = new ZipInputStream(new FileInputStream(jarFile))

int count = 0

while (true) {
    var entry = zis.getNextEntry()
    if (entry == null) {
        break
    }
    if (entry.getName().equals("META-INF/application-client.xml")) {
        count++
    }
}

zis.close()

if (count != 1) {
    throw new IllegalStateException("Wrong number of application-client.xml's in archive: " + count)
}

return true
