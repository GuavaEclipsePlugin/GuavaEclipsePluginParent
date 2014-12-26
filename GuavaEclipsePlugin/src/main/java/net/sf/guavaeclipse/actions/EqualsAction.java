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
import net.sf.guavaeclipse.creator.MethodCreatorType;
import net.sf.guavaeclipse.dialog.GenericDialogBox;
import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.exception.MehodGenerationFailedException;
import net.sf.guavaeclipse.utils.Utils;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

public class EqualsAction extends AbstractAction {

  public EqualsAction() {}

  @Override
  public void run(IAction action) {
    if (getCurrentEditor() == null) {
      return;
    }
    try {
      MethodInsertionPoint insertionPoint = new MethodInsertionPoint(getCurrentEditor());
      IType insertionType = insertionPoint.getInsertionType();
      if (!validateMethodGeneration(insertionType)) {
        return;
      }
      List<String> fields = validateNonStaticFields(insertionType);
      if (fields == null) {
        return;
      }
      IMethod equalsMethod = Utils.getMethod(insertionPoint.getInsertionType(), "equals");
      IMethod hashCodeMethod = Utils.getMethod(insertionPoint.getInsertionType(), "hashCode");
      boolean createEquals = true;
      boolean createHashCode = true;
      boolean replaceBothAnswer = false;
      if (equalsMethod != null && hashCodeMethod != null) {
        replaceBothAnswer =
            MessageDialog.openQuestion(getShell(), "Duplicate Methods",
                "hashCode() and equals() already present. Replace both?");
      }
      if (!replaceBothAnswer) {
        boolean replaceEquals = true;
        boolean replaceHashCode = true;
        if (equalsMethod != null) {
          replaceEquals =
              MessageDialog.openQuestion(getShell(), "Duplicate Method",
                  "equals() already present. Replace it?");
        }

        if (hashCodeMethod != null) {
          replaceHashCode =
              MessageDialog.openQuestion(getShell(), "Duplicate Method",
                  "hashCode() already present. Replace it?");
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
          new GenericDialogBox(getShell(), insertionPoint, fields,
              new ArrayContentProvider(), new LabelProvider(), (new StringBuilder(
                  "Generate hashCode() and equals() for '"))
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
      MessageDialog.openError(getShell(), "Unable to generate equals() and hashCode() methods",
          e.getReason());
    } catch (Exception e) {
      MessageDialog.openError(getShell(), "Unable to generate equals() and hashCode() methods",
          e.getMessage());
    }
  }

  @Override
  public String getMethodName() {
    return "equals";
  }


  @Override
  public MethodCreatorType getMethodCreatorType() {
    return EQUALS_CREATOR;
  }

}
