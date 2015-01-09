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
package net.sf.guavaeclipse.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.guavaeclipse.dto.MethodInsertionPoint;

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
import org.eclipse.swt.events.SelectionListener;
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

@SuppressWarnings({"restriction", "rawtypes", "unchecked"})
public class GenericDialogBox extends SelectionDialog {

  private static String SELECT_ALL_TITLE = WorkbenchMessages.SelectionDialog_selectLabel;
  private static String DESELECT_ALL_TITLE = WorkbenchMessages.SelectionDialog_deselectLabel;
  private List<String> inputElement;
  private ILabelProvider labelProvider;
  private IStructuredContentProvider contentProvider;
  private CheckboxTableViewer listViewer;
  private static final int SIZING_SELECTION_WIDGET_HEIGHT = 250;
  private static final int SIZING_SELECTION_WIDGET_WIDTH = 300;
  private Combo typeCombo;
  private MethodInsertionPoint insertionPoint;
  private boolean cancelPressed;

  public GenericDialogBox(Shell parentShell, MethodInsertionPoint insertionPoint,
      List<String> fieldNames, IStructuredContentProvider contentProvider,
      ILabelProvider labelProvider, String message) throws JavaModelException {
    super(parentShell);
    cancelPressed = true;
    setTitle(message);
    this.insertionPoint = insertionPoint;
    this.inputElement = fieldNames;
    this.contentProvider = contentProvider;
    this.labelProvider = labelProvider;
    if (message != null) {
      setMessage(message);
    } else {
      setMessage(WorkbenchMessages.ListSelection_message);
    }
  }

  private void addSelectionButtons(Composite composite) {
    Composite buttonComposite = new Composite(composite, 0);
    GridLayout layout = new GridLayout();
    layout.numColumns = 0;
    layout.marginWidth = 0;
    layout.horizontalSpacing = convertHorizontalDLUsToPixels(4);
    buttonComposite.setLayout(layout);
    buttonComposite.setLayoutData(new GridData(0x1000008, 128, true, false));
    Button selectButton = createButton(buttonComposite, 18, SELECT_ALL_TITLE, false);
    SelectionListener listener = new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        listViewer.setAllChecked(true);
      }

    };
    selectButton.addSelectionListener(listener);
    Button deselectButton = createButton(buttonComposite, 19, DESELECT_ALL_TITLE, false);
    listener = new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        listViewer.setAllChecked(false);
      }

    };
    deselectButton.addSelectionListener(listener);
  }

  private void checkInitialSelections() {
    for (String itemToCheck : this.inputElement) {
      listViewer.setChecked(itemToCheck, true);
    }
  }

  @Override
  protected void configureShell(Shell shell) {
    super.configureShell(shell);
    PlatformUI.getWorkbench().getHelpSystem()
        .setHelp(shell, "org.eclipse.ui.list_selection_dialog_context");
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    try {
      Composite composite = (Composite) super.createDialogArea(parent);
      initializeDialogUnits(composite);
      createMessageArea(composite);
      listViewer = CheckboxTableViewer.newCheckList(composite, 2048);
      GridData data = new GridData(1808);
      data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
      data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
      listViewer.getTable().setLayoutData(data);
      listViewer.setLabelProvider(labelProvider);
      listViewer.setContentProvider(contentProvider);
      addTypeCombo(composite);
      addSelectionButtons(composite);
      initializeViewer();
      if (getInitialElementSelections().isEmpty()) {
        checkInitialSelections();
      }
      applyDialogFont(composite);
      return composite;
    } catch (JavaModelException e) {
      e.printStackTrace();
    }
    return parent;
  }

  private void addTypeCombo(Composite composite) throws JavaModelException {
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
    for (int i = 0; i < k; i++) {
      IJavaElement javaElement = aijavaelement[i];
      if (javaElement instanceof IType)
        typeCombo.add((new StringBuilder("Before '")).append(javaElement.getElementName())
            .append("'").toString());
      else if (javaElement instanceof IMethod)
        typeCombo.add((new StringBuilder("Before '")).append(javaElement.getElementName())
            .append("()'").toString());
      else if (javaElement instanceof IField)
        typeCombo.add((new StringBuilder("Before '")).append(javaElement.getElementName())
            .append("'").toString());
    }

    if (insertionPoint.getInsertionMember() == null) {
      typeCombo.select(0);
    } else {
      IJavaElement aijavaelement1[];
      int l = (aijavaelement1 = elements).length;
      for (int j = 0; j < l; j++) {
        IJavaElement javaElement = aijavaelement1[j];
        if (javaElement.equals(insertionPoint.getInsertionMember())) {
          typeCombo.select(defaultSelectionIndex);
          break;
        }
        defaultSelectionIndex++;
      }

    }
    listViewer.setAllChecked(true);
  }

  protected CheckboxTableViewer getViewer() {
    return listViewer;
  }

  private void initializeViewer() {
    listViewer.setInput(inputElement);
  }

  @Override
  protected void okPressed() {
    cancelPressed = false;
    Object children[] = contentProvider.getElements(inputElement);
    if (children != null) {
      ArrayList list = new ArrayList();
      for (int i = 0; i < children.length; i++) {
        Object element = children[i];
        if (listViewer.getChecked(element))
          list.add(element);
      }

      setResult(list);
    }
    try {
      if (typeCombo.getText().equals("Last Element")) {
        insertionPoint.setInsertionMember(null);
      } else {
        int i = typeCombo.getSelectionIndex();
        insertionPoint.setInsertionMember(insertionPoint.getInsertionType().getChildren()[i - 1]);
      }
    } catch (JavaModelException e) {
      e.printStackTrace();
    }
    super.okPressed();
  }

  public MethodInsertionPoint getInsertionPoint() {
    return insertionPoint;
  }

  public boolean isCancelPressed() {
    return cancelPressed;
  }

  public List<String> getResultAsList() {
    Object result[] = getResult();
    if (result == null) {
      return null;
    }
    List<String> fields = new ArrayList<String>(result.length);
    for (int i = 0; i < result.length; i++) {
      fields.add((String) result[i]);
    }
    return fields;
  }

}
