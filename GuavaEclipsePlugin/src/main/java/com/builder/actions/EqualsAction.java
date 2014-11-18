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
package com.builder.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.builder.creator.AbstractCreator;
import com.builder.creator.EqualsCreator;
import com.builder.dialog.GenericDialogBox;
import com.builder.dto.MethodInsertionPoint;
import com.builder.exception.MehodGenerationFailedException;
import com.builder.utils.Utils;

@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
public class EqualsAction implements IEditorActionDelegate {

	private CompilationUnitEditor currentEditor;
	private Shell shell;

	public EqualsAction() {
	}

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
		if (currentEditor == null)
			return;
		MethodInsertionPoint insertionPoint;
		try {
			insertionPoint = new MethodInsertionPoint(currentEditor);
			String error = Utils.validateMethodGeneration(insertionPoint.getInsertionType());
			if (error != null) {
				MessageDialog.openError(shell, "Method Generation Failed", error);
				return;
			}
			List list = Utils.getNonStaticFieldNames(insertionPoint.getInsertionType());
			if (list.size() == 0) {
				MessageDialog.openError(shell, "Method Generation Failed", "No non-static field present for method generation.");
				return;
			}
			org.eclipse.jdt.core.IMethod equalsMethod;
			org.eclipse.jdt.core.IMethod hashCodeMethod;
			equalsMethod = Utils.getMethod(insertionPoint.getInsertionType(), "equals");
			hashCodeMethod = Utils.getMethod(insertionPoint.getInsertionType(), "hashCode");
			if (equalsMethod != null && hashCodeMethod != null) {
				boolean ans = MessageDialog.openQuestion(shell, "Duplicate Method", "hashCode() and equals()already present. Replace it?");
				if (!ans)
					return;

			}
			if (equalsMethod != null) {
				boolean ans = MessageDialog.openQuestion(shell, "Duplicate Method", "equals()already present. Replace both mehtods?");
				if (!ans)
					return;

			}
			if (hashCodeMethod != null) {
				boolean ans = MessageDialog.openQuestion(shell, "Duplicate Method", "hashCode()already present. Replace both mehtods?");
				if (!ans)
					return;
			}
			GenericDialogBox dialog = new GenericDialogBox(shell, insertionPoint, Utils.getNonStaticFieldNames(insertionPoint.getInsertionType()),
			        new ArrayContentProvider(), new LabelProvider(), (new StringBuilder("Generate hashCode() and equals()for '"))
			                .append(insertionPoint.getInsertionType().getElementName()).append("' class").toString());
			dialog.open();
			if (!dialog.isCancelPressed()) {
				Object result[] = dialog.getResult();
				List fields = new ArrayList();
				if (result != null) {
					for (int i = 0; i < result.length; i++)
						fields.add(result[i]);

				}
				AbstractCreator builder = new EqualsCreator(dialog.getInsertionPoint(), fields);
				builder.generate();
			}

		} catch (MehodGenerationFailedException e) {
			MessageDialog.openError(shell, "Unable to generate toString()", e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction iaction, ISelection iselection) {
	}

}
