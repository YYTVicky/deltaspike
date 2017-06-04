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
package org.apache.deltaspike.data.impl.meta;

import java.lang.reflect.Method;
import java.util.Map;
import javax.persistence.FlushModeType;
import org.apache.deltaspike.data.api.EntityManagerResolver;

public class RepositoryMetadata
{
    private Class<?> repositoryClass;

    private EntityMetadata entityMetadata;
    private Map<Method, RepositoryMethodMetadata> methodsMetadata;

    private Class<? extends EntityManagerResolver> entityManagerResolverClass;
    private boolean entityManagerResolverIsNormalScope;
    private FlushModeType entityManagerFlushMode;
    
    public RepositoryMetadata(Class<?> repositoryClass)
    {
        this.repositoryClass = repositoryClass;
    }
    
    public RepositoryMetadata(Class<?> repositoryClass, EntityMetadata entityMetadata)
    {
        this.repositoryClass = repositoryClass;
        this.entityMetadata = entityMetadata;
    }
    
    public Class<?> getRepositoryClass()
    {
        return repositoryClass;
    }

    public void setRepositoryClass(Class<?> repositoryClass)
    {
        this.repositoryClass = repositoryClass;
    }

    public EntityMetadata getEntityMetadata()
    {
        return entityMetadata;
    }

    public void setEntityMetadata(EntityMetadata entityMetadata)
    {
        this.entityMetadata = entityMetadata;
    }

    public Class<? extends EntityManagerResolver> getEntityManagerResolverClass()
    {
        return entityManagerResolverClass;
    }

    public void setEntityManagerResolverClass(Class<? extends EntityManagerResolver> entityManagerResolverClass)
    {
        this.entityManagerResolverClass = entityManagerResolverClass;
    }

    public FlushModeType getEntityManagerFlushMode()
    {
        return entityManagerFlushMode;
    }

    public void setEntityManagerFlushMode(FlushModeType entityManagerFlushMode)
    {
        this.entityManagerFlushMode = entityManagerFlushMode;
    }

    public boolean isEntityManagerResolverIsNormalScope()
    {
        return entityManagerResolverIsNormalScope;
    }

    public void setEntityManagerResolverIsNormalScope(boolean entityManagerResolverIsNormalScope)
    {
        this.entityManagerResolverIsNormalScope = entityManagerResolverIsNormalScope;
    }

    public Map<Method, RepositoryMethodMetadata> getMethodsMetadata()
    {
        return methodsMetadata;
    }

    public void setMethodsMetadata(Map<Method, RepositoryMethodMetadata> methodsMetadata)
    {
        this.methodsMetadata = methodsMetadata;
    }
}
