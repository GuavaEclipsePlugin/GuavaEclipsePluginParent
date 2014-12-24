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

import java.util.List;
import java.util.Map;

import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;
import net.sf.guavaeclipse.preferences.UserPreferenceUtil;
import net.sf.guavaeclipse.utils.Utils;

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

import com.builder.dto.MethodInsertionPoint;


public abstract class AbstractMethodCreator {

  protected MethodInsertionPoint insertionPoint;
  protected List<String> fields;
  protected MethodGenerationStratergy methodGenerationStratergy;

  public AbstractMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    this.insertionPoint = insertionPoint;
    this.fields = fields;
    methodGenerationStratergy = UserPreferenceUtil.getMethodGenerationStratergy();
    if (methodGenerationStratergy == MethodGenerationStratergy.SMART_OPTION) {
      ITypeHierarchy a =
          this.insertionPoint.getInsertionType().newSupertypeHierarchy(new NullProgressMonitor());
      IType superTypes[] = a.getAllSuperclasses(this.insertionPoint.getInsertionType());
      if (superTypes.length == 1 && superTypes[0].getKey().equals("Ljava/lang/Object;"))
        methodGenerationStratergy = MethodGenerationStratergy.DONT_USE_SUPER;
      else
        methodGenerationStratergy = MethodGenerationStratergy.USE_SUPER;
    }
  }

  public void generate() throws JavaModelException {
    String content = getMethodContent();
    IMethod method = getExistingMethod(getMethodToDelete());
    boolean methodDeleted = deleteExistingMethod(method);
    insertionPoint.getInsertionType()
        .createMethod(formatCode(content),
            methodDeleted ? null : insertionPoint.getInsertionMember(), true,
            new NullProgressMonitor());
    generateImport(getPackageToImport());
  }

  protected abstract String getMethodContent();

  protected abstract String getMethodToDelete();

  protected String getPackageToImport() {
    return "com.google.common.base.Objects";
  }

  protected void generateImport(String importStatement) throws JavaModelException {
    ICompilationUnit compilationUnit = getCompilationUnit();
    if (compilationUnit != null) {
      compilationUnit.createImport(importStatement, null, new NullProgressMonitor());
    }
  }

  protected IMethod getExistingMethod(String methodName) throws JavaModelException {
    return Utils.getMethod(insertionPoint.getInsertionType(), methodName);
  }

  protected ICompilationUnit getCompilationUnit() {
    IJavaElement parentElement = insertionPoint.getInsertionType().getParent();
    if (parentElement == null)
      return null;
    if (parentElement.getElementType() == 5)
      return (ICompilationUnit) parentElement;
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

