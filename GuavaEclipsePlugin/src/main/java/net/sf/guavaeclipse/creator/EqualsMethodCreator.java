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

import net.sf.guavaeclipse.preferences.EqualsEqualityType;
import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;

import org.eclipse.jdt.core.JavaModelException;

import com.builder.dto.MethodInsertionPoint;

public class EqualsMethodCreator extends AbstractEqualsHashCodeMethodCreator {

  public EqualsMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    super(insertionPoint, fields);
  }

  @Override
  protected String getMethodContent() {
    StringBuilder content = new StringBuilder();
    content.append("@Override\n");
    content.append("public boolean equals(Object object){\n");

    if (eet == EqualsEqualityType.CLASS_EQUALITY) {
      content.append("   if (object != null && getClass() == object.getClass()) {\n");
    } else {
      content.append("   if (object instanceof ")
          .append(insertionPoint.getInsertionType().getElementName()).append(") {\n");
    }

    if (methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER) {
      content.append("      if (!super.equals(object))\n");
      content.append("         return false;\n");
    }

    content.append("      ").append(insertionPoint.getInsertionType().getElementName())
        .append(" that = (").append(insertionPoint.getInsertionType().getElementName())
        .append(") object;\n");
    content.append("      return ");

    for (Iterator<String> fieldsIterator = fields.iterator(); fieldsIterator.hasNext();) {
      String field = fieldsIterator.next();
      content.append("Objects.equal(this.").append(field).append(", that.").append(field)
          .append(")");
      if (!fields.get(fields.size() - 1).equals(field)) {
        content.append("\n         && ");
      }
    }

    content.append(";\n   }\n   return false;\n");
    content.append("}");
    return content.toString();
  }


  @Override
  protected String getMethodToDelete() {
    return "equals";
  }

}
