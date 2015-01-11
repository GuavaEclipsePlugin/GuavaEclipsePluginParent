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
package net.sf.guavaeclipse.handlers;

import java.util.List;

import net.sf.guavaeclipse.creator.AbstractMethodCreator;
import net.sf.guavaeclipse.creator.MethodCreatorFactory;
import net.sf.guavaeclipse.creator.MethodCreatorType;
import net.sf.guavaeclipse.dialog.GenericDialogBox;
import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.exception.MehodGenerationFailedException;
import net.sf.guavaeclipse.utils.Utils;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;


@SuppressWarnings("restriction")
public abstract class AbstractHandler extends org.eclipse.core.commands.AbstractHandler {

  private CompilationUnitEditor currentEditor;
  private Shell shell;

  protected CompilationUnitEditor getCurrentEditor() {
    return currentEditor;
  }

  protected Shell getShell() {
    return shell;
  }

  public final void setActiveEditor(final ExecutionEvent event) {
    IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
    currentEditor = (CompilationUnitEditor) activeEditor.getAdapter(CompilationUnitEditor.class);
    shell = HandlerUtil.getActiveShell(event);
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    setActiveEditor(event);
    if (getCurrentEditor() == null) {
      return null;
    }

    try {
      MethodInsertionPoint insertionPoint = new MethodInsertionPoint(this.currentEditor);
      List<String> fields = run(insertionPoint);
      if (fields == null) {
        return null;
      }
      openFieldsDialogAndGenerateFields(insertionPoint, fields);
    } catch (MehodGenerationFailedException e) {
      MessageDialog.openError(this.shell, "Unable to generate " + getMethodName() + "()",
          e.getReason());
    } catch (Exception e) {
      MessageDialog.openError(this.shell, "Unable to generate " + getMethodName() + "()",
          e.getMessage());
    }

    return null;
  }

  public List<String> run(MethodInsertionPoint insertionPoint) throws JavaModelException {
    return null;
  };

  public abstract String getMethodName();

  public abstract MethodCreatorType getMethodCreatorType();

  public boolean validateMethodGeneration(IType type) throws JavaModelException {
    String error = Utils.validateMethodGeneration(type);
    if (error != null) {
      MessageDialog.openError(getShell(), "Method Generation Failed", error);
      return false;
    }
    return true;
  }

  public List<String> validateNonStaticFields(IType type) throws JavaModelException {
    List<String> nonStaticFieldNames = Utils.getNonStaticFieldNames(type);
    if (nonStaticFieldNames.size() == 0) {
      MessageDialog.openError(getShell(), "Method Generation Failed",
          "No non-static field present for method generation.");
      return null;
    }
    return nonStaticFieldNames;
  }

  public List<String> validateFields(IType type) throws JavaModelException {
    List<String> fieldNames = Utils.getFieldNames(type);
    if (fieldNames.size() == 0) {
      MessageDialog.openError(getShell(), "Method Generation Failed",
          "No field present for method generation.");
      return null;
    }
    return fieldNames;
  }

  public boolean checkExistingMethod(IType type) throws JavaModelException {
    IMethod equalsMethod = Utils.getMethod(type, getMethodName());
    if (equalsMethod != null) {
      boolean ans =
          MessageDialog.openQuestion(getShell(), "Duplicate Method", getMethodName()
              + "() method already present. Replace it?");
      return ans;
    }
    return true;
  }

  public void openFieldsDialogAndGenerateFields(MethodInsertionPoint insertionPoint,
      List<String> fields) throws JavaModelException {
    IType insertionType = insertionPoint.getInsertionType();
    GenericDialogBox dialog =
        new GenericDialogBox(getShell(), insertionPoint, fields, new ArrayContentProvider(),
            new LabelProvider(), getStringBuilderForDialogTitle()
                .append(insertionType.getElementName()).append("' class").toString());
    dialog.open();
    if (!dialog.isCancelPressed()) {
      AbstractMethodCreator creator =
          MethodCreatorFactory.constructMethodCreator(getMethodCreatorType(), insertionPoint,
              dialog.getResultAsList());
      creator.generate();
    }
  }

  public StringBuilder getStringBuilderForDialogTitle() {
    return new StringBuilder("Generate ").append(getMethodName()).append("() for '");
  }

}
