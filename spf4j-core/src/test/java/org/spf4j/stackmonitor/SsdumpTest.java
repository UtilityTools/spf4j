/*
 * Copyright (c) 2001-2017, Zoltan Farkas All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Additionally licensed with:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spf4j.stackmonitor;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spf4j.base.ExecutionContexts;
import org.spf4j.ds.Traversals;
import org.spf4j.ds.Graph;
import org.spf4j.ssdump2.Converter;

public final class SsdumpTest {

  private static final Logger LOG = LoggerFactory.getLogger(SsdumpTest.class);


  @Test
  public void testDumpExecContexts() throws InterruptedException, IOException {
    System.setProperty("spf4j.execContentFactoryWrapperClass",
            "org.spf4j.stackmonitor.ProfiledExecutionContextFactory");
    ProfiledExecutionContextFactory contextFactory =
            (ProfiledExecutionContextFactory) ExecutionContexts.getContextFactory();

    Sampler sampler = new Sampler(1, new ThreadStackCollector(contextFactory::getCurrentThreads));
    sampleTest(sampler, "ecStackSample");
  }


  @Test
  public void testDumpDefault() throws InterruptedException, IOException {
    Sampler sampler = new Sampler(1);
    sampleTest(sampler, "stackSample");
  }

  public void sampleTest(final Sampler sampler, final String filename) throws InterruptedException, IOException {
    sampler.registerJmx();
    sampler.start();
    MonitorTest.main(new String[]{});
    final File serializedFile = File.createTempFile(filename, ".ssdump2");
    sampler.getStackCollector().applyOnSamples((final SampleNode f) -> {
      if (f != null) {
        try {
          Converter.save(serializedFile, f);
        } catch (IOException ex) {
          throw new UncheckedIOException(ex);
        }

      }
      return f;
    });
    LOG.debug("Dumped to file {}", serializedFile);
    sampler.stop();
    final SampleNode samples = Converter.load(serializedFile);
    Graph<InvokedMethod, SampleNode.InvocationCount> graph = SampleNode.toGraph(samples);
    Traversals.traverse(graph, InvokedMethod.ROOT,
            (final InvokedMethod vertex, final Map<SampleNode.InvocationCount, InvokedMethod> edges) -> {
              LOG.debug("Method: {} from {}", vertex, edges);
            }, true);
    Assert.assertNotNull(graph);
  }
}
