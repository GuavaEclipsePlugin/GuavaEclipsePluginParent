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
package net.sf.guavaeclipse.dto;

import net.sf.guavaeclipse.utils.Utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.part.FileEditorInput;

@SuppressWarnings({ "restriction" })
public class MethodInsertionPoint {
	private IType insertionType;
	private IJavaElement insertionMember;

	public MethodInsertionPoint(CompilationUnitEditor editor) throws JavaModelException {

		IFile file = ((FileEditorInput) editor.getEditorInput()).getFile();
		IType types[] = Utils.getIType(file);
		int ii = getElementAfterCursorPosition(types, editor);
		if (ii != 0)
			ii--;
		insertionType = types[ii];
		IJavaElement members[] = insertionType.getChildren();
		ii = getElementAfterCursorPosition(members, editor);
		insertionMember = null;
		if (ii == 0)
			insertionMember = null;
		if (ii == members.length)
			insertionMember = null;
		else
			insertionMember = members[ii];
	}

	private int getElementAfterCursorPosition(IJavaElement members[], CompilationUnitEditor editor) throws JavaModelException {
		int offset = ((ITextSelection) editor.getSelectionProvider().getSelection()).getOffset();
		for (int i = 0; i < members.length; i++) {
			IMember curr = (IMember) members[i];
			ISourceRange range = curr.getSourceRange();
			if (offset < range.getOffset())
				return i;
		}

		return members.length;
	}

	public IType getInsertionType() {
		return insertionType;
	}

	public void setInsertionType(IType insertionType) {
		this.insertionType = insertionType;
	}

	public IJavaElement getInsertionMember() {
		return insertionMember;
	}

	public void setInsertionMember(IJavaElement insertionMember) {
		this.insertionMember = insertionMember;
	}

}
