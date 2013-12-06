/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.deltaspike.test.testcontrol.uc003;

import org.apache.deltaspike.test.category.SeCategory;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.apache.deltaspike.test.testcontrol.shared.ApplicationScopedBean;
import org.apache.deltaspike.test.testcontrol.shared.RequestScopedBean;
import org.apache.deltaspike.test.testcontrol.shared.SessionScopedBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

//Usually NOT needed! Currently only needed due to our arquillian-setup
@Category(SeCategory.class)



@RunWith(CdiTestRunner.class) //starts container and session once and one request per test-method
@TestControl(startScopes = SessionScoped.class)
public class SessionScopePerTestClassTest
{
    @Inject
    private ApplicationScopedBean applicationScopedBean;

    @Inject
    private SessionScopedBean sessionScopedBean;

    @Inject
    private RequestScopedBean requestScopedBean;

    @Test
    public void firstTest()
    {
        applicationScopedBean.increaseCount();
        sessionScopedBean.increaseCount();

        Assert.assertEquals(0, requestScopedBean.getCount());
        requestScopedBean.increaseCount();
        Assert.assertEquals(1, requestScopedBean.getCount());
    }

    @Test
    public void secondTest()
    {
        applicationScopedBean.increaseCount();
        sessionScopedBean.increaseCount();

        Assert.assertEquals(0, requestScopedBean.getCount());
        requestScopedBean.increaseCount();
        Assert.assertEquals(1, requestScopedBean.getCount());
    }
}
