package com.builder.utils;

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

@SuppressWarnings({"rawtypes", "unchecked"})
public class Utils
{

	private Utils()
    {
    }

    public static IType[] getIType(IFile file)
        throws JavaModelException
    {
        IResource resource = file.getParent().findMember(file.getName());
        return getIType(resource);
    }

    public static IType[] getIType(IResource resource)
        throws JavaModelException
    {
        ICompilationUnit java = (ICompilationUnit)JavaCore.create(resource);
        return java.getTypes();
    }

    public static List getFieldNames(IType type)
        throws JavaModelException
    {
        IField fields[] = type.getFields();
        List list = new ArrayList();
        IField aifield[];
        int j = (aifield = fields).length;
        for(int i = 0; i < j; i++)
        {
            IField field = aifield[i];
            list.add(field.getElementName());
        }

        return list;
    }
    
    
    // use goog
    public static List<IField> getFields(IType type)
            throws JavaModelException
        {
            IField fields[] = type.getFields();
            List<IField> list = new ArrayList<IField>();
            IField aifield[];
            int j = (aifield = fields).length;
            for(int i = 0; i < j; i++)
            {
                IField field = aifield[i];
                list.add(field);
            }

            return list;
        }

    public static List getNonStaticFieldNames(IType type)
        throws JavaModelException
    {
        IField fields[] = type.getFields();
        List list = new ArrayList();
        IField aifield[];
        int j = (aifield = fields).length;
        for(int i = 0; i < j; i++)
        {
            IField field = aifield[i];
            if(!isStaticField(field))
                list.add(field.getElementName());
        }

        return list;
    }

    public static List getMethodNames(IType type)
        throws JavaModelException
    {
        IMethod fields[] = type.getMethods();
        List list = new ArrayList();
        IMethod aimethod[];
        int j = (aimethod = fields).length;
        for(int i = 0; i < j; i++)
        {
            IMethod field = aimethod[i];
            list.add(field.getElementName());
        }

        return list;
    }

    public static IMethod getMethod(IType type, String methodName)
        throws JavaModelException
    {
        IMethod fields[] = type.getMethods();
        IMethod aimethod[];
        int j = (aimethod = fields).length;
        for(int i = 0; i < j; i++)
        {
            IMethod field = aimethod[i];
            if(field.getElementName().equals(methodName))
                return field;
        }

        return null;
    }

    public static String validateMethodGeneration(IType type)
        throws JavaModelException
    {
        if(type.isReadOnly())
            return "File is read only";
        if(type.isInterface())
            return "Unable to generate method for Interface";
        else
            return null;
    }

    public static boolean isStaticField(IField field)
        throws JavaModelException
    {
        int flag = field.getFlags();
        return Flags.isStatic(flag);
    }
}
