/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.runtimes.jetty9;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;

public class DeploymentCheck extends AbstractLifeCycle.AbstractLifeCycleListener {
  @Override
  public void lifeCycleStarted(LifeCycle bean) {
    if (bean instanceof Server) {
      Server server = (Server)bean;
      Connector[] connectors = server.getConnectors();
      if (connectors.length == 0 || !connectors[0].isStarted()) {
        server.dumpStdErr();
        throw new IllegalStateException("No Started Connector");
      }
      ContextHandler context = server.getChildHandlerByClass(ContextHandler.class);
      if (context == null || !context.isAvailable()) {
        server.dumpStdErr();
        throw new IllegalStateException("No Available Context");
      }
    }
  }
}