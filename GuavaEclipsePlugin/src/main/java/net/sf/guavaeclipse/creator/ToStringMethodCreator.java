/*
 * Copyright 2014
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sf.guavaeclipse.creator;

import java.util.Iterator;
import java.util.List;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;

import org.eclipse.jdt.core.JavaModelException;

public class ToStringMethodCreator extends AbstractMethodCreator {

  public ToStringMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    super(insertionPoint, fields);
  }

  @Override
  protected String getMethodContent() {
    StringBuilder content = new StringBuilder();
    content.append("@Override\n");
    content.append("public String toString() {\n");
    content.append("  return Objects.toStringHelper(this)\n");

    if (methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER) {
      content.append("    .add(\"super\", super.toString())\n");
    }

    for (Iterator<String> iterator = fields.iterator(); iterator.hasNext();) {
      String field = iterator.next();
      content.append("    .add(\"").append(field).append("\", ").append(field).append(")\n");
    }

    content.append("    .toString();\n");
    content.append("}\n");
    return content.toString();
  }

  @Override
  protected String getMethodToDelete() {
    return "toString";
  }
}
