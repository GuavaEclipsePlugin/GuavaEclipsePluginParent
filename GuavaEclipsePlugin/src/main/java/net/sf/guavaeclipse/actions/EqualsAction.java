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

import static net.sf.guavaeclipse.creator.MethodCreatorType.EQUALS_CREATOR;
import static net.sf.guavaeclipse.creator.MethodCreatorType.HASH_CODE_CREATOR;

import java.util.List;

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
public class EqualsAction implements IEditorActionDelegate {

  private CompilationUnitEditor currentEditor;
  private Shell shell;

  public EqualsAction() {}

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
    MethodInsertionPoint insertionPoint;
    try {
      insertionPoint = new MethodInsertionPoint(currentEditor);
      String error = Utils.validateMethodGeneration(insertionPoint.getInsertionType());
      if (error != null) {
        MessageDialog.openError(shell, "Method Generation Failed", error);
        return;
      }
      List<String> nonStaticFields =
          Utils.getNonStaticFieldNames(insertionPoint.getInsertionType());
      if (nonStaticFields.size() == 0) {
        MessageDialog.openError(shell, "Method Generation Failed",
            "No non-static field present for method generation.");
        return;
      }
      IMethod equalsMethod = Utils.getMethod(insertionPoint.getInsertionType(), "equals");
      IMethod hashCodeMethod = Utils.getMethod(insertionPoint.getInsertionType(), "hashCode");
      boolean createEquals = true;
      boolean createHashCode = true;
      boolean replaceBothAnswer = false;
      if (equalsMethod != null && hashCodeMethod != null) {
        replaceBothAnswer =
            MessageDialog.openQuestion(shell, "Duplicate Methods",
                "hashCode() and equals() already present. Replace both?");
      }
      if (!replaceBothAnswer) {
        boolean replaceEquals = true;
        boolean replaceHashCode = true;
        if (equalsMethod != null) {
          replaceEquals =
              MessageDialog.openQuestion(shell, "Duplicate Method",
                  "equals() already present. Replace it?");
        }

        if (hashCodeMethod != null) {
          replaceHashCode =
              MessageDialog.openQuestion(shell, "Duplicate Method",
                  "hashCode() already present. Replace both mehtods?");
        }
        // when the user don't want to replace both methods return
        if (!replaceEquals && !replaceHashCode) {
          return;
        }
        if (!replaceEquals) {
          createEquals = false;
        }
        if (!replaceHashCode) {
          createHashCode = false;
        }
      }
      GenericDialogBox dialog =
          new GenericDialogBox(shell, insertionPoint, nonStaticFields, new ArrayContentProvider(),
              new LabelProvider(), (new StringBuilder("Generate hashCode() and equals()for '"))
                  .append(insertionPoint.getInsertionType().getElementName()).append("' class")
                  .toString());
      dialog.open();

      if (!dialog.isCancelPressed()) {
        AbstractMethodCreator creator = null;
        if (createHashCode) {
          creator =
              MethodCreatorFactory.constructMethodCreator(HASH_CODE_CREATOR, insertionPoint,
                  dialog.getResultAsList());
          creator.generate();
        }

        if (createEquals) {
          creator =
              MethodCreatorFactory.constructMethodCreator(EQUALS_CREATOR, insertionPoint,
                  dialog.getResultAsList());
          creator.generate();
        }
      }

    } catch (MehodGenerationFailedException e) {
      MessageDialog.openError(shell, "Unable to generate equals() and hashCode() methods",
          e.getReason());
    } catch (Exception e) {
      MessageDialog.openError(shell, "Unable to generate equals() and hashCode() methods",
          e.getMessage());
    }
  }

  @Override
  public void selectionChanged(IAction iaction, ISelection iselection) {}

}
