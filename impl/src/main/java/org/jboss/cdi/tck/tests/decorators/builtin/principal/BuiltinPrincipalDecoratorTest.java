/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.decorators.builtin.principal;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_INVOCATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.Collections;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.tests.decorators.AbstractDecoratorTest;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class BuiltinPrincipalDecoratorTest extends AbstractDecoratorTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder()
                .withTestClassPackage(BuiltinPrincipalDecoratorTest.class)
                .withClass(AbstractDecoratorTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createDecorators().clazz(PrincipalDecorator.class.getName())
                                .up()).build();
    }

    @Inject
    PrincipalInjector principalInjector;

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "acm") })
    public void testDecoratorIsResolved() {
        assertNotNull(principalInjector);
        checkDecorator(
                resolveUniqueDecorator(principalInjector.getBeanManager(), Collections.<Type> singleton(Principal.class)),
                PrincipalDecorator.class, Collections.<Type> singleton(Principal.class), Principal.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "acm") })
    public void testDecoratorInvoked() throws Exception {
        assertNotNull(principalInjector);
        assertNotNull(principalInjector.getPrincipal());
        assertEquals(principalInjector.getPrincipal().getName(), "Edgar");
    }
}