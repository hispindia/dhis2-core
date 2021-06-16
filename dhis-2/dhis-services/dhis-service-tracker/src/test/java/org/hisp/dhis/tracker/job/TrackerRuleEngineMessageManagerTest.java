/*
 * Copyright (c) 2004-2021, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.tracker.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.hisp.dhis.artemis.MessageManager;
import org.hisp.dhis.artemis.Topics;
import org.hisp.dhis.render.RenderService;
import org.hisp.dhis.scheduling.SchedulingManager;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.ObjectFactory;

/**
 * @author Zubair Asghar
 */
public class TrackerRuleEngineMessageManagerTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ObjectFactory<TrackerRuleEngineThread> objectFactory;

    @Mock
    private MessageManager messageManager;

    @Mock
    private RenderService renderService;

    @Mock
    private TextMessage textMessage;

    @Mock
    private SchedulingManager schedulingManager;

    @Mock
    private TrackerRuleEngineThread trackerRuleEngineThread;

    @InjectMocks
    private TrackerRuleEngineMessageManager trackerRuleEngineMessageManager;

    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<TrackerSideEffectDataBundle> bundleArgumentCaptor;

    @Captor
    private ArgumentCaptor<Runnable> runnableArgumentCaptor;

    @Test
    public void test_add_job()
    {
        doNothing().when( messageManager ).sendQueue( anyString(), any( TrackerSideEffectDataBundle.class ) );

        TrackerSideEffectDataBundle dataBundle = TrackerSideEffectDataBundle.builder().build();

        trackerRuleEngineMessageManager.addJob( dataBundle );

        Mockito.verify( messageManager ).sendQueue( topicCaptor.capture(), bundleArgumentCaptor.capture() );

        assertNotNull( topicCaptor.getValue() );
        assertEquals( Topics.TRACKER_IMPORT_RULE_ENGINE_TOPIC_NAME, topicCaptor.getValue() );
        assertEquals( dataBundle, bundleArgumentCaptor.getValue() );
    }

    @Test
    public void test_message_consumer()
        throws JMSException,
        IOException
    {
        TrackerSideEffectDataBundle bundle = TrackerSideEffectDataBundle.builder().accessedBy( "test-user" ).build();

        when( textMessage.getText() ).thenReturn( "text" );
        when( objectFactory.getObject() ).thenReturn( trackerRuleEngineThread );
        doNothing().when( schedulingManager ).executeJob( any( Runnable.class ) );

        when( renderService.fromJson( anyString(), eq( TrackerSideEffectDataBundle.class ) ) ).thenReturn( null );
        trackerRuleEngineMessageManager.consume( textMessage );

        verify( schedulingManager, times( 0 ) ).executeJob( any( Runnable.class ) );

        doReturn( bundle ).when( renderService ).fromJson( anyString(), eq( TrackerSideEffectDataBundle.class ) );
        trackerRuleEngineMessageManager.consume( textMessage );

        Mockito.verify( schedulingManager ).executeJob( runnableArgumentCaptor.capture() );

        assertTrue( runnableArgumentCaptor.getValue() instanceof TrackerRuleEngineThread );
    }
}
