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
package net.sf.guavaeclipse.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public final class Utils {

  private Utils() {}

  public static IType[] getIType(IFile file) throws JavaModelException {
    IResource resource = file.getParent().findMember(file.getName());
    return getIType(resource);
  }

  public static IType[] getIType(IResource resource) throws JavaModelException {
    ICompilationUnit java = (ICompilationUnit) JavaCore.create(resource);
    return java.getTypes();
  }

  public static List<String> getFieldNames(IType type) throws JavaModelException {
    IField fields[] = type.getFields();
    List<String> list = new ArrayList<String>();
    IField aifield[];
    int j = (aifield = fields).length;
    for (int i = 0; i < j; i++) {
      IField field = aifield[i];
      list.add(field.getElementName());
    }

    return list;
  }


  public static List<IField> getFields(IType type) throws JavaModelException {
    IField fields[] = type.getFields();
    List<IField> list = new ArrayList<IField>();
    IField aifield[];
    int j = (aifield = fields).length;
    for (int i = 0; i < j; i++) {
      IField field = aifield[i];
      list.add(field);
    }

    return list;
  }

  public static List<String> getNonStaticFieldNames(IType type) throws JavaModelException {
    IField fields[] = type.getFields();
    List<String> list = new ArrayList<String>();
    IField aifield[];
    int j = (aifield = fields).length;
    for (int i = 0; i < j; i++) {
      IField field = aifield[i];
      if (!isStaticField(field))
        list.add(field.getElementName());
    }

    return list;
  }

  public static List<String> getMethodNames(IType type) throws JavaModelException {
    IMethod fields[] = type.getMethods();
    List<String> list = new ArrayList<String>();
    IMethod aimethod[];
    int j = (aimethod = fields).length;
    for (int i = 0; i < j; i++) {
      IMethod field = aimethod[i];
      list.add(field.getElementName());
    }

    return list;
  }

  public static IMethod getMethod(IType type, String methodName) throws JavaModelException {
    IMethod fields[] = type.getMethods();
    IMethod aimethod[];
    int j = (aimethod = fields).length;
    for (int i = 0; i < j; i++) {
      IMethod field = aimethod[i];
      if (field.getElementName().equals(methodName))
        return field;
    }

    return null;
  }

  public static String validateMethodGeneration(IType type) throws JavaModelException {
    if (type.isReadOnly())
      return "File is read only";
    if (type.isInterface())
      return "Unable to generate method for Interface";
    else
      return null;
  }

  public static boolean isStaticField(IField field) throws JavaModelException {
    int flag = field.getFlags();
    return Flags.isStatic(flag);
  }
}
