/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.guvnor.client.modeldriven.ui;

import java.util.List;

import org.drools.guvnor.client.messages.Constants;
import org.drools.guvnor.client.modeldriven.HumanReadable;
import org.drools.guvnor.client.resources.OperatorsCss;
import org.drools.guvnor.client.resources.OperatorsResource;
import org.drools.ide.common.client.modeldriven.SuggestionCompletionEngine;
import org.drools.ide.common.client.modeldriven.brl.CEPWindow;
import org.drools.ide.common.client.modeldriven.brl.HasCEPWindow;
import org.drools.ide.common.shared.SharedConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Drop-down Widget for Operators including supplementary controls for CEP
 * operator parameters
 */
public class CEPWindowOperatorsDropdown extends Composite
    implements
    HasValueChangeHandlers<OperatorSelection> {

    private static final Constants         constants                        = ((Constants) GWT.create( Constants.class ));
    private static final OperatorsResource resources                        = GWT.create( OperatorsResource.class );
    private static final OperatorsCss      css                              = resources.operatorsCss();

    private List<String>                   operators;
    private ListBox                        box;
    private HorizontalPanel                container                        = new HorizontalPanel();

    protected HasCEPWindow                 hcw;

    //Parameter value defining the server-side class used to generate DRL for CEP operator parameters (key is in droolsjbpm-ide-common)
    private static final String            CEP_OPERATOR_PARAMETER_GENERATOR = "org.drools.ide.common.server.util.CEPWindowOperatorParameterDRLBuilder";

    public CEPWindowOperatorsDropdown(List<String> operators,
                                      HasCEPWindow hcw) {
        this.hcw = hcw;
        this.operators = operators;

        HorizontalPanel hp = new HorizontalPanel();
        hp.setStylePrimaryName( css.container() );
        hp.add( getDropDown() );
        hp.add( getOperatorExtension() );

        initWidget( hp );
    }

    /**
     * Gets the index of the currently-selected item.
     * 
     * @return
     */
    public int getSelectedIndex() {
        return box.getSelectedIndex();
    }

    /**
     * Gets the value associated with the item at a given index.
     * 
     * @param index
     * @return
     */
    public String getValue(int index) {
        return box.getValue( index );
    }

    //Additional widget for CEP Window operator parameter
    private Widget getOperatorExtension() {
        container.setStylePrimaryName( css.container() );
        return container;
    }

    //Hide\display the additional CEP widget is appropriate
    private void operatorChanged(OperatorSelection selection) {
        container.clear();
        String operator = selection.getValue();

        if ( SuggestionCompletionEngine.isCEPWindowOperatorTime( operator ) ) {
            AbstractRestrictedEntryTextBox txt = new CEPTimeParameterTextBox( hcw.getWindow(),
                                                                              1 );
            initialiseTextBox( txt );
        } else if ( SuggestionCompletionEngine.isCEPWindowOperatorLength( operator ) ) {
            AbstractRestrictedEntryTextBox txt = new CEPLengthParameterTextBox( hcw.getWindow(),
                                                                                1 );
            initialiseTextBox( txt );
        } else {
            container.setVisible( false );
            hcw.getWindow().clearParameters();
        }
    }

    private void initialiseTextBox(AbstractRestrictedEntryTextBox txt) {
        String key = String.valueOf( 1 );
        String value = hcw.getWindow().getParameter( key );
        if ( value == null ) {
            value = "";
            hcw.getWindow().setParameter( key,
                                          value );
        }
        if ( !txt.isValidValue( value ) ) {
            value = "";
            hcw.getWindow().setParameter( key,
                                          value );
        }
        txt.setText( value );
        container.add( txt );
        container.setVisible( true );
        hcw.getWindow().setParameter( SharedConstants.OPERATOR_PARAMETER_GENERATOR,
                                      CEP_OPERATOR_PARAMETER_GENERATOR );

    }

    //Actual drop-down
    private Widget getDropDown() {

        String selected = "";
        String selectedText = "";
        box = new ListBox();

        box.addItem( constants.noCEPWindow(),
                     "" );
        for ( int i = 0; i < operators.size(); i++ ) {
            String op = operators.get( i );
            box.addItem( HumanReadable.getOperatorDisplayName( op ),
                         op );
            if ( op.equals( hcw.getWindow().getOperator() ) ) {
                selected = op;
                selectedText = HumanReadable.getOperatorDisplayName( op );
                box.setSelectedIndex( i + 1 );
            }
        }
        selectItem( hcw.getWindow().getOperator() );

        //Fire event to ensure parent Widgets correct their state depending on selection
        final HasValueChangeHandlers<OperatorSelection> source = this;
        final OperatorSelection selection = new OperatorSelection( selected,
                                                                   selectedText );
        Scheduler.get().scheduleFinally( new Command() {

            public void execute() {
                operatorChanged( selection );
                ValueChangeEvent.fire( source,
                                       selection );
            }

        } );

        //Signal parent Widget whenever a change happens
        box.addChangeHandler( new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                String selected = box.getValue( box.getSelectedIndex() );
                String selectedText = box.getItemText( box.getSelectedIndex() );
                OperatorSelection selection = new OperatorSelection( selected,
                                                                     selectedText );
                operatorChanged( selection );
                ValueChangeEvent.fire( source,
                                       selection );
            }
        } );

        return box;
    }

    /**
     * Allow parent Widgets to register for events when the operator changes
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<OperatorSelection> handler) {
        return addHandler( handler,
                           ValueChangeEvent.getType() );
    }

    /**
     * Select a given item in the drop-down
     * 
     * @param operator
     *            The DRL operator, not the HumanReadable form
     */
    public void selectItem(String operator) {
        String currentOperator = box.getValue( box.getSelectedIndex() );
        if ( currentOperator.equals( operator ) ) {
            return;
        }
        for ( int i = 0; i < box.getItemCount(); i++ ) {
            String op = box.getValue( i );
            if ( op.equals( operator ) ) {
                box.setSelectedIndex( i );
                break;
            }
        }
        String selected = box.getValue( box.getSelectedIndex() );
        String selectedText = box.getItemText( box.getSelectedIndex() );
        OperatorSelection selection = new OperatorSelection( selected,
                                                             selectedText );
        operatorChanged( selection );
    }

}
