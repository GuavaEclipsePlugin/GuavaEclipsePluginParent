package net.sf.guavaeclipse.handlers;

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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

public class EqualsHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    setActiveEditor(event);
    if (getCurrentEditor() == null) {
      return null;
    }
    try {
      MethodInsertionPoint insertionPoint = new MethodInsertionPoint(getCurrentEditor());
      IType insertionType = insertionPoint.getInsertionType();
      if (!validateMethodGeneration(insertionType)) {
        return null;
      }
      List<String> fields = validateNonStaticFields(insertionType);
      if (fields == null) {
        return null;
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
          return null;
        }
        if (!replaceEquals) {
          createEquals = false;
        }
        if (!replaceHashCode) {
          createHashCode = false;
        }
      }
      GenericDialogBox dialog =
          new GenericDialogBox(getShell(), insertionPoint, fields, new ArrayContentProvider(),
              new LabelProvider(), (new StringBuilder("Generate hashCode() and equals() for '"))
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
    return null;
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
