/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mailbox.cassandra.mail;

import org.apache.james.backends.cassandra.CassandraCluster;
import org.apache.james.backends.cassandra.DockerCassandraRule;
import org.apache.james.backends.cassandra.init.CassandraModuleComposite;
import org.apache.james.blob.cassandra.CassandraBlobModule;
import org.apache.james.mailbox.cassandra.modules.CassandraAclModule;
import org.apache.james.mailbox.cassandra.modules.CassandraAnnotationModule;
import org.apache.james.mailbox.cassandra.modules.CassandraApplicableFlagsModule;
import org.apache.james.mailbox.cassandra.modules.CassandraAttachmentModule;
import org.apache.james.mailbox.cassandra.modules.CassandraDeletedMessageModule;
import org.apache.james.mailbox.cassandra.modules.CassandraFirstUnseenModule;
import org.apache.james.mailbox.cassandra.modules.CassandraMailboxCounterModule;
import org.apache.james.mailbox.cassandra.modules.CassandraMailboxModule;
import org.apache.james.mailbox.cassandra.modules.CassandraMailboxRecentsModule;
import org.apache.james.mailbox.cassandra.modules.CassandraMessageModule;
import org.apache.james.mailbox.cassandra.modules.CassandraModSeqModule;
import org.apache.james.mailbox.cassandra.modules.CassandraUidModule;
import org.apache.james.mailbox.store.mail.model.MailboxMapperTest;
import org.apache.james.mailbox.store.mail.model.MapperProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;

public class CassandraGenericMailboxMapperTest extends MailboxMapperTest {
    
    @ClassRule public static DockerCassandraRule cassandraServer = new DockerCassandraRule();
    
    private CassandraCluster cassandra;

    @Override
    @Before
    public void setUp() throws Exception {
        CassandraModuleComposite modules = new CassandraModuleComposite(
                new CassandraAclModule(),
                new CassandraMailboxModule(),
                new CassandraMessageModule(),
                new CassandraMailboxCounterModule(),
                new CassandraMailboxRecentsModule(),
                new CassandraModSeqModule(),
                new CassandraUidModule(),
                new CassandraAttachmentModule(),
                new CassandraAnnotationModule(),
                new CassandraFirstUnseenModule(),
                new CassandraApplicableFlagsModule(),
                new CassandraDeletedMessageModule(),
                new CassandraBlobModule());
        this.cassandra = CassandraCluster.create(modules, cassandraServer.getIp(), cassandraServer.getBindingPort());
        super.setUp();
    }
    
    @After
    public void tearDown() {
        cassandra.close();
    }
    
    @Override
    protected MapperProvider createMapperProvider() {
        return new CassandraMapperProvider(cassandra);
    }
}
