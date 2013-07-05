/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.jbehave.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.jbehave.container.JBehaveContainerExtension;
import org.jboss.arquillian.jbehave.core.ArquillianInstanceStepsFactory;
import org.jboss.arquillian.junit.ArquillianJbehaveRunner;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;


/**
 * Deployment appender that adds the JBehave-Core distribution to a deployment.
 * Also adds the classes and files necessary for the remote loadable extension.
 * 
 * 
 */
public class JBehaveCoreDeploymentAppender implements AuxiliaryArchiveAppender {

	@Override
	public Archive<?> createAuxiliaryArchive() {
		Collection<JavaArchive> archives = new ArrayList<>();
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, new File(
				"../lib/bdd/jbehave-core-3.7.5-SNAPSHOT.jar")));
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../lib/bdd/xstream-1.3.1.jar")));
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../lib/bdd/paranamer-2.4.jar")));
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, new File(
				"../lib/javassist/javassist-3.15.0-GA.jar")));
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../lib/bdd/freemarker-2.3.16.jar")));
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../lib/bdd/plexus-utils-2.0.5.jar")));
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class,
				new File("../lib/bdd/hamcrest-integration-1.1.jar")));
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../lib/bdd/hamcrest-core-1.1.jar")));
		archives.add(ShrinkWrap.createFromZipFile(JavaArchive.class, new File("../lib/bdd/xpp3_min-1.1.4c.jar")));

		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "arquillian-jbehave.jar");
		Filter<ArchivePath> filter = Filters.exclude("/META-INF.*");
		for (Archive<JavaArchive> element : archives) {
			archive.merge(element, filter);
		}

		archive.addPackage("com.capgemini.javassist").addPackage("de.codecentric.jbehave.junit.monitoring")
				.addClass(ArquillianJbehaveRunner.class).addPackage("com.capgemini.arquillian.utils")
				.addPackage("com.capgemini.arquillian.jbehave.injection")
				.addPackage("com.capgemini.arquillian.jbehave.container").addPackages(true, "com.capgemini.jbehave");
		archive.addAsServiceProvider(RemoteLoadableExtension.class, JBehaveContainerExtension.class);
		return archive;
	}

}
