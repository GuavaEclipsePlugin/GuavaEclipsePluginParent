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
import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.OBJECTS_HASH_CODE;
import static net.sf.guavaeclipse.preferences.HashCodeStrategyType.SMART_HASH_CODE;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.preferences.CodeAnalysisType;
import net.sf.guavaeclipse.preferences.HashCodeStrategyType;
import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;
import net.sf.guavaeclipse.preferences.UserPreferenceUtil;
import net.sf.guavaeclipse.utils.Utils;

public abstract class AbstractMethodCreator {

  protected static final String IMPORT_DECL_OBJECTS = "com.google.common.base.Objects";
  protected static final String IMPORT_DECL_ARRAYS = "java.util.Arrays";

  protected final MethodInsertionPoint insertionPoint;
  protected final List<String> fields;
  protected final MethodGenerationStratergy methodGenerationStratergy;
  protected final HashCodeStrategyType hcst;

  public AbstractMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    this.insertionPoint = insertionPoint;
    this.fields = fields;
    this.methodGenerationStratergy = getMethodGenerationStratergy();
    this.hcst = getHashCodeStrategyType();
  }

  private MethodGenerationStratergy getMethodGenerationStratergy() throws JavaModelException {
    MethodGenerationStratergy methodGenerationStratergy =
        UserPreferenceUtil.getMethodGenerationStratergy();
    if (methodGenerationStratergy == MethodGenerationStratergy.SMART_OPTION) {
      ITypeHierarchy a =
          this.insertionPoint.getInsertionType().newSupertypeHierarchy(new NullProgressMonitor());
      IType superTypes[] = a.getAllSuperclasses(this.insertionPoint.getInsertionType());
      if (superTypes.length == 1 && superTypes[0].getKey().equals("Ljava/lang/Object;")) {
        return MethodGenerationStratergy.DONT_USE_SUPER;
      } else {
        return MethodGenerationStratergy.USE_SUPER;
      }
    }
    return methodGenerationStratergy;
  }

  private HashCodeStrategyType getHashCodeStrategyType() throws JavaModelException {
    HashCodeStrategyType hashCodeStrategyType = UserPreferenceUtil.getHashCodeStrategyType();
    if (hashCodeStrategyType == SMART_HASH_CODE) {
      if (Utils.atLeastOneSelectedFieldIsArray(insertionPoint.getInsertionType(), fields)) {
        hashCodeStrategyType = ARRAYS_DEEP_HASH_CODE;
      } else {
        hashCodeStrategyType = OBJECTS_HASH_CODE;
      }
    }
    return hashCodeStrategyType;
  }

  public void generate() throws JavaModelException {
    String content = getMethodContent();
    IMethod method = getExistingMethod(getMethodToDelete());
    boolean methodDeleted = deleteExistingMethod(method);
    insertionPoint.getInsertionType().createMethod(
            formatCode(content),
            methodDeleted ? null : insertionPoint.getInsertionMember(),
            true,
            new NullProgressMonitor());
    generateImport(getPackageToImport());
  }

  protected abstract String getMethodContent() throws JavaModelException;

  protected abstract String getMethodToDelete();

  /**
   * @return the package which is to add as import statement, overwrite if something else as
   *         <code>com.google.common.base.Objects</code> is necessary
   */
  protected String getPackageToImport() {
    return IMPORT_DECL_OBJECTS;
  }

  protected void generateImport(String importStatement) throws JavaModelException {
    String[] importStatements = importStatement.split(",");
    ICompilationUnit compilationUnit = getCompilationUnit();
    if (compilationUnit != null) {
      for (String importDecl : importStatements) {
        compilationUnit.createImport(importDecl, null, new NullProgressMonitor());
      }
    }
  }
  
  protected String addGeneratedAnnotationImportDeclarationIfNecessary(String packageToImport) {
    if (UserPreferenceUtil.getCodeAnalysisPreference() == CodeAnalysisType.ADD_GENERATED_ANNOTATION) {
      packageToImport += ",javax.annotation.Generated";
    }
    return packageToImport;
  }

  protected void addCodeAnalysisCommentIfNecessary(StringBuilder content) {
    if (UserPreferenceUtil.getCodeAnalysisPreference() == CodeAnalysisType.ADD_COMMENT) {
      content.append(" // ").append(UserPreferenceUtil.getCodeAnalysisCommentPreference());
    }
  }

  protected void addGeneratedAnnotationIfNecessary(StringBuilder content) {
    if (UserPreferenceUtil.getCodeAnalysisPreference() == CodeAnalysisType.ADD_GENERATED_ANNOTATION) {
      content.append("@Generated(value = \"GuavaEclipsePlugin\")");
    }
  }

  protected IMethod getExistingMethod(String methodName) throws JavaModelException {
    return Utils.getMethod(insertionPoint.getInsertionType(), methodName);
  }

  protected ICompilationUnit getCompilationUnit() {
    IJavaElement parentElement = insertionPoint.getInsertionType().getParent();
    if (parentElement == null) {
      return null;
    }
    if (parentElement.getElementType() == 5) {
      return (ICompilationUnit) parentElement;
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  protected String formatCode(String newCode) {
    try {
      Map options = getCompilationUnit().getJavaProject().getOptions(true);
      final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
      TextEdit format =
          codeFormatter.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS, newCode, 0,
              newCode.length(), 1, null);

      IDocument document = new Document(newCode);
      format.apply(document);
      String formattedCode = document.get();
      return formattedCode.replaceAll("\r", "");
    } catch (MalformedTreeException e) {
      e.printStackTrace();
    } catch (BadLocationException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return newCode;
  }

  protected boolean deleteExistingMethod(IMethod method) throws JavaModelException {
    if (method != null) {
      method.delete(true, new NullProgressMonitor());
      return true;
    }
    return false;
  }

}
