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
		content.append("@Override\n");
		content.append("public int hashCode(){\n");
		content.append("   return Objects.hashCode(");
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
        content.append(");\n");
		content.append("}\n");
		content.append("\n");
        IMethod method = Utils.getMethod(insertionPoint.getInsertionType(), "hashCode");
        if(method != null) {
            method.delete(true, new NullProgressMonitor());
        }
		insertionPoint.getInsertionType().createMethod(
				formatCode(content.toString()),
				insertionPoint.getInsertionMember(), 
				true,
				new NullProgressMonitor());

		content = new StringBuilder();
		content.append("@Override\n");
		content.append("public boolean equals(Object object){\n");
		if (eet == EqualsEqualityType.CLASS_EQUALITY) {
			content.append("   if (object != null && getClass() == object.getClass()) {\n");
		} else {
			content.append("   if (object instanceof ")
					.append(insertionPoint.getInsertionType().getElementName())
					.append(") {\n");
		}
        if(methodGenerationStratergy == MethodGenerationStratergy.USE_SUPER) {
			content.append("      if (!super.equals(object))\n");
			content.append("         return false;\n");
        }
		content.append("      ")
				.append(insertionPoint.getInsertionType().getElementName())
				.append(" that = (")
				.append(insertionPoint.getInsertionType().getElementName())
				.append(") object;\n");
		content.append("      return ");
        String field;
        for(Iterator iterator1 = fields.iterator(); iterator1.hasNext(); content.append("Objects.equal(this.").append(field).append(", that.").append(field).append(")"))
        {
            field = (String)iterator1.next();
            if(!((String)fields.get(0)).equals(field))
				content.append("\n         && ");
        }

		content.append(";\n   }\n   return false;\n");
		content.append("}");
        IMethod equalsMethod = Utils.getMethod(insertionPoint.getInsertionType(), "equals");
        if(equalsMethod != null)
            equalsMethod.delete(true, new NullProgressMonitor());

		insertionPoint.getInsertionType()
				.createMethod(
				formatCode(content.toString()),
				insertionPoint.getInsertionMember(), 
				true,
				new NullProgressMonitor());

        generateImport("com.google.common.base.Objects");

    }

}
