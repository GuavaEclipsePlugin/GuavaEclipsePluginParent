package com.builder.creator;

import java.util.Iterator;
import java.util.List;

import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.builder.dto.MethodInsertionPoint;
import com.builder.utils.Utils;

@SuppressWarnings({"rawtypes"})
public class ToStringCreator extends AbstractCreator
{

    public ToStringCreator(MethodInsertionPoint insertionPoint, List fields)
        throws JavaModelException
    {
        super(insertionPoint, fields);
    }

    public void generate()
        throws JavaModelException
    {
        StringBuilder content = new StringBuilder();
        content.append("\n@Override\n");
        content.append("public String toString() {\n");
        content.append("\treturn Objects.toStringHelper(this)\n");
        if(methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER)
            content.append("\t\t.add(\"super\", super.toString())\n");
        String field;
        for(Iterator iterator = fields.iterator(); iterator.hasNext(); content.append("\t\t.add(\"").append(field).append("\", ").append(field).append(")\n"))
            field = (String)iterator.next();

        content.append("\t\t.toString();\n");
        content.append("}\n");
        IMethod method = Utils.getMethod(insertionPoint.getInsertionType(), "toString");
        if(method != null)
            method.delete(true, new NullProgressMonitor());
        insertionPoint.getInsertionType().createMethod(content.toString(), insertionPoint.getInsertionMember(), true, new NullProgressMonitor());
        generateImport("com.google.common.base.Objects");
    }
}
