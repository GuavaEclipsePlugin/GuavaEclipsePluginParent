package com.builder.creator;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.builder.dto.MethodInsertionPoint;
import com.builder.utils.Utils;

@SuppressWarnings({"rawtypes"})
public class CompareCreator extends AbstractCreator
{

    public CompareCreator(MethodInsertionPoint insertionPoint, List fields)
        throws JavaModelException
    {
        super(insertionPoint, fields);
    }

    @Override
    public void generate()
        throws JavaModelException
    {
        StringBuilder content = new StringBuilder();
        content.append("\n\t@Override\n");
		content.append((new StringBuilder("public int compareTo(")).append(insertionPoint.getInsertionType().getElementName()).append(" that){\n")
		        .toString());
		content.append("\treturn ComparisonChain.start()\n");
        String field;
		for (Iterator iterator = fields.iterator(); iterator.hasNext(); content.append((new StringBuilder("\t\t.compare(this.")).append(field)
		        .append(", that.").append(field).append(")\n").toString()))
            field = (String)iterator.next();

        content.append("\t\t.result();\n");
        content.append("\t}");
		IMethod method = Utils.getMethod(insertionPoint.getInsertionType(), "compareTo");
        if(method != null)
            method.delete(true, new NullProgressMonitor());
		insertionPoint.getInsertionType().createMethod(content.toString(), Utils.getMethod(insertionPoint.getInsertionType(), "compareTo"), true,
		        new NullProgressMonitor());
        generateImport("com.google.common.collect.ComparisonChain");
		addImplementsComparable();
	}

	private void addImplementsComparable() throws JavaModelException {
		ICompilationUnit compilationUnit = null;
		do {
			IJavaElement parentElement = insertionPoint.getInsertionType().getParent();
			if (parentElement == null)
				break;
			if (parentElement.getElementType() == 5)
				compilationUnit = (ICompilationUnit) parentElement;
		} while (compilationUnit == null);
		if (compilationUnit != null)
			addImplementsComparable(compilationUnit.getTypes()[0]);

    }

	private void addImplementsComparable(IType type) throws JavaModelException {

		// does class already implements comparable?
		IType[] interfaces = type.newSupertypeHierarchy(null).getAllInterfaces();
		for (int j = 0, size = interfaces.length; j < size; j++) {
			if (interfaces[j].getFullyQualifiedName().equals("java.lang.Comparable")) //$NON-NLS-1$
			{
				return;
			}
		}

		// find class declaration
		ISourceRange nameRange = type.getNameRange();

		// no declaration??
		if (nameRange == null) {
			return;
		}

		// offset for END of class name
		int offset = nameRange.getOffset() + nameRange.getLength();

		IBuffer buffer = type.getCompilationUnit().getBuffer();
		String contents = buffer.getText(offset, buffer.getLength() - offset);

		// warning, this doesn't handle "implements" and "{" contained in comments in the middle of the declaration!
		int indexOfPar = contents.indexOf("{"); //$NON-NLS-1$

		contents = contents.substring(0, indexOfPar);

		int indexOfImplements = contents.indexOf("implements"); //$NON-NLS-1$
		if (indexOfImplements > -1) {
			buffer.replace(offset + indexOfImplements + "implements".length()//$NON-NLS-1$
			, 0, " Comparable<" + type.getElementName() + ">,"); //$NON-NLS-1$
		} else {
			buffer.replace(offset, 0, " implements Comparable<" + type.getElementName() + ">"); //$NON-NLS-1$
		}

		buffer.save(null, false);
		buffer.close();

	}
}
