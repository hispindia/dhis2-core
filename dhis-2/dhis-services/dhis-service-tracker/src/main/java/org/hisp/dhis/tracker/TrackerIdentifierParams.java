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
package org.hisp.dhis.tracker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper object to handle identifier-related parameters for tracker
 * import/export
 *
 * @author Stian Sandvold
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackerIdentifierParams
{
    /**
     * Specific identifier to match data elements on.
     */
    @JsonProperty
    @Builder.Default
    private TrackerIdentifier dataElementIdScheme = TrackerIdentifier.UID;

    /**
     * Specific identifier to match organisation units on.
     */
    @JsonProperty
    @Builder.Default
    private TrackerIdentifier orgUnitIdScheme = TrackerIdentifier.UID;

    /**
     * Specific identifier to match program on.
     */
    @JsonProperty
    @Builder.Default
    private TrackerIdentifier programIdScheme = TrackerIdentifier.UID;

    /**
     * Specific identifier to match program stage on.
     */
    @JsonProperty
    @Builder.Default
    private TrackerIdentifier programStageIdScheme = TrackerIdentifier.UID;

    /**
     * Specific identifier to match all metadata on. Will be overridden by
     * metadata-specific idSchemes.
     */
    @JsonProperty
    @Builder.Default
    private TrackerIdentifier idScheme = TrackerIdentifier.UID;

}
