/* Copyright 2014
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.guavaeclipse.creator;

import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.ARRAYS_DEEP_HASH_CODE;

import java.util.Iterator;
import java.util.List;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.preferences.HashCodeStrategyType;
import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;
import net.sf.guavaeclipse.preferences.UserPreferenceUtil;

import org.eclipse.jdt.core.JavaModelException;

public class HashCodeMethodCreator extends AbstractEqualsHashCodeMethodCreator {

  private final HashCodeStrategyType hcst;

  public HashCodeMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    super(insertionPoint, fields);
    hcst = UserPreferenceUtil.getHashCodeStrategyType();
  }

  @Override
  protected String getMethodContent() {
    StringBuilder content = new StringBuilder();

    content.append("@Override\n");
    content.append("public int hashCode(){\n");
    if (hcst == ARRAYS_DEEP_HASH_CODE) {
      content.append("   return Arrays.deepHashCode(new Object[] {");
    } else {
      content.append("   return Objects.hashCode(");
    }
    if (methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER) {
      content.append("super.hashCode(), ");
    }
    for (Iterator<String> fieldsIterator = fields.iterator(); fieldsIterator.hasNext();) {
      String field = fieldsIterator.next();
      content.append(getGetterOrField(field));
      if (!fields.get(fields.size() - 1).equals(field)) {
        content.append(", ");
      }
    }
    if (hcst == ARRAYS_DEEP_HASH_CODE) {
      content.append("}");
    }
    content.append(");\n");
    content.append("}\n");
    content.append("\n");
    return content.toString();
  }

  @Override
  protected String getMethodToDelete() {
    return "hashCode";
  }

  @Override
  protected String getPackageToImport() {
    if (hcst == ARRAYS_DEEP_HASH_CODE) {
      return "java.util.Arrays";
    }
    return super.getPackageToImport();
  }

}
