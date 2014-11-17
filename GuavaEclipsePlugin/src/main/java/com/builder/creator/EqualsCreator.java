package com.builder.creator;

import java.util.Iterator;
import java.util.List;

import net.sf.guavaeclipse.preferences.EqualsEqualityType;
import net.sf.guavaeclipse.preferences.MethodGenerationStratergy;
import net.sf.guavaeclipse.preferences.UserPreferenceUtil;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.builder.dto.MethodInsertionPoint;
import com.builder.utils.Utils;

@SuppressWarnings({"rawtypes"})
public class EqualsCreator extends AbstractCreator
{

	private EqualsEqualityType eet;

    public EqualsCreator(MethodInsertionPoint insertionPoint, List fields)
        throws JavaModelException
    {
        super(insertionPoint, fields);
		eet = UserPreferenceUtil.getEqualsEqualityType();
    }

    @Override
    public void generate()
        throws JavaModelException
    {
        StringBuilder content = new StringBuilder();
        content.append("\n\t@Override\n");
        content.append("public int hashCode(){\n");
        content.append("\treturn Objects.hashCode(");
        if(methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER)
            content.append("super.hashCode(), ");
        for(Iterator iterator = fields.iterator(); iterator.hasNext();)
        {
            String field = (String)iterator.next();
            if(((String)fields.get(fields.size() - 1)).equals(field))
                content.append(field);
            else
                content.append(field).append(", ");
        }

        content.trimToSize();
        content.append(");\n");
        content.append("}\n");
        IMethod method = Utils.getMethod(insertionPoint.getInsertionType(), "hashCode");
        if(method != null)
            method.delete(true, new NullProgressMonitor());
        StringBuilder equalsContent = new StringBuilder();
        equalsContent.append(content.toString());
        equalsContent.append("\n@Override\n");
        equalsContent.append("public boolean equals(Object object){\n");
		if (eet == EqualsEqualityType.CLASS_EQUALITY) {
			equalsContent.append("\tif (object != null && getClass() == object.getClass()) {\n");
		} else {
        equalsContent.append("\tif (object instanceof ").append(insertionPoint.getInsertionType().getElementName()).append(") {\n");
		}
        if(methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER)
            equalsContent.append("\t\tif (!super.equals(object)) \n\t\t\treturn false;\n");
        equalsContent.append((new StringBuilder("\t\t")).append(insertionPoint.getInsertionType().getElementName()).toString()).append(" that = (").append(insertionPoint.getInsertionType().getElementName()).append(") object;\n");
        equalsContent.append("\t\treturn ");
        String field;
        for(Iterator iterator1 = fields.iterator(); iterator1.hasNext(); equalsContent.append("Objects.equal(this.").append(field).append(", that.").append(field).append(")"))
        {
            field = (String)iterator1.next();
            if(!((String)fields.get(0)).equals(field))
                equalsContent.append("\n\t\t\t&& ");
        }

        equalsContent.append(";\n\t}\n\treturn false;\n");
        equalsContent.append("}\n");
        IMethod equalsMethod = Utils.getMethod(insertionPoint.getInsertionType(), "equals");
        if(equalsMethod != null)
            equalsMethod.delete(true, new NullProgressMonitor());
        insertionPoint.getInsertionType().createMethod(equalsContent.toString(), insertionPoint.getInsertionMember(), true, new NullProgressMonitor());
        generateImport("com.google.common.base.Objects");
    }
}
