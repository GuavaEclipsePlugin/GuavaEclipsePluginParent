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
package net.sf.guavaeclipse.actions;

import static net.sf.guavaeclipse.creator.MethodCreatorType.TO_STRING_CREATOR;
import net.sf.guavaeclipse.creator.AbstractMethodCreator;
import net.sf.guavaeclipse.creator.MethodCreatorFactory;
import net.sf.guavaeclipse.dialog.GenericDialogBox;
import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.exception.MehodGenerationFailedException;
import net.sf.guavaeclipse.utils.Utils;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

@SuppressWarnings({"restriction"})
public class ToStringAction implements IEditorActionDelegate {

  private CompilationUnitEditor currentEditor;
  private Shell shell;

  public ToStringAction() {}

  @Override
  public final void setActiveEditor(IAction action, IEditorPart targetPart) {
    if (targetPart == null) {
      return;
    }
    if (action == null) {
      return;
    }
    currentEditor = (CompilationUnitEditor) targetPart.getAdapter(CompilationUnitEditor.class);
    shell = targetPart.getSite().getShell();
  }

  @Override
  public void run(IAction action) {
    if (currentEditor == null) {
      return;
    }
    try {
      MethodInsertionPoint insertionPoint;
      insertionPoint = new MethodInsertionPoint(currentEditor);
      String error = Utils.validateMethodGeneration(insertionPoint.getInsertionType());
      if (error != null) {
        MessageDialog.openError(shell, "Method Generation Failed", error);
        return;
      }
      IMethod method = Utils.getMethod(insertionPoint.getInsertionType(), "toString");
      if (method != null) {
        boolean ans =
            MessageDialog.openQuestion(shell, "Duplicate Method",
                "toString() method already present. Replace it?");
        if (!ans) {
          return;
        }
      }

      GenericDialogBox dialog =
          new GenericDialogBox(shell, insertionPoint, Utils.getFieldNames(insertionPoint
              .getInsertionType()), new ArrayContentProvider(), new LabelProvider(),
              (new StringBuilder("Generate toString() for '"))
                  .append(insertionPoint.getInsertionType().getElementName()).append("' class")
                  .toString());
      dialog.open();
      if (!dialog.isCancelPressed()) {
        AbstractMethodCreator creator =
            MethodCreatorFactory.constructMethodCreator(TO_STRING_CREATOR, insertionPoint,
                dialog.getResultAsList());
        creator.generate();
      }
    } catch (MehodGenerationFailedException e) {
      MessageDialog.openError(shell, "Unable to generate toString()", e.getReason());
    } catch (Exception e) {
      MessageDialog.openError(shell, "Unable to generate toString()", e.getMessage());
    }
  }

  @Override
  public void selectionChanged(IAction iaction, ISelection iselection) {}

}
