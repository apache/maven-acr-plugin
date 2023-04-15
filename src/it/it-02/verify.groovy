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
    var jarFile = new File(basedir, "app-client/target/maven-acr-plugin-it02-app-client-1.0.jar")
    println "Checking for existence of $jarFile"
    if (!jarFile.isFile()) {
        println "FAILURE!"
        return false
    }

    var jar = new JarFile(jarFile)

    var includedEntries = [
        "META-INF/application-client.xml",
        "org/apache/maven/plugin/acr/it/it02/SampleApp.class"
    ]
    for (String included : includedEntries) {
        println "Checking for existence of $included"
        if (jar.getEntry(included) == null) {
            println "FAILURE!"
            return false
        }
    }

    jar.close()

    var bundleJarFile = new File(basedir, "bundle/target/maven-acr-plugin-it02-bundle-1.0.jar")
    var bundle = new JarFile(bundleJarFile);


    var includedBundleEntries = [
        "org/apache/maven/plugin/acr/it/it02/DependencyTest.class"
    ]
    for (String included : includedBundleEntries) {
        println "Checking for existence of $included"
        if (bundle.getEntry(included) == null) {
            println "FAILURE!"
            return false
        }
    }

    bundle.close()


}
catch (Throwable t) {
    t.printStackTrace()
    return false
}

return true
