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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

import com.builder.dto.MethodInsertionPoint;
import com.builder.utils.Utils;

@SuppressWarnings({ "rawtypes" })
public class CompareCreator extends AbstractCreator {

    public CompareCreator(MethodInsertionPoint insertionPoint, List fields) throws JavaModelException {
        super(insertionPoint, fields);
    }

    @Override
    public void generate() throws JavaModelException {
        StringBuilder content = new StringBuilder();
        content.append("@Override\n");
		content.append("public int compareTo(")
				.append(insertionPoint.getInsertionType().getElementName())
				.append(" that){\n");
        content.append("    return ComparisonChain.start()\n");
        String field;
        for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
            field = (String) iterator.next();

//            boolean appendField = false;
//                List<IField> sample = Utils.getFields(insertionPoint.getInsertionType());
//                for (Iterator s = sample.iterator(); s.hasNext();) {
//                    IField iField = (IField) s.next();
//                    if (iField.getElementName().equals(field)) {
//                        System.out.println();
////                        String[][] type=iField.getDeclaringType().resolveType(iField.getTypeSignature().substring(1,iField.getTypeSignature().length() - 1));
////                        System.out.println(iField.getJavaProject().findType(type[0][0] + "." + type[0][1]).isEnum());
//                        
//                        System.out.println(((IType)iField.getAncestor(IJavaElement.TYPE)).getElementName());
////                        Signature.toString(iField.getType(arg0, arg1)Root()TypeSignature());
//                        System.out.println(Signature.getElementType(iField.getTypeSignature()));
//                        if (doesImplementsComparable(iField.getDeclaringType())) {
//                            appendField = true;
//                            
//                        }
//                        break;
//                    }
//                }
//                if (appendField) {
//                    System.out.println(field);
//                }


            content.append("    .compare(this.").append(field).append(", that.").append(field).append(")\n");
        }

        content.append("    .result();\n");
        content.append("}");
        IMethod method = Utils.getMethod(insertionPoint.getInsertionType(), "compareTo");
        if (method != null)
            method.delete(true, new NullProgressMonitor());

		insertionPoint.getInsertionType()
				.createMethod(
						formatCode(content.toString()),
						Utils.getMethod(insertionPoint.getInsertionType(), "compareTo"), 
						true,
                new NullProgressMonitor());

        generateImport("com.google.common.collect.ComparisonChain");

        addImplementsComparable();
    }

    private void addImplementsComparable() throws JavaModelException {
        ICompilationUnit compilationUnit = getCompilationUnit();
        if (compilationUnit != null)
            addImplementsComparable(compilationUnit.getTypes()[0]);

    }

    private boolean doesImplementsComparable(IType type) throws JavaModelException {

        
        // get hierarchy
        ITypeHierarchy hierarchy = type.newTypeHierarchy(null);
hierarchy.getRootInterfaces();
        // get interfaces starting from superclass
        IType[] interfaces = hierarchy.getAllInterfaces();

        // does superclass implements comparable?
        for (int j = 0; j < interfaces.length; j++) {
            System.out.println(interfaces[j].getKey());
            if (interfaces[j].getFullyQualifiedName().equals("java.lang.Comparable")) //$NON-NLS-1$
            {
                return true;
            }
        }

        return false;
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

        // warning, this doesn't handle "implements" and "{" contained in
        // comments in the middle of the declaration!
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
