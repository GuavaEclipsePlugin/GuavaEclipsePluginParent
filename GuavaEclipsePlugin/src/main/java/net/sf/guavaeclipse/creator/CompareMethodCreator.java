/*
 * Copyright 2014
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sf.guavaeclipse.creator;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;
import net.sf.guavaeclipse.utils.Utils;

public class CompareMethodCreator extends AbstractMethodCreator {

  public CompareMethodCreator(MethodInsertionPoint insertionPoint, List<String> fields)
      throws JavaModelException {
    super(insertionPoint, fields);
  }

  @Override
  public void generate() throws JavaModelException {
    super.generate();

    addImplementsComparable();
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected String getMethodContent() throws JavaModelException {
    StringBuilder content = new StringBuilder();
    content.append("@Override\n");
    content.append("public int compareTo(")
        .append(insertionPoint.getInsertionType().getElementName()).append(" that){\n");
    content.append("    return ComparisonChain.start()\n");
    //
    //
    // ITypeHierarchy a =
    // this.insertionPoint.getInsertionType().newSupertypeHierarchy(new NullProgressMonitor());
    // IType[] allSuperClasses = a.getAllSuperclasses(this.insertionPoint.getInsertionType());
    // for (int i = 0; i < allSuperClasses.length; i++) {
    // IType superClass = allSuperClasses[i];
    // if ("java.lang.Object".equals(superClass.getFullyQualifiedName())) {
    // // reached end
    // break;
    // }
    // IMethod[] methods = superClass.getMethods();
    // for (int j = 0; j < methods.length; j++) {
    // IMethod method = methods[i];
    // System.out.println(method.getElementName());
    // if ("compareTo".equals(method.getElementName())) {
    // System.out.println("we would add");
    // }
    // }
    // IMethod method = superClass.getMethod("compareTo", new String[]{"ClassWithComparable"});
    // System.out.println(method);
    // }
    for (Iterator<String> iterator = fields.iterator(); iterator.hasNext();) {
      String field = iterator.next();
      String commentMsg = "";
      boolean appendField = false;
      List<IField> sample = Utils.getFields(insertionPoint.getInsertionType());
      for (Iterator s = sample.iterator(); s.hasNext();) {
        IField iField = (IField) s.next();
        if (iField.getElementName().equals(field)) {
//          System.out.println("ElementName="+iField.getElementName());
          // handling arrays...
          if (iField.getTypeSignature().startsWith("[")) {
            commentMsg = "is an Array! and they are not comparable by default"; 
            appendField = false;
            break;
          }
          // handling primitives
          if (iField.getTypeSignature().length() == 1) {
            appendField = true;
            break;
          }
          String[][] type = iField.getDeclaringType().resolveType(
              iField.getTypeSignature().substring(1, iField.getTypeSignature().length() - 1));
          if (type != null) {
            String fullQualifiedClassName = type[0][0] + "." + type[0][1];
//            System.out.println("Searching for Class = "+fullQualifiedClassName);
            if ("java.lang.Object".equals(fullQualifiedClassName)) {
              commentMsg = "java.lang.Object is not comparable"; 
              appendField = false;
              break;
            }
            IType findType = iField.getJavaProject().findType(fullQualifiedClassName);
            if (findType != null) {
              if (doesImplementsComparable(findType)) {
                appendField = true;
              } else {
                commentMsg = " does not implements java.lang.Comparable";
                appendField = false;
              }
            } else {
              appendField = false;
            }
            break;
          } else {
            appendField = true;
          }
          
        }
      }
      if (appendField) {
//        System.out.println(field +" isComparable");
        content.append("    .compare(this.").append(field).append(", that.").append(field).append(")\n");
      } else {
//        System.out.println(field +" NOT Comparable");
        if (commentMsg == null || commentMsg.trim().isEmpty()) {
          commentMsg = "is not comparable";
        }
        content.append("// XXX field '"+field+"' "+commentMsg+" \n");
        content.append("//.compare(this.").append(field).append(", that.").append(field).append(")\n");
      }
//      System.out.println("***************************");
    }

    content.append("    .result();\n");
    content.append("}");
    return content.toString();
  }

  @Override
  protected String getPackageToImport() {
    return "com.google.common.collect.ComparisonChain";
  }

  @Override
  protected String getMethodToDelete() {
    return "compareTo";
  }

  private void addImplementsComparable() throws JavaModelException {
    ICompilationUnit compilationUnit = getCompilationUnit();
    if (compilationUnit != null)
      addImplementsComparable(compilationUnit.getTypes()[0]);

  }

  private boolean doesImplementsComparable(IType type) throws JavaModelException {


    // get hierarchy
    ITypeHierarchy hierarchy = type.newTypeHierarchy(null);
//    hierarchy.getRootInterfaces();
    // get interfaces starting from superclass
    IType[] interfaces = hierarchy.getAllInterfaces();

    // does superclass implements comparable?
//    System.out.println("interfaces for "+type.getElementName()+" = "+interfaces.length);
    for (int j = 0; j < interfaces.length; j++) {
//      System.out.println("  "+interfaces[j].getKey());
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
