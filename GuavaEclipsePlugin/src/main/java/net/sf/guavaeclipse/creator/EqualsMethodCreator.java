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

import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.ARRAYS_DEEP_HASH_CODE;
import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.SMART_HASH_CODE;
import static net.sf.guavaeclipse.preferences.UserPreferenceUtil.getHashCodeStrategyType;
import static net.sf.guavaeclipse.preferences.UserPreferenceUtil.usePrimitivesCompareInEquals;
import static net.sf.guavaeclipse.utils.Utils.fieldIsPrimitiv;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.JavaModelException;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.preferences.EqualsEqualityType;
import net.sf.guavaeclipse.preferences.HashCodeStrategyType;
import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;
import net.sf.guavaeclipse.utils.Utils;

public class EqualsMethodCreator extends AbstractEqualsHashCodeMethodCreator {

  private final HashCodeStrategyType tmpHcst;
  
  private boolean useArrays = false;

  private final String equalMethod;
  
  private final boolean dontUseObjectsMethodForPrimitives;
  
  public EqualsMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    super(insertionPoint, fields);
    this.tmpHcst = getHashCodeStrategyType();
    this.equalMethod = super.useJavaUtilsObjects ? "equals" : "equal";
    this.dontUseObjectsMethodForPrimitives = usePrimitivesCompareInEquals();
  }

  @Override
  protected String getMethodContent() throws JavaModelException {
    StringBuilder content = new StringBuilder();
    content.append("@Override\n");
    content.append("public boolean equals(Object object){\n");

    String className = insertionPoint.getInsertionType().getElementName();
    if (eet == EqualsEqualityType.CLASS_EQUALITY) {
      content.append("   if (object != null && getClass() == object.getClass()) {\n");
    } else {
      content.append("   if (object instanceof ").append(className).append(") {\n");
    }

    if (methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER) {
      content.append("      if (!super.equals(object))\n");
      content.append("         return false;\n");
    }

    content.append("      ").append(className).append(" that = (").append(className).append(") object;\n");
    content.append("      return ");

    for (Iterator<String> fieldsIterator = fields.iterator(); fieldsIterator.hasNext();) {
      String field = fieldsIterator.next();
      if (useDeepEquals(field)) {

        content.append("Arrays.deepEquals(");
        if (fieldIsPrimitiv(insertionPoint.getInsertionType(), field)
            || !Utils.fieldIsArray(insertionPoint.getInsertionType(), field)) {
          content.append("new Object[] {this.").append(getGetterOrField(field))
              .append("}, new Object[] {that.").append(getGetterOrField(field)).append("}");
        } else {
          content.append("this.").append(getGetterOrField(field)).append(", that.")
              .append(getGetterOrField(field)).append("");
        }
        content.append(")");
        useArrays = true;
      } else {
        // don't use objects when parameter is primitive feature request #25
        if (dontUseObjectsMethodForPrimitives && fieldIsPrimitiv(insertionPoint.getInsertionType(), field) && !Utils.fieldIsArray(insertionPoint.getInsertionType(), field)) {
          content.append("this.").append(getGetterOrField(field)).append(" ==  that.").append(getGetterOrField(field));
        } else {
          content.append("Objects.").append(this.equalMethod).append("(this.").append(getGetterOrField(field))
          .append(", that.").append(getGetterOrField(field)).append(")");
        }
      }
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

  @Override
  protected String getPackageToImport() {
    if (tmpHcst == SMART_HASH_CODE) {
      if (useArrays) {
        return super.getPackageToImport() + "," + IMPORT_DECL_ARRAYS;
      } else {
        return super.getPackageToImport();
      }
    }
    if (hcst == ARRAYS_DEEP_HASH_CODE) {
      return IMPORT_DECL_ARRAYS;
    }
    return super.getPackageToImport();
  }


  private boolean useDeepEquals(String fieldName) throws JavaModelException {
    if (tmpHcst == SMART_HASH_CODE
        && Utils.fieldIsArray(insertionPoint.getInsertionType(), fieldName)) {
      return true;
    } else if (hcst == ARRAYS_DEEP_HASH_CODE && tmpHcst != SMART_HASH_CODE) {
      return true;
    }
    return false;
  }

}
