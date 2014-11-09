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
import com.builder.creator.CompareCreator;
import com.builder.dialog.GenericDialogBox;
import com.builder.dto.MethodInsertionPoint;
import com.builder.exception.MehodGenerationFailedException;
import com.builder.utils.Utils;

@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
public class CompareAction implements IEditorActionDelegate {

	private CompilationUnitEditor currentEditor;
	private Shell shell;

	public CompareAction() {
	}

	@Override
	public final void setActiveEditor(IAction action, IEditorPart targetPart) {
		currentEditor = (CompilationUnitEditor) targetPart.getAdapter(CompilationUnitEditor.class);
		shell = targetPart.getSite().getShell();
	}

	@Override
	public void run(IAction action) {
		if (currentEditor == null)
			return;

		try {
			MethodInsertionPoint insertionPoint;
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
			org.eclipse.jdt.core.IMethod equalsMethod = Utils.getMethod(insertionPoint.getInsertionType(), "compareTo");
			if (equalsMethod != null) {
				boolean ans = MessageDialog.openQuestion(shell, "Duplicate Method", "compareTo() already present. Replace it?");
				if (!ans)
					return;
			}
			GenericDialogBox dialog = new GenericDialogBox(shell, insertionPoint, Utils.getNonStaticFieldNames(insertionPoint.getInsertionType()),
			        new ArrayContentProvider(), new LabelProvider(), (new StringBuilder("Generate compareTo() for '"))
			                .append(insertionPoint.getInsertionType().getElementName()).append("' class").toString());
			dialog.open();
			if (!dialog.isCancelPressed()) {
				Object result[] = dialog.getResult();
				List fields = new ArrayList();
				if (result != null) {
					for (int i = 0; i < result.length; i++)
						fields.add(result[i]);

				}
				AbstractCreator builder = new CompareCreator(dialog.getInsertionPoint(), fields);
				builder.generate();
			}
		} catch (MehodGenerationFailedException e) {
			MessageDialog.openError(shell, "Unable to generate compare()", e.getReason());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction iaction, ISelection iselection) {
	}

}
