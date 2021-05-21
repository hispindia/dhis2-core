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
package org.hisp.dhis.configuration;

import org.hisp.dhis.condition.RedisDisabledCondition;
import org.hisp.dhis.condition.RedisEnabledCondition;
import org.hisp.dhis.system.notification.InMemoryNotifier;
import org.hisp.dhis.system.notification.Notifier;
import org.hisp.dhis.system.notification.RedisNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * This class deals with the configuring an appropriate notifier depending on
 * whether redis is enabled or not.
 *
 * @author Ameen Mohamed
 *
 */
@Configuration
public class NotifierConfiguration
{
    @Autowired( required = false )
    private RedisTemplate<?, ?> redisTemplate;

    @SuppressWarnings( "unchecked" )
    @Bean
    @Qualifier( "notifier" )
    @Conditional( RedisEnabledCondition.class )
    public Notifier redisNotifier()
    {
        return new RedisNotifier( (RedisTemplate<String, String>) redisTemplate );
    }

    @Bean
    @Qualifier( "notifier" )
    @Conditional( RedisDisabledCondition.class )
    public Notifier inMemoryNotifier()
    {
        return new InMemoryNotifier();
    }
}