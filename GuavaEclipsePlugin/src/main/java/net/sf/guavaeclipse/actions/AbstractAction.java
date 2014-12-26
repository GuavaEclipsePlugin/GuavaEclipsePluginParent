package net.sf.guavaeclipse.actions;

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
import org.eclipse.jdt.core.JavaModelException;
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
public abstract class AbstractAction implements IEditorActionDelegate {

  private CompilationUnitEditor currentEditor;
  private Shell shell;

  public AbstractAction() {

  }

  protected CompilationUnitEditor getCurrentEditor() {
    return currentEditor;
  }

  protected Shell getShell() {
    return shell;
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
  public void selectionChanged(IAction iaction, ISelection iselection) {}

  @Override
  public void run(IAction action) {
    if (getCurrentEditor() == null)
      return;

    try {
      MethodInsertionPoint insertionPoint = new MethodInsertionPoint(getCurrentEditor());
      List<String> fields = run(insertionPoint);
      if (fields == null) {
        return;
      }
      openFieldsDialogAndGenerateFields(insertionPoint, fields);
    } catch (MehodGenerationFailedException e) {
      MessageDialog.openError(getShell(), "Unable to generate " + getMethodName() + "()",
          e.getReason());
    } catch (Exception e) {
      MessageDialog.openError(getShell(), "Unable to generate " + getMethodName() + "()",
          e.getMessage());
    }
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
      List<String> fields)
      throws JavaModelException {
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
