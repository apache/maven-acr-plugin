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
package org.apache.maven.plugins.acr;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.FileSet;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AcrMojoTest {

    @TempDir
    Path tmp;

    @Test
    void executeDoesNotModifyConfiguredExcludes() throws Exception {
        Path classes = Files.createDirectories(tmp.resolve("classes"));
        Files.write(classes.resolve("A.class"), new byte[] {1});

        List<String> excludes = new ArrayList<>(Arrays.asList("**/*DevOnly.class"));
        List<String[]> seen = new ArrayList<>();
        JarArchiver archiver = new JarArchiver() {
            @Override
            public void addFileSet(FileSet fileSet) throws ArchiverException {
                seen.add(fileSet.getExcludes());
            }

            @Override
            public void addFile(File inputFile, String destFileName) {
                // not needed: only the file set is under test
            }
        };

        AcrMojo mojo = new AcrMojo(archiver, null);
        set(mojo, "basedir", tmp.resolve("target").toFile());
        set(mojo, "outputDirectory", classes.toFile());
        set(mojo, "jarName", "test");
        set(mojo, "excludes", excludes);
        MavenProject project = new MavenProject();
        project.setArtifact(new DefaultArtifact("g", "a", "1", null, "jar", null, new DefaultArtifactHandler("jar")));
        project.getBuild().setDirectory(tmp.resolve("target").toString());
        set(mojo, "project", project);
        Files.createDirectories(tmp.resolve("target"));

        mojo.execute();
        mojo.execute();

        assertEquals(Arrays.asList("**/*DevOnly.class"), excludes);
        assertEquals(2, seen.size());
        List<String> expected = Arrays.asList("**/*DevOnly.class", "META-INF/application-client.xml");
        assertEquals(expected, Arrays.asList(seen.get(0)));
        assertEquals(expected, Arrays.asList(seen.get(1)));
    }

    private static void set(Object o, String name, Object value) throws Exception {
        Field f = AcrMojo.class.getDeclaredField(name);
        f.setAccessible(true);
        f.set(o, value);
    }
}
