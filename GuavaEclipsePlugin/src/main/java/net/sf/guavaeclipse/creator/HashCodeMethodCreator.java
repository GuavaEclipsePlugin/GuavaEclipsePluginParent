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
import static net.sf.guavaeclipse.utils.Utils.fieldIsPrimitiv;

import java.util.Iterator;
import java.util.List;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;
import net.sf.guavaeclipse.preferences.PrimitivsBoxingType;
import net.sf.guavaeclipse.utils.Utils;

import org.eclipse.jdt.core.JavaModelException;

public class HashCodeMethodCreator extends AbstractEqualsHashCodeMethodCreator {


  private final String hashMethod;
  public HashCodeMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    super(insertionPoint, fields);
    this.hashMethod = super.useJavaUtilsObjects ? "hash" : "hashCode";
  }

  @Override
  protected String getMethodContent() throws JavaModelException {
    StringBuilder content = new StringBuilder();

    content.append("@Override\n");
    if (PrimitivsBoxingType.AUTOBOXING_SUPRESS_WARNINGS == primitivsBoxingType) {
      content.append("@SuppressWarnings(\"boxing\")\n");
    }
    addGeneratedAnnotationIfNecessary(content);
    content.append("public int hashCode(){");
    addCodeAnalysisCommentIfNecessary(content);
    content.append("\n");
    if (hcst == ARRAYS_DEEP_HASH_CODE) {
      content.append("   return Arrays.deepHashCode(new Object[] {");
    } else {
      content.append("   return Objects.");
      
      // this is a hack for java.util.Objects.hash(...) method
      // see warning in javadoc: java.util.Objects#hash(Object...)
      if (methodGenerationStratergy != MethodGenerationStratergy.USE_SUPER && fields.size() == 1) {
        content.append("hashCode");
      } else {
        content.append(hashMethod);
      }

      content.append("(");
    }
    if (methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER) {
      content.append("super.hashCode(), ");
    }
    for (Iterator<String> fieldsIterator = fields.iterator(); fieldsIterator.hasNext();) {
      String field = fieldsIterator.next();
      
      if (PrimitivsBoxingType.EXPLICIT_BOXING == primitivsBoxingType &&
          fieldIsPrimitiv(insertionPoint.getInsertionType(), field) &&
          !Utils.fieldIsArray(insertionPoint.getInsertionType(), field)) {
        content.append(Utils.getBoxingString(insertionPoint.getInsertionType(), field))
        .append("(").append(getGetterOrField(field)).append(")");
      } else {
        content.append(getGetterOrField(field));
      }
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
    String packageToImport = super.getPackageToImport();
    if (hcst == ARRAYS_DEEP_HASH_CODE) {
      packageToImport += "," + IMPORT_DECL_ARRAYS;
    }
    packageToImport = addGeneratedAnnotationImportDeclarationIfNecessary(packageToImport);
    return packageToImport;
  }

}
