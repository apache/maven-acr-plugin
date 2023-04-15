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
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.input.XmlStreamReader;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.FilterWrapper;
import org.apache.maven.shared.filtering.MavenFileFilter;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Build a JavaEE Application Client jar file from the current project.
 *
 * @author <a href="pablo@anahata-it.com">Pablo Rodriguez</a>
 * @author <a href="snicoll@apache.org">Stephane Nicoll</a>
 */
@Mojo(
        name = "acr",
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true,
        defaultPhase = LifecyclePhase.PACKAGE)
public class AcrMojo extends AbstractMojo {

    private static final String APP_CLIENT_XML = "META-INF/application-client.xml";

    // TODO: will null work instead?
    private static final String[] DEFAULT_INCLUDES = {"**/**"};

    private static final String[] DEFAULT_EXCLUDES = {APP_CLIENT_XML};

    /**
     * The directory for the generated jar.
     */
    @Parameter(defaultValue = "${project.build.directory}", required = true, readonly = true)
    private File basedir;

    /**
     * Directory that resources are copied to during the build.<br/>
     * Starting with <b>3.0.0</b> the property has been renamed from <code>outputDirectory</code> to
     * <code>maven.acr.outputDirectory</code>.
     */
    @Parameter(property = "maven.acr.outputDirectory", defaultValue = "${project.build.outputDirectory}")
    private File outputDirectory;

    /**
     * The name of the Application client JAR file to generate.
     */
    @Parameter(defaultValue = "${project.build.finalName}")
    private String jarName;

    /**
     * The files and directories to exclude from the main Application Client jar. Usage:
     * <p/>
     *
     * <pre>
     * &lt;excludes&gt;
     *   &lt;exclude&gt;**&#47;*DevOnly.class&lt;&#47;exclude&gt;
     * &lt;&#47;excludes&gt;
     * </pre>
     *
     * <br/>
     * Default exclusions: META-INF&#47;application-client.xml,
     */
    @Parameter
    private List<String> excludes;

    /**
     * The Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * The Jar archiver.
     */
    @Component(role = Archiver.class, hint = "jar")
    private JarArchiver jarArchiver;

    /**
     * The archive configuration to use. See <a href="https://maven.apache.org/shared/maven-archiver/index.html">Maven
     * Archiver Reference</a>.
     */
    @Parameter
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * To escape interpolated value with windows path. c:\foo\bar will be replaced with c:\\foo\\bar.<br/>
     * Starting with <b>3.0.0</b> the property has been renamed from <code>acr.escapeBackslashesInFilePath</code> to
     * <code>maven.acr.escapeBackslashesInFilePath</code>.
     */
    @Parameter(property = "maven.acr.escapeBackslashesInFilePath", defaultValue = "false")
    private boolean escapeBackslashesInFilePath;

    /**
     * An expression preceded with this String won't be interpolated. \${foo} will be replaced with ${foo}.<br/>
     * Starting with <b>3.0.0</b> the property has been renamed from <code>acr.escapeString</code> to
     * <code>maven.acr.escapeString</code>.
     */
    @Parameter(property = "maven.acr.escapeString")
    private String escapeString;

    /**
     * To filter the deployment descriptor. Starting with <b>3.0.0</b> the property has been renamed from
     * <code>acr.filterDeploymentDescriptor</code> to <code>maven.acr.filterDeploymentDescriptor</code>.
     */
    @Parameter(property = "maven.acr.filterDeploymentDescriptor", defaultValue = "false")
    private boolean filterDeploymentDescriptor;

    /**
     * Filters (properties files) to include during the interpolation of the deployment descriptor.
     */
    @Parameter
    private List<String> filters;

    /**
     */
    @Component(role = MavenFileFilter.class, hint = "default")
    private MavenFileFilter mavenFileFilter;

    /**
     */
    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    /**
     * Timestamp for reproducible output archive entries, either formatted as ISO 8601
     * <code>yyyy-MM-dd'T'HH:mm:ssXXX</code> or as an int representing seconds since the epoch (like
     * <a href="https://reproducible-builds.org/docs/source-date-epoch/">SOURCE_DATE_EPOCH</a>).
     *
     * @since 3.2.0
     */
    @Parameter(defaultValue = "${project.build.outputTimestamp}")
    private String outputTimestamp;

    /** {@inheritDoc} */
    public void execute() throws MojoExecutionException {
        if (getLog().isInfoEnabled()) {
            getLog().info("Building JavaEE Application client: " + jarName);
        }

        File jarFile = getAppClientJarFile(basedir, jarName);

        MavenArchiver archiver = new MavenArchiver();

        archiver.setArchiver(jarArchiver);

        archiver.setCreatedBy("Maven ACR Plugin", "org.apache.maven.plugins", "maven-acr-plugin");

        archiver.setOutputFile(jarFile);

        // configure for Reproducible Builds based on outputTimestamp value
        archiver.configureReproducible(outputTimestamp);

        try {
            String[] mainJarExcludes = DEFAULT_EXCLUDES;

            if (excludes != null && !excludes.isEmpty()) {
                excludes.add(APP_CLIENT_XML);
                mainJarExcludes = excludes.toArray(new String[0]);
            }

            if (outputDirectory.exists()) {
                archiver.getArchiver().addDirectory(outputDirectory, DEFAULT_INCLUDES, mainJarExcludes);
            } else {
                // CHECKSTYLE_OFF: LineLength
                getLog().info(
                                "JAR will only contain the META-INF/application-client.xml as no content was marked for inclusion");
                // CHECKSTYLE_ON: LineLength
            }

            File deploymentDescriptor = new File(outputDirectory, APP_CLIENT_XML);

            if (deploymentDescriptor.exists()) {
                if (filterDeploymentDescriptor) {
                    getLog().debug("Filtering deployment descriptor.");
                    MavenResourcesExecution mavenResourcesExecution = new MavenResourcesExecution();
                    mavenResourcesExecution.setEscapeString(escapeString);
                    List<FilterWrapper> filterWrappers = mavenFileFilter.getDefaultFilterWrappers(
                            project, filters, escapeBackslashesInFilePath, this.session, mavenResourcesExecution);

                    // Create a temporary file that we can copy-and-filter
                    File unfilteredDeploymentDescriptor = new File(outputDirectory, APP_CLIENT_XML + ".unfiltered");
                    FileUtils.copyFile(deploymentDescriptor, unfilteredDeploymentDescriptor);
                    mavenFileFilter.copyFile(
                            unfilteredDeploymentDescriptor,
                            deploymentDescriptor,
                            true,
                            filterWrappers,
                            getEncoding(unfilteredDeploymentDescriptor));
                    // Remove the temporary file
                    FileUtils.forceDelete(unfilteredDeploymentDescriptor);
                }
                archiver.getArchiver().addFile(deploymentDescriptor, APP_CLIENT_XML);
            }

            // create archive
            archiver.createArchive(session, project, archive);
            // CHECKSTYLE_OFF: LineLength
        } catch (ArchiverException e) {
            throw new MojoExecutionException(
                    "There was a problem creating the JavaEE Application Client  archive: " + e.getMessage(), e);
        } catch (ManifestException e) {
            throw new MojoExecutionException(
                    "There was a problem reading / creating the manifest for the JavaEE Application Client  archive: "
                            + e.getMessage(),
                    e);
        } catch (IOException e) {
            throw new MojoExecutionException(
                    "There was a I/O problem creating the JavaEE Application Client archive: " + e.getMessage(), e);
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException(
                    "There was a problem resolving dependencies while creating the JavaEE Application Client archive: "
                            + e.getMessage(),
                    e);
        } catch (MavenFilteringException e) {
            throw new MojoExecutionException(
                    "There was a problem filtering the deployment descriptor: " + e.getMessage(), e);
        }

        project.getArtifact().setFile(jarFile);

        // CHECKSTYLE_ON: LineLength
    }

    /**
     * Returns the App-client Jar file to generate.
     *
     * @param basedir the output directory
     * @param finalName the name of the ear file
     * @return the Application client JAR file to generate
     */
    private static File getAppClientJarFile(File basedir, String finalName) {
        return new File(basedir, finalName + ".jar");
    }

    /**
     * Get the encoding from an XML-file.
     *
     * @param xmlFile the XML-file
     * @return The encoding of the XML-file, or UTF-8 if it's not specified in the file
     * @throws IOException if an error occurred while reading the file
     */
    private String getEncoding(File xmlFile) throws IOException {
        try (XmlStreamReader xmlReader = new XmlStreamReader(xmlFile)) {
            return xmlReader.getEncoding();
        }
    }
}
