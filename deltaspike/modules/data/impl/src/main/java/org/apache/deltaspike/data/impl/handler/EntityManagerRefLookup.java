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
package org.apache.deltaspike.data.impl.handler;

import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.deltaspike.core.api.provider.BeanManagerProvider;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.data.impl.meta.RepositoryMetadata;
import org.apache.deltaspike.jpa.spi.entitymanager.ActiveEntityManagerHolder;

@ApplicationScoped
public class EntityManagerRefLookup
{
    @Inject
    private ActiveEntityManagerHolder activeEntityManagerHolder;

    private volatile Boolean globalEntityManagerInitialized;
    private boolean globalEntityManagerIsNormalScope;
    private EntityManager globalEntityManager;
    
    private void lazyInitGlobalEntityManager()
    {
        if (this.globalEntityManagerInitialized == null)
        {
            initGlobalEntityManager();
        }
    }

    private synchronized void initGlobalEntityManager()
    {
        // switch into paranoia mode
        if (this.globalEntityManagerInitialized == null)
        {
            this.globalEntityManagerInitialized = true;
            
            BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
            Set<Bean<?>> beans = beanManager.getBeans(EntityManager.class);
            Bean<?> bean = beanManager.resolve(beans);

            if (bean == null)
            {
                throw new IllegalStateException("Could not find EntityManager with default qualifier.");
            }
            
            globalEntityManagerIsNormalScope = beanManager.isNormalScope(bean.getScope());
            if (globalEntityManagerIsNormalScope)
            {
                globalEntityManager = (EntityManager) beanManager.getReference(bean,
                        EntityManager.class,
                        beanManager.createCreationalContext(bean));       
            }
        }
    }
    
    public EntityManagerRef lookupReference(final RepositoryMetadata repositoryMetadata)
    {
        EntityManagerRef ref = new EntityManagerRef();

        if (repositoryMetadata.getEntityManagerResolverClass() != null)
        {
            ref.setEntityManagerResolverClass(repositoryMetadata.getEntityManagerResolverClass());
            
            if (repositoryMetadata.isEntityManagerResolverIsNormalScope())
            {
                ref.setEntityManagerResolver(
                        BeanProvider.getContextualReference(ref.getEntityManagerResolverClass()));
            }
            else
            {
                ref.setEntityManagerResolverDependentProvider(
                        BeanProvider.getDependent(ref.getEntityManagerResolverClass()));

                ref.setEntityManagerResolver(
                        ref.getEntityManagerResolverDependentProvider().get());
            }
            
            ref.setEntityManager(
                    ref.getEntityManagerResolver().resolveEntityManager());
        }
        else
        {
            if (activeEntityManagerHolder.isSet())
            {
                ref.setEntityManager(
                        activeEntityManagerHolder.get());
                
                // TODO should we really not apply the FlushMode on the active EntityManager?
                return ref;
            }
            else
            {
                lazyInitGlobalEntityManager();
                if (globalEntityManagerIsNormalScope)
                {
                    ref.setEntityManager(globalEntityManager);
                }
                else
                {
                    ref.setEntityManagerDependentProvider(
                            BeanProvider.getDependent(EntityManager.class));
                    ref.setEntityManager(
                            ref.getEntityManagerDependentProvider().get());
                }
            }
        }

        if (repositoryMetadata.getEntityManagerFlushMode() != null)
        {
            ref.getEntityManager().setFlushMode(repositoryMetadata.getEntityManagerFlushMode());
        }

        return ref;
    }
}
