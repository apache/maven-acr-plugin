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

try {
    var jarFile = new File(basedir, "target/maven-acr-plugin-it01-1.0.jar")
    println "Checking for existence of $jarFile"
    if (!jarFile.isFile()) {
        println "FAILURE!"
        return false;
    }

    var jar = new JarFile(jarFile)

    var includedEntries = [
            "META-INF/application-client.xml",
            "org/apache/maven/plugin/acr/it/it01/SampleApp.class"
    ]
    for (String included : includedEntries) {
        println "Checking for existence of $included"
        if (jar.getEntry(included) == null) {
            println "FAILURE!"
            return false
        }
    }

    jar.close()

    // Make sure that the jar has been added with a .jar extension
    var localRepo = new File(basedir.getParentFile().getParentFile(), "local-repo")
    var repoFile = new File(localRepo,
            "/org/apache/maven/plugin/maven-acr-plugin-it01/1.0/maven-acr-plugin-it01-1.0.jar");
    if (!repoFile.exists()) {
        println "Jar file for application client not found in repo [" + repoFile.getAbsolutePath() + "]"
        return false
    }
}
catch (Throwable t) {
    t.printStackTrace()
    return false
}

return true
