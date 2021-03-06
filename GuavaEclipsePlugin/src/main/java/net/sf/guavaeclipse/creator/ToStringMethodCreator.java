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
import static net.sf.guavaeclipse.preferences.UserPreferenceUtil.getNonNls1Preference;
import static net.sf.guavaeclipse.preferences.UserPreferenceUtil.isSkipNullValues;
import static net.sf.guavaeclipse.preferences.UserPreferenceUtil.useMoreObjects;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.preferences.HashCodeStrategyType;
import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;
import net.sf.guavaeclipse.preferences.NonNlsType;
import net.sf.guavaeclipse.preferences.UserPreferenceUtil;
import net.sf.guavaeclipse.utils.Utils;

public class ToStringMethodCreator extends AbstractMethodCreator {

  private final HashCodeStrategyType tmpHcst;

  private boolean useArrays = false;
  
  public ToStringMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    super(insertionPoint, fields);
    this.tmpHcst = UserPreferenceUtil.getHashCodeStrategyType();
  }

  @Override
  protected String getMethodContent() throws JavaModelException {
    StringBuilder content = new StringBuilder();
    content.append("@Override\n");
    addNonNls1SupressWarningIfNecessary(content);
    addGeneratedAnnotationIfNecessary(content);
    content.append("public String toString() {");
    addCodeAnalysisCommentIfNecessary(content);
    content.append("\n");
    if (useMoreObjects()) {
      content.append("  return MoreObjects.toStringHelper(this)\n");
    } else {
      content.append("  return Objects.toStringHelper(this)\n");
    }
    addSkipNullValuesIfNecessary(content);

    if (methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER) {
      content.append("    .add(\"super\", super.toString())\n");
    }

    for (String field : fields) {
      if (useArraysToString(field)) {
        content.append("    .add(\"").append(field).append("\", Arrays.deepToString(");
        if (Utils.fieldIsPrimitiv(insertionPoint.getInsertionType(), field)
            || !Utils.fieldIsArray(insertionPoint.getInsertionType(), field)) {
          content.append("new Object[]{").append(field).append("}");
        } else {
          content.append(field);
        }
        content.append("))");
        useArrays = true;
      } else {
        content.append("    .add(\"").append(field).append("\", ").append(field).append(")");
      }
      addNonNls1CommentIfNecessary(content);
      content.append("\n");
    }

    content.append("    .toString();\n");
    content.append("}\n");
    return content.toString();
  }

  private void addNonNls1CommentIfNecessary(StringBuilder content) {
    if (NonNlsType.NON_NLS_1_COMMENT.equals(getNonNls1Preference())) {
      content.append(" //$NON-NLS-1$");
    }
  }

  private void addNonNls1SupressWarningIfNecessary(StringBuilder content) {
    if (NonNlsType.NON_NLS_1_SUPRESS.equals(getNonNls1Preference())) {
      content.append("@SuppressWarnings(\"nls\")\n");
    }
  }

  private void addSkipNullValuesIfNecessary(StringBuilder content) {
    if (isSkipNullValues()) {
      content.append("    .omitNullValues()\n");
    }
  }

  @Override
  protected String getMethodToDelete() {
    return "toString";
  }

  @Override
  protected String getPackageToImport() {
    String packageToImport = super.getPackageToImport();
    if (useMoreObjects()) {
      packageToImport = "com.google.common.base.MoreObjects";
    }
    packageToImport = addGeneratedAnnotationImportDeclarationIfNecessary(packageToImport);
    if (useArrays) {
      return packageToImport + "," + IMPORT_DECL_ARRAYS;
    } else {
      return packageToImport;
    }
  }

  private boolean useArraysToString(String fieldName) throws JavaModelException {
    if (tmpHcst == SMART_HASH_CODE
        && Utils.fieldIsArray(insertionPoint.getInsertionType(), fieldName)) {
      return true;
    } else if (hcst == ARRAYS_DEEP_HASH_CODE && tmpHcst != SMART_HASH_CODE) {
      return true;
    }
    return false;
  }


}
