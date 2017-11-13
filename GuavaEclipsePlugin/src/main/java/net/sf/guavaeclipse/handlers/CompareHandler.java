package net.sf.guavaeclipse.handlers;

import static net.sf.guavaeclipse.creator.MethodCreatorType.COMPARE_CREATOR;

import java.util.List;

import net.sf.guavaeclipse.creator.MethodCreatorType;
import net.sf.guavaeclipse.dto.MethodInsertionPoint;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class CompareHandler extends AbstractHandler {

  @Override
  public List<String> run(MethodInsertionPoint insertionPoint) throws JavaModelException {
    IType insertionType = insertionPoint.getInsertionType();
    if (!validateMethodGeneration(insertionType)) {
      return null;
    }
    List<String> fields = validateNonStaticFields(insertionType);
    if (fields == null) {
      return null;
    }
    if (!checkExistingMethod(insertionType)) {
      return null;
    }
    return fields;
  };

  @Override
  public String getMethodName() {
    return "compareTo";
  }


  @Override
  public MethodCreatorType getMethodCreatorType() {
    return COMPARE_CREATOR;
  }

}
