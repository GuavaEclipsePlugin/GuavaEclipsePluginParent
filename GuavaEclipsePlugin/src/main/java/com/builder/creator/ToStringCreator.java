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
package com.builder.creator;

import java.util.Iterator;
import java.util.List;

import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;
import net.sf.guavaeclipse.utils.Utils;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.builder.dto.MethodInsertionPoint;

@SuppressWarnings({"rawtypes"})
public class ToStringCreator extends AbstractCreator {

  public ToStringCreator(MethodInsertionPoint insertionPoint, List fields)
      throws JavaModelException {
    super(insertionPoint, fields);
  }

  @Override
  public void generate() throws JavaModelException {
    StringBuilder content = new StringBuilder();
    content.append("@Override\n");
    content.append("public String toString() {\n");
    content.append("  return Objects.toStringHelper(this)\n");
    if (methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER)
      content.append("    .add(\"super\", super.toString())\n");
    for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
      String field = (String) iterator.next();
      content.append("    .add(\"").append(field).append("\", ").append(field).append(")\n");
    }
    content.append("    .toString();\n");
    content.append("}\n");
    IMethod method = Utils.getMethod(insertionPoint.getInsertionType(), "toString");
    boolean methodDeleted = deleteExistingMethod(method);
    insertionPoint.getInsertionType()
        .createMethod(formatCode(content.toString()),
            methodDeleted ? null : insertionPoint.getInsertionMember(), true,
            new NullProgressMonitor());
    generateImport("com.google.common.base.Objects");
  }
}
