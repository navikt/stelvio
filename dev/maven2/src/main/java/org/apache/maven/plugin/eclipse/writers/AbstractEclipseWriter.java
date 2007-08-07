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
package org.apache.maven.plugin.eclipse.writers;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Fabrizio Giustina
 * @version $Id: AbstractEclipseWriter.java 485864 2006-12-11 20:41:36Z fgiust $
 */
public abstract class AbstractEclipseWriter
    implements EclipseWriter
{
    /**
     * Logger.
     */
    protected Log log;

    /**
     * Configuration that will be used by the writer.
     */
    protected EclipseWriterConfig config;

    /**
     * @see org.apache.maven.plugin.eclipse.writers.EclipseWriter#init(org.apache.maven.plugin.logging.Log, org.apache.maven.plugin.eclipse.writers.EclipseWriterConfig)
     */
    public EclipseWriter init( Log log, EclipseWriterConfig config )
    {
        this.log = log;
        this.config = config;
        return this;
    }

}
