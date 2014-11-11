package com.builder.creator;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

import com.builder.constant.MethodGenerationStratergy;
import com.builder.constant.UserPrefernce;
import com.builder.dto.MethodInsertionPoint;

@SuppressWarnings({ "rawtypes" })
public abstract class AbstractCreator {

    protected MethodInsertionPoint insertionPoint;
    protected List fields;
    protected MethodGenerationStratergy methodGenerationStratergy;

    public AbstractCreator(MethodInsertionPoint insertionPoint, List fields) throws JavaModelException {
        this.insertionPoint = null;
        this.fields = null;
        this.insertionPoint = insertionPoint;
        this.fields = fields;
        methodGenerationStratergy = UserPrefernce.getMethodGenerationStratergy();
        if (methodGenerationStratergy == MethodGenerationStratergy.SMART_OPTION) {
            ITypeHierarchy a = this.insertionPoint.getInsertionType().newSupertypeHierarchy(new NullProgressMonitor());
			IType superTypes[] = a.getAllSuperclasses(this.insertionPoint.getInsertionType());
            if (superTypes.length == 1 && superTypes[0].getKey().equals("Ljava/lang/Object;"))
                methodGenerationStratergy = MethodGenerationStratergy.DONT_USE_SUPER;
            else
                methodGenerationStratergy = MethodGenerationStratergy.USE_SUPER;
        }
    }

    public abstract void generate() throws JavaModelException;

    protected void generateImport(String importStatement) throws JavaModelException {
        // ICompilationUnit compilationUnit = null;
        // do {
        // IJavaElement parentElement = insertionPoint.getInsertionType().getParent();
        // if (parentElement == null)
        // break;
        // if (parentElement.getElementType() == 5)
        // compilationUnit = (ICompilationUnit) parentElement;
        // } while (compilationUnit == null);
        ICompilationUnit compilationUnit = getCompilationUnit();
        if (compilationUnit != null)
            compilationUnit.createImport(importStatement, null, new NullProgressMonitor());

    }

    protected ICompilationUnit getCompilationUnit() {
        IJavaElement parentElement = insertionPoint.getInsertionType().getParent();
        if (parentElement == null)
            return null;
        if (parentElement.getElementType() == 5)
            return (ICompilationUnit) parentElement;
        return null;
    }
}
