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
package org.apache.maven.plugins.acr.internal;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.lifecycle.mapping.Lifecycle;
import org.apache.maven.lifecycle.mapping.LifecycleMapping;
import org.apache.maven.lifecycle.mapping.LifecyclePhase;

@Singleton
@Named("app-client")
public final class AppClientLifecycleMappingProvider implements Provider<LifecycleMapping> {
    // The maven-acr-plugin binding carries no version: this mapping is only loaded when the project
    // declares the plugin, so the version always comes from the project's effective POM.
    private static final String[] BINDINGS = {
        "process-resources", "org.apache.maven.plugins:maven-resources-plugin:2.7:resources",
        "compile", "org.apache.maven.plugins:maven-compiler-plugin:3.5.1:compile",
        "process-test-resources", "org.apache.maven.plugins:maven-resources-plugin:2.7:testResources",
        "test-compile", "org.apache.maven.plugins:maven-compiler-plugin:3.5.1:testCompile",
        "test", "org.apache.maven.plugins:maven-surefire-plugin:2.18.1:test",
        "package", "org.apache.maven.plugins:maven-acr-plugin:acr",
        "install", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install",
        "deploy", "org.apache.maven.plugins:maven-deploy-plugin:2.8.2:deploy"
    };

    private final LifecycleMapping lifecycleMapping;

    public AppClientLifecycleMappingProvider() {
        Map<String, LifecyclePhase> lifecyclePhases = new LinkedHashMap<>();
        for (int i = 0; i < BINDINGS.length; i += 2) {
            lifecyclePhases.put(BINDINGS[i], new LifecyclePhase(BINDINGS[i + 1]));
        }
        Lifecycle defaultLifecycle = new Lifecycle();
        defaultLifecycle.setId("default");
        defaultLifecycle.setLifecyclePhases(lifecyclePhases);

        this.lifecycleMapping = new LifecycleMapping() {
            @Override
            public Map<String, Lifecycle> getLifecycles() {
                return Collections.singletonMap("default", defaultLifecycle);
            }

            @Override
            public List<String> getOptionalMojos(String lifecycle) {
                return null;
            }

            @Override
            public Map<String, String> getPhases(String lifecycle) {
                if (!"default".equals(lifecycle)) {
                    return null;
                }
                Map<String, String> phases = new LinkedHashMap<>();
                for (Map.Entry<String, LifecyclePhase> entry : lifecyclePhases.entrySet()) {
                    phases.put(entry.getKey(), entry.getValue().toString());
                }
                return phases;
            }
        };
    }

    @Override
    public LifecycleMapping get() {
        return lifecycleMapping;
    }
}
