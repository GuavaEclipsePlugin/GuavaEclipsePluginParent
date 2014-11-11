package com.builder.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.internal.WorkbenchMessages;

import com.builder.dto.MethodInsertionPoint;

@SuppressWarnings({"restriction", "rawtypes", "unchecked"})
public class GenericDialogBox extends SelectionDialog
{

    public GenericDialogBox(Shell parentShell, MethodInsertionPoint insertionPoint, List fieldNames, IStructuredContentProvider contentProvider, ILabelProvider labelProvider, String message)
        throws JavaModelException
    {
        super(parentShell);
        cancelPressed = true;
        setTitle(message);
        this.insertionPoint = insertionPoint;
        List input = fieldNames;
        inputElement = input;
        this.contentProvider = contentProvider;
        this.labelProvider = labelProvider;
        if(message != null)
            setMessage(message);
        else
            setMessage(WorkbenchMessages.ListSelection_message);
    }

    private void addSelectionButtons(Composite composite)
    {
        Composite buttonComposite = new Composite(composite, 0);
        GridLayout layout = new GridLayout();
        layout.numColumns = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(4);
        buttonComposite.setLayout(layout);
        buttonComposite.setLayoutData(new GridData(0x1000008, 128, true, false));
        Button selectButton = createButton(buttonComposite, 18, SELECT_ALL_TITLE, false);
        org.eclipse.swt.events.SelectionListener listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e)
            {
                listViewer.setAllChecked(true);
            }

            final GenericDialogBox gdb;
            
            {
            	gdb = GenericDialogBox.this;
            }
        }
;
        selectButton.addSelectionListener(listener);
        Button deselectButton = createButton(buttonComposite, 19, DESELECT_ALL_TITLE, false);
        listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e)
            {
                listViewer.setAllChecked(false);
            }

            final GenericDialogBox gdb;
            
            {
            	gdb = GenericDialogBox.this;
            }
        }
;
        deselectButton.addSelectionListener(listener);
    }

    private void checkInitialSelections()
    {
        for(Iterator itemsToCheck = getInitialElementSelections().iterator(); itemsToCheck.hasNext(); listViewer.setChecked(itemsToCheck.next(), true));
    }

    protected void configureShell(Shell shell)
    {
        super.configureShell(shell);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(shell, "org.eclipse.ui.list_selection_dialog_context");
    }

    protected Control createDialogArea(Composite parent)
    {
        try
        {
            Composite composite = (Composite)super.createDialogArea(parent);
            initializeDialogUnits(composite);
            createMessageArea(composite);
            listViewer = CheckboxTableViewer.newCheckList(composite, 2048);
            GridData data = new GridData(1808);
            data.heightHint = 250;
            data.widthHint = 300;
            listViewer.getTable().setLayoutData(data);
            listViewer.setLabelProvider(labelProvider);
            listViewer.setContentProvider(contentProvider);
            addTypeCombo(composite);
            addSelectionButtons(composite);
            initializeViewer();
            if(!getInitialElementSelections().isEmpty())
                checkInitialSelections();
            applyDialogFont(composite);
            return composite;
        }
        catch(JavaModelException e)
        {
            e.printStackTrace();
        }
        return parent;
    }

    private void addTypeCombo(Composite composite)
        throws JavaModelException
    {
        GridData data1 = new GridData(1808);
        Label label = new Label(composite, 8);
        label.setText("Insertion Point:");
        label.setLayoutData(data1);
        typeCombo = new Combo(composite, 8);
        typeCombo.setLayoutData(data1);
        int defaultSelectionIndex = 1;
        typeCombo.add("Last Element");
        IJavaElement elements[] = insertionPoint.getInsertionType().getChildren();
        IJavaElement aijavaelement[];
        int k = (aijavaelement = elements).length;
        for(int i = 0; i < k; i++)
        {
            IJavaElement javaElement = aijavaelement[i];
            if(javaElement instanceof IType)
                typeCombo.add((new StringBuilder("Before '")).append(javaElement.getElementName()).append("'").toString());
            else
            if(javaElement instanceof IMethod)
                typeCombo.add((new StringBuilder("Before '")).append(javaElement.getElementName()).append("()'").toString());
            else
            if(javaElement instanceof IField)
                typeCombo.add((new StringBuilder("Before '")).append(javaElement.getElementName()).append("'").toString());
        }

        if(insertionPoint.getInsertionMember() == null)
        {
            typeCombo.select(0);
        } else
        {
            IJavaElement aijavaelement1[];
            int l = (aijavaelement1 = elements).length;
            for(int j = 0; j < l; j++)
            {
                IJavaElement javaElement = aijavaelement1[j];
                if(javaElement.equals(insertionPoint.getInsertionMember()))
                {
                    typeCombo.select(defaultSelectionIndex);
                    break;
                }
                defaultSelectionIndex++;
            }

        }
        listViewer.setAllChecked(true);
    }

    protected CheckboxTableViewer getViewer()
    {
        return listViewer;
    }

    private void initializeViewer()
    {
        listViewer.setInput(inputElement);
    }

    protected void okPressed()
    {
        cancelPressed = false;
        Object children[] = contentProvider.getElements(inputElement);
        if(children != null)
        {
            ArrayList list = new ArrayList();
            for(int i = 0; i < children.length; i++)
            {
                Object element = children[i];
                if(listViewer.getChecked(element))
                    list.add(element);
            }

            setResult(list);
        }
        try
        {
            if(typeCombo.getText().equals("Last Element"))
            {
                insertionPoint.setInsertionMember(null);
            } else
            {
                int i = typeCombo.getSelectionIndex();
                insertionPoint.setInsertionMember(insertionPoint.getInsertionType().getChildren()[i - 1]);
            }
        }
        catch(JavaModelException e)
        {
            e.printStackTrace();
        }
        super.okPressed();
    }

    public MethodInsertionPoint getInsertionPoint()
    {
        return insertionPoint;
    }

    public boolean isCancelPressed()
    {
        return cancelPressed;
    }

    static String SELECT_ALL_TITLE;
    static String DESELECT_ALL_TITLE;
    private Object inputElement;
    private ILabelProvider labelProvider;
    private IStructuredContentProvider contentProvider;
    CheckboxTableViewer listViewer;
    private static final int SIZING_SELECTION_WIDGET_HEIGHT = 250;
    private static final int SIZING_SELECTION_WIDGET_WIDTH = 300;
    private Combo typeCombo;
    private MethodInsertionPoint insertionPoint;
    private boolean cancelPressed;

    static 
    {
        SELECT_ALL_TITLE = WorkbenchMessages.SelectionDialog_selectLabel;
        DESELECT_ALL_TITLE = WorkbenchMessages.SelectionDialog_deselectLabel;
    }
}
